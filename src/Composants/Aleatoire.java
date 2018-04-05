package Composants;

import java.util.Random;
import java.util.ArrayList;

/**
 * Cette implementation d'IJoueur correspond a l'intelligence artificielle qui
 * joue aleatoirement un coup parmis les coups possibles, pour un jeu bien
 * defini, que l'on passe sous forme d'entier lors de l'instanciation de l'IA
 * aleatoire.(se refere a l'interface Ordinateur pour l'association entier-jeu.)
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public class Aleatoire extends Ordinateur {

    /**
     * Constructeur prenant en parametre le nom, et le type de jeu sur lequel
     * cette IA jouera.
     * 
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     */
    public Aleatoire(String name, int type) {
        typeJoueur = 1;
        typeJeu = type;
        super.name = name;
    }

    /**
     * Cette methode retourne un coup aleatoire a jouer.
     *
     * @param plateau Representation de l'etat actuel du jeu.
     * @param numero Entier representant le joueur sur le <b>IPlateau</b> qui
     * l'accompagne au premier paramètre.
     * @return Le <b>Coup</b> a jouer.
     */
    @Override
    public Coup obtenirCoup(IPlateau plateau, int numero) {
        Random rd = new Random();
        Plateau grid = typeDuPlateau();
        grid.importSituation(plateau, numero);
        ArrayList<Coup> list = grid.coupPossible(numero);
        Coup result = null;
        // On ne prend le coup, dans la liste des coups possibles, seulement.
        try {
            int pifpafpouf = rd.nextInt(list.size());
            result = list.get(pifpafpouf);
        } catch (IllegalArgumentException e) {
            System.out.println("L' IA a ete instancie pour un autre jeu");
        }
        return result;
    }
}
