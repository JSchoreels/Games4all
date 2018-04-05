package Composants;

/**
 *  Cette interface regroupe tous les jeux qui comportent en leur sein, le
 * concept de score. Par exemple, l'othello peut avoir le concept de score
 * courant, selon les pions deja en possession d'un joueur.
 * 
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public interface IScore {

    /**
     *  Cette methode permet d'obtenir le score d'un joueur dont on fournit
     * l'identifiant numerique.
     *
     * @param numJ Entier qui identifie le joueur dont on veut connaitre le
     * score.
     * @return Entier qui represente le score du joueur.
     */
    public int obtenirScore(int numJ);
}
