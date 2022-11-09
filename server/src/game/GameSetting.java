package game;

import game.settings.Difficulty;

public class GameSetting {
    Difficulty difficulty = Difficulty.NORMAL;
    
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        if (difficulty != null) {
            this.difficulty = difficulty;
        }
    }
}
