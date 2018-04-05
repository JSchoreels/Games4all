package Composants;

import java.util.ArrayList;

/**
 *  Cette classe, est l'implementation de l'algorithme <i>MinMax</i>, pour les
 * intelligences artificielles. Celle-ci, herite de la classe abstraite
 * <b>Ordinateur</b>, et explore tout l'arbre de jeux, pour estimer l'interet
 * d'un <b>Coup</b>.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @see AlphaBeta
 */
public class MinMax extends Ordinateur {

    protected int profondeur = 1;
    protected int aggressivite = 0;
    protected int typeEval = 0;

    /**
     *  Constructeur permettant de preciser a une IA, un nom sous forme de
     * String et un type de jeu, represente sous forme d'un entier. Cf la classe
     * abstraite <b>Ordinateur</b> pour les associations jeux-entiers.
     *
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     */
    public MinMax(String name, int type) {
        this(name, type, 3, 0, 1);
    }

    /**
     *  Constructeur identique au precedent, mais qui permet en plus de preciser
     * une profondeur sous forme d'entier de recherche preferee.
     * 
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     * @param n Entier representant la profondeur d'exploration.
     */
    public MinMax(String name, int type, int n) {
        this(name, type, n, 0, 1);
    }

    /**
     *  Constructeur complet, permettant en plus du precedent, de preciser une
     * aggressivite via un entier (allant de 0 a 10, en dehors de ces marges,
     * le comportement de l'IA pourrait etre incomprehensible), et le type
     * d'evaluation que l'on veut appliquer aux feuilles de l'arbre (Details sur
     * celles ci presentes dans la documentation des plateaux).
     * 
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     * @param n Entier representant la profondeur d'exploration.
     * @param balance Entier representant la valeur de l'agressivite.
     * @param typeEvaluation Entier representant le type d'evaluation.
     */
    public MinMax(String name, int type, int n, int balance, int typeEvaluation) {
        typeJoueur = 2;
        super.typeJeu = type;
        super.name = name;
        this.profondeur = n;
        this.aggressivite = balance;
        this.typeEval = typeEvaluation;
    }

    /**
     *  Implementation d'obtenirCoup de l'interface IJoueur, cette methode
     * enclenche le mecanisme de calcul de coup, selon l'algorithme MinMax.
     *
     * @param plateau Une representation du jeu.
     * @param num Entier identifiant le joueur qui doit jouer a cet instant.
     * @return Le <b>Coup</b> a jouer.
     */
    @Override
    public Coup obtenirCoup(IPlateau plateau, int num) {
        Coup result;
        Plateau grid = typeDuPlateau();
        grid.importSituation(plateau, num);
        result = coupAfaire(grid, num);
        return result;
    }

    /**
     *  Methode permettant de mettre en branle la mecanique d'exploration de
     * l'arbre de jeux, de facon recursive, celle ci retourne un Coup a jouer.
     *
     * @param plateau Une representation du jeu.
     * @param num Entier representant le joueur qui doit jouer a un instant.
     * @return Un <b>Coup</b> a jouer.
     */
    protected Coup coupAfaire(Plateau plateau, int num) {
        Coup result;
        ArrayList<Coup> cpJouable = plateau.coupPossible(num);
        int xMax = plateau.obtenirNombreLignes();
        int yMax = plateau.obtenirNombreColonnes();
        int[] value = new int[cpJouable.size()];
        int[] secondary_value = new int[cpJouable.size()];
        if (plateau.getEspaceLibre() == (yMax * xMax)) {
            result = cpJouable.get(cpJouable.size() / 2);
            return result;
        }
        for (int i = 0; i < cpJouable.size(); i++) {
            plateau.appliquer(cpJouable.get(i), num);
            value[i] = explorationArbreJeu(plateau, num, false, profondeur);
            plateau.retourArriere();
        }
        int max = maximum(value);
        int[] aDepartager = new int[value.length];
        for (int k = 0; k < value.length; k++) {
            if (value[k] == max) {
                aDepartager[k] = secondary_value[k];
            } else {
                aDepartager[k] = Integer.MIN_VALUE;
            }
        }
        int indice = indiceofmaximum(aDepartager);
        result = cpJouable.get(indice);

        return result;
    }

    /**
     *  Methode d'exploration de l'arbre de jeux, par l'algorithme MinMax,
     * retourne sous forme d'entier, une estimation d'une situation de jeux.
     * 
     * @param plat Plateau qui est une situation du jeu.
     * @param num Entier qui est la signature numerique du joueur sur le plateau
     * @param ismax Booleen qui retourne vrai si on est dans un noeud max
     * @param n Entier qui compte les couches de l'arbre a encore explorer.
     * @return un Entier etant l'estimation de la situation fournie en parametre
     */
    protected int explorationArbreJeu(Plateau plat, int num, boolean ismax,
            int n) {
        int moi = num;
        int adversaire = (num + 1) % 2;
        int current = adversaire;
        if (ismax) {
            current = moi;
        }
        ArrayList<Coup> cpJouable = plat.coupPossible(current);
        int[] valeur = new int[cpJouable.size()];
        if (plat.finPartie() != -2) {
            n = 0;
        }
        if (n == 0) {
            return plat.runEvaluation(moi, aggressivite, typeEval);
        } else if (cpJouable.size() > 0) {
            for (int i = 0; i < cpJouable.size(); i++) {
                plat.appliquer(cpJouable.get(i), current);
                valeur[i] = explorationArbreJeu(plat, num, !ismax, n - 1);
                plat.retourArriere();
            }
            if (ismax) {
                return maximum(valeur);
            } else {
                return minimum(valeur);
            }
        } else {
            return explorationArbreJeu(plat, num, !ismax, n - 1);
        }

    }
}





