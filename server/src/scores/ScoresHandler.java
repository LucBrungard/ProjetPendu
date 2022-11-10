package scores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import game.GameUtil;
import game.settings.Difficulty;

public class ScoresHandler {
    public static final String filename = System.getProperty("user.dir")
        + File.separator + "server"
        + File.separator + "scores.json";
    private static ScoresHandler instance;
    private static JSONObject scoresJSON;
    private static Map<Difficulty, Score[]> scores = new HashMap<>();

    public static ScoresHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new ScoresHandler();
        }
        return instance;
    }
    
    private ScoresHandler() throws IOException {
        // Load the file containing all the scores
        String tmp = Files.readString(Paths.get(filename, new String[]{""}));
        scoresJSON = new JSONObject(tmp);

        // For each difficulty
        Iterator<String> keys = scoresJSON.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONArray scoreForDifficulty = scoresJSON.getJSONArray(key);

            Score[] scoreList = new Score[10];
            scoreForDifficulty.forEach(score -> {
                JSONObject scoreObject = (JSONObject) score;
                String name = scoreObject.getString("name");
                int scoreValue = scoreObject.getInt("score");

                this.insertSortDesc(scoreList, name, scoreValue);
            });
            scores.put(Difficulty.fromString(key), scoreList);
        }
    };

    /**
     * Insert a score in a list if the value is > to any value in the list
     * @param scoreList The list to insert the value
     * @param name The name of who scored
     * @param scoreValue The score value
     */
    private void insertSortDesc(Score[] scoreList, String name, int scoreValue) {
        // Go to the last value being >= scoreValue
        int i;
        for (i = 0; i < scoreList.length; i++) {
            if (scoreList[i] == null || scoreList[i].getScore() < scoreValue) break;
        }

        final int tmpI = i;

        if (tmpI != scoreList.length) {
            // Push all the values after an additional index
            for (i = scoreList.length-2; i >= tmpI; i--) {
                scoreList[i+1] = scoreList[i];
            }

            // Saves the new value
            scoreList[tmpI] = new Score(name, scoreValue);
        }
    }

    public void addScore(Difficulty difficulty, String name, int score) {
        // Get the score list for the difficulty
        Score[] scoreList = scores.get(difficulty);

        // Update the list
        this.insertSortDesc(scoreList, name, score);

        // Save the list
        scores.put(difficulty, scoreList);

        // Update the JSON object
        scoresJSON.put(difficulty.value, scoreList);

        // Save to file
        writeScores();
    }

    private void writeScores() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(filename);
                    FileChannel channel = fileOutputStream.getChannel();                
                    FileLock lock = null;
                    do {
                        try {
                            lock = channel.tryLock();
            
                            fileOutputStream.write(scoresJSON.toString().getBytes());
                            lock.release();
                        } catch (OverlappingFileLockException e) {}
                    } while (lock == null);
                    fileOutputStream.close();
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public String getLeaderBoardString(Difficulty difficulty) {
        String res = GameUtil.captitalize(difficulty.value);
        res = res.concat("\n===============================");

        for (Score score : scores.get(difficulty)) {
            if (score == null) break;
            res = res.concat("    " + score.getName() + " : " + score.getScore());
        }

        res = res.concat("\n===============================\n");

        return res;
    }

    public void showScores() {
        scores.forEach((key, value) -> {
            System.out.println(key.value);
            for (Score score : value) {
                System.out.println("    " + score);
            }
        });
    }
}
