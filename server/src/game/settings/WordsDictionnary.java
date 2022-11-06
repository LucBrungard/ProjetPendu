package game.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordsDictionnary {
    private WordsDictionnary() {};

    private static final List<String> words = new ArrayList<String>() {{
        add("abeille");
        add("bélier");
        add("courir");
        add("duplicata");
        add("estampille");
        add("fournir");
        add("gesticuler");
        add("hélice");
    }};

    public static String getRandomWord() {
        int size = words.size();
        int idx = new Random().nextInt(size);
        return words.get(idx);
    }
}
