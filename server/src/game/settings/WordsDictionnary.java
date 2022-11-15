package game.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordsDictionnary {
    private WordsDictionnary() {
    };

    public static final List<String> words2To5Letters = new ArrayList<String>() {
        {
            add("git");
            add("cap");
            add("fil");
            add("ut");
            add("ri");
            add("antre");
            add("jais");
            add("quiz");
        }
    };

    public static final List<String> words5To9Letters = new ArrayList<String>() {
        {
            add("abeille");
            add("belier");
            add("courir");
            add("duplicata");
            add("buvette");
            add("fournir");
            add("gesticule");
            add("helice");
        }
    };

    public static final List<String> words4To8Letters = new ArrayList<String>() {
        {
            add("panda");
            add("critique");
            add("cabri");
            add("champs");
            add("frire");
            add("basque");
            add("bouseux");
            add("papille");
        }
    };

    public static final List<String> words7To15Letters = new ArrayList<String>() {
        {
            add("hydroxyde");
            add("equivoque");
            add("symbolique");
            add("entassement");
            add("accumulation");
            add("deraisonnable");
            add("hexasyllabique");
            add("caracteristique");
        }
    };

    public static String getRandomWord(Difficulty difficulty) {
        int size = difficulty.words.size();
        int idx = new Random().nextInt(size);
        return difficulty.words.get(idx);
    }
}
