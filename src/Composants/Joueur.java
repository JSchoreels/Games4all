package Composants;

/**
 *  Classe abstraite Joueur, implementant <b>IJoueur</b>, permettant de
 * connaitre rapidement le type d'un joueur (Humain, Aleatoire, MinMax, ...),
 * ainsi que son nom. Cela donne plus de liberte et de personalisation dans
 * l'implementation que l'interface <b>IJoueur</b>.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @see Humain
 * @see Ordinateur
 * @see Aleatoire
 * @see MinMax
 * @see AlphaBeta
 */
public abstract class Joueur implements IJoueur {

    protected String name = "";
    protected int typeJoueur;

    /**
     *  Methode qui retourne le nom du joueur, sous forme d'une String.
     *
     * @return Chaine de caractères, représentant le pseudo de ce joueur.
     */
    public String getName() {
        return name;
    }

    /**
     * Methode qui retourne le type de joueur, sous forme d'un entier.
     *
     * @return Entier representant le type de joueur dont il s'agit.
     */
    public int getTypeJoueur() {
        return typeJoueur;
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
