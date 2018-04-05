package Composants;

/**
 *  Cette classe possede uniquement de quoi represente un coup a jouer, quel que
 * soit le jeu, quel que soit la validite du coup et quelque soit le joueur qui
 * l'effectue.
 *  Notez que la numerotation des lignes et des colonnes debute a partir de zero
 * et que les lignes se comptent de haut en bas, et les colonnes de gauche a
 * droite.
 */
public class Coup {

    protected final int ligne;
    protected final int colonne;

    /**
     * Constructeur prenant en parametre la ligne et la colonne du coup a jouer.
     *
     * @param ligne La ligne sur laquelle on joue >=0
     * @param colonne La colonne sur laquelle on joue >=0
     */
    public Coup(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    /**
     *  Methode permettant d'obtenir le numero de la ligne du coup.
     * 
     * @return Le numero de la ligne >=0
     */
    public int obtenirLigne() {
        return this.ligne;
    }

    /**
     *  Methode permettant d'obtenir le numero de la colonne du coup.
     * 
     * @return Le numero de la colonne, >=0
     */
    public int obtenirColonne() {
        return this.colonne;
    }
}
