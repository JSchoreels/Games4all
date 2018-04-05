package Composants;

import java.util.ArrayList;

/**
 *  Cette classe represente l'intelligence artificielle qui basera sa recherche
 * recursive selon l'algorithme de l'elagage Alpha-Beta. Celui-ci peut etre
 * jusqu'a 30 fois plus rapides que son homologue Min-Max, selon les situations.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public class AlphaBeta extends MinMax {

    /**
     *  Constructeur par defaut, ne prenant en parametre qu'un nom, et la
     * signature numerique du jeu sur lequel on joue.(cf. associations presentes
     * entre entier et jeu, dans l'interface Ordinateur.)
     *
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     */
    public AlphaBeta(String name, int type) {
        super(name, type, 5, 0, 1);
    }

    /**
     *  Constructeur par defaut, identique au precedent, mais qui permet en plus
     * de passer en parametre la profondeur en demi-coups voulue.
     *
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     * @param n Entier representant la profondeur d'exploration.
     */
    public AlphaBeta(String name, int type, int n) {
        super(name, type, n, 0, 1);
    }

    /**
     *  Constructeur complet, permettant toujours de donner en parametre ceux
     * presents dans les 2 autres, mais en plus, de rentrer sous forme d'un
     * entier, l'aggressivite de l'IA, et en deuxieme parametre, un type
     * d'evaluation.
     *
     *  Ces types d'evaluations sont specifiques a chaque jeu, il est donc
     * preferable de se renseigner d'abord dans ces classes pour les utiliser.
     *
     * @param name Chaine de caractères, représentant le pseudo de ce joueur.
     * @param type Entier definissant quel type de jeu est entrain d'etre jouer.
     * @param n Entier representant la profondeur d'exploration.
     * @param balance Entier representant la valeur de l'agressivite.
     * @param typeEval Entier representant le type d'evaluation.
     */
    public AlphaBeta(String name, int type, int n, int balance, int typeEval) {
        super(name, type, n, balance, typeEval);
        typeJoueur = 3;
    }

    /**
     *  Methode permettant de lancer la recherche recursive sur un plateau, et
     * de fournir tout une serie d'heuristique immediate, permettant a l'IA de
     * ne pas forcement faire sa recherche recursive, si le jeu rencontre est a
     * un moment recurrent au niveau coup joue pour celui ci.
     * (Par exemple: un debut de partie).
     *
     * @param plateau Une representation du jeu.
     * @param numJ Entier representant le joueur qui doit jouer a un instant.
     * @return Un <b>Coup</b> a jouer.
     */
    @Override
    public Coup coupAfaire(Plateau plateau, int numJ) {
        ArrayList<Coup> coupJouable = plateau.coupPossible(numJ);
        Coup result = coupJouable.get(0);
        int alpha = Integer.MIN_VALUE;
        int val = Integer.MIN_VALUE;
        int xMax = plateau.obtenirNombreLignes();
        int yMax = plateau.obtenirNombreColonnes();
        if (plateau.getEspaceLibre() == (xMax * yMax)) {
            result = coupJouable.get(coupJouable.size() / 2);
            return result;
        }
        for (Coup coup : coupJouable) {
            plateau.appliquer(coup, numJ);
            val = alphabeta(plateau, numJ, alpha,
                    Integer.MAX_VALUE, false, profondeur);
            plateau.retourArriere();
            if (val > alpha) {
                result = coup;
                alpha = val;
            }

        }
        return result;
    }

    /**
     *  Methode explorant l'arbre de jeu actuel, en procedant par coupure Alpha
     * ou Beta, lorsque celles ci se presentent, selon le principe de l'elagage
     * Alpha-Beta.
     *
     * @param plateau Une representation du jeu.
     * @param numJ Entier representant le joueur qui doit jouer a un instant.
     * @param ismax Booleen etant vrai si on est dans un noeud max.
     * @param alpha Entier pour lequel l'evaluation du sous-arbre doit etre
     * superieur pour etre prise en compte.
     * @param beta Entier pour lequel l'evaluation du sous-abre doit etre
     * inferieur pour etre prise en compte.
     * @param n Entier la profondeur encore explorable
     * @return Entier l'estimation de la situation.
     */
    protected int alphabeta(Plateau plateau, int numJ, int alpha,
            int beta, boolean ismax, int n) {
        int adversaire = (numJ + 1) % 2;
        int current = adversaire;
        int currentAdversaire = numJ;
        //current et currentAdversaire permet de garder moi et adv comme
        // deux constantes.
        if (ismax) {
            current = numJ;
            currentAdversaire = adversaire;
        }
        ArrayList<Coup> coupJouable = plateau.coupPossible(current);
        // verification si la partie est toujours en cours.
        if (plateau.finPartie() != -2) {
            n = 0;
        }
        // cas ou la profondeur maximale est atteinte.
        if (n == 0) {
            return plateau.runEvaluation(numJ, aggressivite, typeEval);
        } else if (coupJouable.size() > 0) {
            // cas ou on est dans un noeud max.
            if (ismax) {
                int alphaP = Integer.MIN_VALUE;
                for (Coup coup : coupJouable) {
                    int val;
                    plateau.appliquer(coup, current);
                    val = alphabeta(plateau, numJ, Math.max(alphaP, alpha),
                            beta, !ismax, n - 1);
                    plateau.retourArriere();
                    alphaP = Math.max(alphaP, val);
                    // coupure beta.
                    if (alphaP >= beta) {
                        return alphaP;
                    }
                }
                return alphaP;
            } // noeud min.
            else {
                int betaP = Integer.MAX_VALUE;
                for (Coup coup : coupJouable) {
                    int val;
                    plateau.appliquer(coup, current);
                    val = alphabeta(plateau, numJ, alpha,
                            Math.min(betaP, beta), !ismax, n - 1);
                    plateau.retourArriere();
                    betaP = Math.min(betaP, val);
                    // coupure alpha.
                    if (betaP <= alpha) {
                        return betaP;
                    }
                }
                return betaP;
            }
        } else {
            if (plateau.coupPossible(currentAdversaire).size() > 0) {
                return alphabeta(plateau, numJ, alpha, beta, !ismax, n);
            } else {
                return plateau.runEvaluation(numJ, aggressivite, typeEval);
            }
        }
    }
}

