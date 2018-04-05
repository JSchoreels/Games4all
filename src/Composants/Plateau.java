package Composants;

import java.util.ArrayList;

/**
 *  Classe abstraite Plateau implemente <b>IPlateau</b>. Celle-ci fournit a tous
 * les plateaux de jeux, une base solide de methodes diverses, leur permettant
 * d'appeler celles ci, et simplifiant leur implementations.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @see PlateauMorpion
 * @see PlateauPuissance4
 * @see PlateauOthello
 */
public abstract class Plateau implements IPlateau {

    protected int[][] plateau;
    protected int espaceLibre;
    protected Coup last;
    protected ArrayList<Coup> pileCoup = new ArrayList<Coup>();

    /**
     *  Constructeur de plateau, prenant en parametre le nombre de lignes et de
     * colonnes que l'on souhaite sur la grille.
     *
     * @param lignes Entier donnant le nombre de ligne du plateau(>=0)
     * @param colonnes Entier donnant le nombre de colonne du plateau(>=0)
     */
    public Plateau(int lignes, int colonnes) {
        plateau = new int[lignes][colonnes];
        initialisation();
    }

    /**
     *  Methode d'initialisation, qui permet de changer les valeurs par defauts
     * du type primitif int, en AUCUN_JOUEUR, et qui permet d'initialiser le
     * nombre de cases encore libres.
     */
    protected void initialisation() {
        int l = obtenirNombreLignes(), c = obtenirNombreColonnes();
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                plateau[i][j] = AUCUN_JOUEUR;
            }
        }
        espaceLibre = l * c;
    }

    /**
     *  Methode retournant le nombre de lignes du plateau.
     *
     * @return Le nombre de lignes du plateau.
     */
    @Override
    public int obtenirNombreLignes() {
        return plateau.length;
    }

    /**
     *  Methode retournant le nombre de colonnes du plateau.
     *
     * @return Le nombre de colonnes du plateau.
     */
    @Override
    public int obtenirNombreColonnes() {
        return plateau[0].length;
    }

    /**
     *  Methode retournant le dernier coup joue.
     *
     * @return Le dernier <b>Coup</b> qui a ete joue sur le plateau.
     */
    @Override
    public Coup obtenirDernierCoup() {
        return last;
    }

    /**
     *  Methode retournant la signature numerique du joueur possedant la case
     * se trouvant a une ligne et une colonne precise. Retourne AUCUN_JOUEUR si
     * la case est libre.
     *
     * @param ligne La ligne (>=0)
     * @param colonne La Colonne (>=0)
     * @return Le numero du joueur "possedant" la case, ou la constante
     * AUCUN_JOUEUR si cette case est libre.
     */
    @Override
    public int obtenirNumJoueur(int ligne, int colonne) {
        return plateau[ligne][colonne];
    }

    /**
     *  Methode qui retourne le tableau a double dimensions d'entiers
     * represetant le plateau.
     *
     * @return Fournit un tableau d'entiers, representant l'etat de la partie.
     */
    public int[][] getPlateau() {
        return plateau;
    }

    /**
     *  Cette methode prend en plus un x et un y, ce qui permet de garder une
     * trace de quel case a fait declencher ce processus. Ce qui permet par
     * exemple de surcharger cette methode pour ensuite lui rajouter des
     * comportements qui dependent de x ou de y.
     * (par exemple dans le puissance 4, qui a besoin de savoir quelle colonne.)
     *
     * @param x Entier representant la ligne ou a ete ajoute le pion.
     * @param y Entier representant la colonne ou a ete ajoute le pion.
     */
    protected void downEspaceLibre(int x, int y) {
        espaceLibre--;
    }

    /**
     *  Methode qui retourne le nombre de cases encore disponible.
     *
     * @return Entier representant les cases du plateau ou il n'y a pas de pion.
     */
    public int getEspaceLibre() {
        return espaceLibre;
    }

    /**
     *  Methode qui verifie si un coup est bel et bien dans les marges du
     * tableau retourne true si oui, false sinon.
     *
     * @param cp Un <b>Coup</b> a tester.
     * @return true si le <b>Coup</b> fournit en parametre est dans le plateau,
     * false sinon.
     */
    protected boolean dedans(Coup cp) {
        int x = cp.obtenirLigne(), y = cp.obtenirColonne();
        return (0 <= x && x < obtenirNombreLignes()
                && 0 <= y && y < obtenirNombreColonnes());
    }

    /**
     *  Methode qui permet d'importer dans le plateau present, la situation d'un
     * IPlateau fournit en parametre, et ce, dans l'optique de servir le joueur
     * possedant la meme signature numerique dans celui-ci, que celle fournie en
     * parametre de la methode.
     *
     * @param iPlateau Le plateau a copier.
     * @param numero L'identifiant du joueur qui fait appel a cette methode.
     */
    public void importSituation(IPlateau iPlateau, int numero) {
        int xMax = iPlateau.obtenirNombreLignes();
        int yMax = iPlateau.obtenirNombreColonnes();
        this.plateau = new int[xMax][yMax];
        this.last = iPlateau.obtenirDernierCoup();
        this.espaceLibre = xMax * yMax;
        for (int i = 0; i < xMax; i++) {
            for (int j = 0; j < yMax; j++) {
                int current = iPlateau.obtenirNumJoueur(i, j);
                if (current == numero) {
                    plateau[i][j] = numero%2;
                    downEspaceLibre(i, j);
                } else if (current == IPlateau.AUCUN_JOUEUR) {
                    plateau[i][j] = IPlateau.AUCUN_JOUEUR;
                } else {
                    plateau[i][j] = (numero + 1) % 2;
                    downEspaceLibre(i, j);
                }
            }
        }
    }

    /**
     *  Methode qui retourne un ArrayList de Coup, comprenant tous les coups
     * encore jouables par le joueur ayant sur le plateau, la signature
     * numerique fournie en parametre de la methode.
     *
     * @param numero Identifiant du joueur qui souhaite connaitre les
     * <b>Coup</b> disponibles pour lui.
     * @return une liste des <b>Coup</b> possibles.
     */
    public ArrayList<Coup> coupPossible(int numero) {
        ArrayList<Coup> montableau = new ArrayList<Coup>();
        for (int i = 0; i < obtenirNombreLignes(); i++) {
            for (int j = 0; j < obtenirNombreColonnes(); j++) {
                Coup newest = new Coup(i, j);
                if (verification(newest, numero)) {
                    montableau.add(newest);
                }
            }
        }
        return montableau;
    }

    /**
     *  Methode retournant si les preconditions de jeux pour pouvoir jouees
     * sont encore reunies, et ce pour le joueur dont la signature numerique est
     * passee en parametre. Dans ce jeu, cette precondition est toujours
     * respectee, tant que la partie est non gagnee, ou non pleine.
     *
     * @param numeroJ Identifiant du joueur dont on verifie s'il peut jouer.
     * @return true si la situation lui permet de jouer, false sinon.
     */
    public abstract boolean precondition(int numeroJ);

    /**
     *  Methode retournant vrai si le coup entre en parametre, pour le joueur
     * dont la signature numerique est passee en parametre, est valable, false
     * sinon.
     *
     * @param cp Le <b>Coup</b> a verifier.
     * @param numeroJ L'identifiant du joueur qui souhaite faire ce <b>Coup</b>.
     * @return true si le joueur peut faire ce <b>Coup</b>, false sinon.
     */
    public abstract boolean verification(Coup cp, int numeroJ);

    /**
     *  Methode qui permet d'appliquer un coup passe en parametre, pour le
     * joueur dont la signature numerique a ete passee en parametre, sur la
     * plateau. Attention, ceci applique le coup, sans verifier si il est bon.
     * La verification se fait dans une autre methode.
     *
     * @param cp Le <b>Coup</b> a ajoute au plateau.
     * @param numeroJ L'identifiant du joueur.
     */
    public abstract void appliquer(Coup cp, int numeroJ);

    /**
     *
     * @param numero Entier etant la signature numerique d un joueur.
     * @param type Entier representant la generation d'evaluation (1, ou 2)
     * @param agg Entier representant l'aggressivite du joueur. (subjectif)
     * @return Entier evaluant l'avantage ou le desavantage pour un joueur.
     */
    abstract int runEvaluation(int numero, int aggressivite, int type);

    /**
     *  Methode retournant un entier selon l'avance du jeu se deroulant sur ce
     * plateau.
     * Les valeurs de retours possibles sont :
     * - -2 : si la partie est toujours en cours.
     * - -1 : si la partie est finie, et ce par un match nul.
     * - N : si le joueur dont la signature numerique est N a gagne la partie.
     *
     * @return Entier representant l'etat de la partie.
     */
    public abstract int finPartie();

    /**
     *  Methode permettant de revenir en arriere dans la partie en cours.
     */
    public abstract void retourArriere();
}

