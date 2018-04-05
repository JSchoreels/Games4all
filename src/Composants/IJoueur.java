package Composants;

/**
 *  Interface <i>IJoueur</i> devant etre implementee par chaque classe
 * representant un joueur (humain ou IA), peu importe le jeu.
 */
public interface IJoueur {

    /**
     *  Methode qui, sur base d'un plateau passe en parametre, calcule et
     * retourne le coup qu'elle souhaite jouer sur ce plateau pour le joueur
     * d'identifiant numero (egalement passe en parametre).
     * 
     * @param plateau Objet implementant l'interface <b>IPlateau</b>.
     * @param numero L'identifiant numerique du joueur pour lequel on calcule
     * le coup.
     * @return Le <b>Coup</b> a jouer.
     */
    public Coup obtenirCoup(IPlateau plateau, int numero);
}
