package game.modes;

import game.Game;
import game.GameSetting;

public class Classic extends Game {
    public Classic(GameSetting settings) {
        super(settings);
    }

    @Override
    public void startSpecialRule() {}

    @Override
    public String getDescription() {
        String res = "C'est le mode classique du jeu de pendu : ";
        res = res.concat("\n\t- On vous donne un mot");
        res = res.concat("\n\t- Proposez une lettre ou un mot");
        res = res.concat("\n\t- Si vous vous êtes trompé dans votre proposition, votre compte d'erreur augmente");
        res = res.concat("\n\t- Lorsque vous arrivez au maximum d'erreurs possibles, vous perdez la partie");
        res = res.concat("\n\t- Si vous trouvez le mot avant de dépasser le nombre d'erreurs maximales, vous ganez la partie");
        res = res.concat("\nBonne chance");

        return res;
    }
}
