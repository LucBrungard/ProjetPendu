package game.modes;

import game.Game;
import game.GameSetting;

public class Classic extends Game {
   public Classic(GameSetting settings) {
      super(settings);
   }

   @Override
   public void startSpecialRule() {
      this.mode = "classic";
   }

   public static String getFullDescription() {
      String res = "===================================\n";
      res = res.concat("=========== CLASSIQUE =============\n");
      res = res.concat("===================================\n\n");

      res = res.concat("Limite de temps : aucune\n");
      res = res.concat("Nombre d'essais : En fonction de la difficulté\n\n");

      res = res.concat("Détails : C'est le mode classique du jeu de pendu :\n");
      res = res.concat("\t- On vous donne un mot\n");
      res = res.concat(
            "\t- Si vous vous êtes trompé dans votre proposition, votre compte d'erreur augmente\n");
      res = res.concat(
            "\t- Lorsque vous arrivez au maximum d'erreurs possibles, vous perdez la partie\n");
      res = res.concat(
            "\t- Si vous trouvez le mot avant de dépasser le nombre d'erreurs maximales, vous ganez la partie\n");
      res = res.concat("Refléchissez bien avant de jouer !\n");
      return res;
   }

}
