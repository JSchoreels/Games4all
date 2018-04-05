package Composants;

/**
 *  Classe implementant <b>IJoueur</b>, representant un joueur dirige par un
 * humain.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public class Humain extends Joueur {

    private Coup toDo;

    /**
     *  Constructeur prenant en parametre une String permettant d'identifier un
     * joueur, de facon plus personnelle.
     *
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     */
    public Humain(String name) {
        super.name = name;
        super.typeJoueur = 0;
    }

    /**
     *  Methode qui retourne le coup, qui resulte de l'update.
     *
     * @param plateau Representation de l'etat actuel du jeu.
     * @param numero Entier representant le joueur sur le <b>IPlateau</b> qui
     * l'accompagne au premier paramètre.
     *
     * @return Le <b>Coup</b> a jouer.
     */
    @Override
    public Coup obtenirCoup(IPlateau plateau, int numero) {
        return toDo;
    }

    /**
     *  Methode, enregistrant un coup dans le champ toDo, permettant de le
     * retourner plus tard (Cette methode s'appelle grace a un listener
     * detectant la position du clic de l'humain sur la grille).
     *
     * @param cp <b>Coup</b> ou le joueur humain a clique sur la grille.
     */
    public void update(Coup cp) {
        toDo = cp;
    }
}
