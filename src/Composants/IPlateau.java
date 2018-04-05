package Composants;

/**
 *  Interface <i>IPlateau</i> devant etre implementee par toute representation
 * d'un plateau de jeu, quel que soit le jeu. Cette interface garantit que les
 * plateaux de jeu implementent un minimum de methodes afin d'assurer
 * l'interoperabilite des joueurs.
 */
public interface IPlateau {

    /**
     *  Constante de classe entiere correspondant a la valeur d'une case du
     * plateau ne contenant aucun jeton.
     */
    public static final int AUCUN_JOUEUR = -1;

    /**
     *  Retourne le nombre de lignes du plateau.
     *
     * @return Le nombre de lignes du plateau.
     */
    public int obtenirNombreLignes();

    /**
     *  Retourne le nombre de colonnes du plateau.
     * 
     * @return Le nombre de colonnes du plateau.
     */
    public int obtenirNombreColonnes();

    /**
     *  Retourne le dernier coup joue ou null si aucun coup n'a ete joue.
     * 
     * @return Le dernier coup joue ou null si aucun coup n'a ete joue.
     */
    public Coup obtenirDernierCoup();

    /**
     *  Retourne le numero du joueur "possedant" la case en ligne "ligne" et
     * en colonne "colonne" ou la constante <i>AUCUN_JOUEUR</i> si cette case
     * est libre.
     * 
     * @param ligne La ligne (>=0)
     * @param colonne La Colonne (>=0)
     * @return Le numero du joueur "possedant" la case, ou la constante
     * AUCUN_JOUEUR si cette case est libre.
     */
    public int obtenirNumJoueur(int ligne, int colonne);
}
