package Composants;

/**
 *  Classe abstraite regroupant tous les algorithmes utilises pour les IA.
 * 
 * @author Jonathan Schoreels & Alexandre Devaux
 * @see Aleatoire
 * @see MinMax
 * @see AlphaBeta
 */
public abstract class Ordinateur extends Joueur {

    protected int typeJeu;

    /**
     *  Cette methode retourne le maximum d'un tableau d'entier.
     *
     * @param liste 
     * @return Le plus grand entier contenu dans le tableau d'entier.
     */
    public static int maximum(int[] liste) {
        int result = liste[0];
        for (int k = 1; k < liste.length; k++) {
            if (liste[k] > result) {
                result = liste[k];
            }
        }
        return result;
    }

    /**
     *  Cette methode retourne l'indice du maximum d'un tableau d'entier.
     * 
     * @param liste 
     * @return Entier representant l'index du plus grand entier contenu dans le
     * tableau d'entier fourni en parametre.
     */
    public static int indiceofmaximum(int[] liste) {
        int result = 0;
        for (int k = 1; k < liste.length; k++) {
            if (liste[k] > liste[result]) {
                result = k;
            }
        }
        return result;
    }

    /**
     *  Cette methode retourne le minimum d'un tableau d'entier.
     *
     * @param liste 
     * @return Le plus petit entier contenu dans le tableau d'entier.
     */
    public static int minimum(int[] liste) {
        int result = liste[0];
        for (int k = 1; k < liste.length; k++) {
            if (liste[k] < result) {
                result = liste[k];
            }
        }
        return result;
    }

    /**
     *  Cette methode permet de retourner une instance particuliere de tableau,
     * selon le type de l'intelligence artificielle. Ce type est passe en
     * parametre via le constructeur de l'IA, et est alors immuable, par
     * simple preference des developpeurs.
     *
     * Les signatures numeriques sont les suivantes :
     *  La valeur : 0, represente un jeu du Morpion.
     *              1, represente un jeu du Puissance 4.
     *              2, represente un jeu de l'Othello.
     * 
     * @return Nouvelle instance de Plateau convenant au type d'IA.
     */
    public Plateau typeDuPlateau() {
        switch (typeJeu) {
            case 0:
                return new PlateauMorpion(3);
            case 1:
                return new PlateauPuissance4();
            case 2:
                return new PlateauOthello(4);
            default:
                return null;
        }
    }

    /**
     *  Methode abstraite visant a obtenir le coup d'un Joueur, selon un plateau
     * et la signature numerique du joueur sur celui ci.
     *
     * @return Le <b>Coup</b> a jouer.
     */
    @Override
    public abstract Coup obtenirCoup(IPlateau plateau, int numero);
}
