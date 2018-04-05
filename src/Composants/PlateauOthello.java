package Composants;

/**
 *  Cette classe, permet de modeliser un plateau du jeu d'Othello.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public class PlateauOthello extends Plateau implements IScore {

    private int passe = 0, nbJ = 2;
    protected EvaluationOthello moduleEval = null;

    /**
     *  Constructeur qui prend en parametre le nombre de lignes du plateau
     * carre, et initialise la partie.
     *
     * @param n Entier designant la taille du plateau.
     */
    public PlateauOthello(int n) {
        super(n, n);
        initialisation(n);
    }

    /**
     * Cette methode permet d'appliquer un coup (juge valide), sur le plateau,
     * et renverse les pions adverses des ranges qui comportent la position du
     * coup selon les regles du jeu de l'Othello.
     *
     * @param cp Le <b>Coup</b> a ajoute au plateau.
     * @param numJ L'identifiant du joueur.
     */
    @Override
    public void appliquer(Coup cp, int numJ) {
        int x = cp.obtenirLigne();
        int y = cp.obtenirColonne();
        plateau[x][y] = numJ;
        downEspaceLibre(x, y);
        last = cp;
        boolean[] test = isPossibleAll(cp, numJ);
        pileCoup.add(null);
        if (test[0]) {
            for (int i = 1; plateau[x - i][y - i] != numJ; i++) {
                plateau[x - i][y - i] = numJ;
                pileCoup.add(new Coup(x - i, y - i));
            }
        }
        if (test[1]) {
            for (int i = 1; plateau[x - i][y] != numJ; i++) {
                plateau[x - i][y] = numJ;
                pileCoup.add(new Coup(x - i, y));
            }
        }
        if (test[2]) {
            for (int i = 1; plateau[x - i][y + i] != numJ; i++) {
                plateau[x - i][y + i] = numJ;
                pileCoup.add(new Coup(x - i, y + i));
            }
        }
        if (test[3]) {
            for (int i = 1; plateau[x][y - i] != numJ; i++) {
                plateau[x][y - i] = numJ;
                pileCoup.add(new Coup(x, y - i));
            }
        }
        if (test[5]) {
            for (int i = 1; plateau[x][y + i] != numJ; i++) {
                plateau[x][y + i] = numJ;
                pileCoup.add(new Coup(x, y + i));
            }
        }
        if (test[6]) {
            for (int i = 1; plateau[x + i][y - i] != numJ; i++) {
                plateau[x + i][y - i] = numJ;
                pileCoup.add(new Coup(x + i, y - i));
            }
        }
        if (test[7]) {
            for (int i = 1; plateau[x + i][y] != numJ; i++) {
                plateau[x + i][y] = numJ;
                pileCoup.add(new Coup(x + i, y));
            }
        }
        if (test[8]) {
            for (int i = 1; plateau[x + i][y + i] != numJ; i++) {
                plateau[x + i][y + i] = numJ;
                pileCoup.add(new Coup(x + i, y + i));
            }
        }
        pileCoup.add(cp);
    }

    /**
     *  Cette methode initialise la partie, permettant le positionement des 4
     * pions initiaux, selon le principe du jeu de l'Othello.
     *
     * @param n Entier representant la taille de la grille.
     */
    private void initialisation(int n) {
        plateau[n / 2 - 1][n / 2 - 1] = 1;
        plateau[n / 2 - 1][n / 2] = 0;
        plateau[n / 2][n / 2] = 1;
        plateau[n / 2][n / 2 - 1] = 0;
        espaceLibre = espaceLibre - 4;
    }

    /**
     *  Cette methode retourne un tableau de boolean, representant si il est oui
     * ou non possible de retourner des pions adverses dans les 8 directions qui
     * etendent la position du coup joue, et ce, pour le joueur dont la
     * signature numerique est passee en parametre.
     *
     * @param cp Le <b>Coup</b> dont il faut verifier s'il est valable.
     * @param numJ Identifiant du joueur souhaitant appliquer ce <b>Coup</b>.
     * @return un tableau de boolean, chaque element du tableau represente une
     * direction autour du pion que l'on veut place au <b>Coup</b> cp. Si les
     * conditions sont verifies pour une direction, alors true, si ce n'est pas
     * le cas, false.
     */
    private boolean[] isPossibleAll(Coup cp, int numJ) {
        int x = cp.obtenirLigne(), y = cp.obtenirColonne();
        boolean[] possibility = new boolean[9];
        int compteur = 0, boucle = 1;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                possibility[compteur] = false;
                int entI = x + (i * boucle);
                int entJ = y + (j * boucle);
                Coup ent = new Coup(entI, entJ);
                boucle++;
                int extI = x + (i * boucle);
                int extJ = y + (j * boucle);
                Coup ext = new Coup(extI, extJ);
                while (dedans(ext) && (plateau[entI][entJ] == (numJ + 1) % 2)) {
                    if (plateau[extI][extJ] == numJ) {
                        possibility[compteur] = true;
                        break;
                    }
                    entI = ext.obtenirLigne();
                    entJ = ext.obtenirColonne();
                    ent = new Coup(entI, entJ);
                    boucle++;
                    extI = x + (i * boucle);
                    extJ = y + (j * boucle);
                    ext = new Coup(extI, extJ);
                }
                compteur++;
                boucle = 1;
            }
        }
        return possibility;
    }

    /**
     *  Cette methode verifie si le coup passe en parametre, est valide, et ce,
     * pour le joueur dont la signature numerique est passee en parametre.
     *
     * @param cp Le <b>Coup</b> dont il faut verifier s'il est valable.
     * @param numJ Identifiant du joueur souhaitant appliquer ce <b>Coup</b>.
     * @return true s'il existe une direction autour de ce pion qui verifie les
     * conditions de l'othello, false sinon.
     */
    @Override
    public boolean verification(Coup cp, int numJ) {
        int x = cp.obtenirLigne(), y = cp.obtenirColonne();
        return dedans(cp) && (plateau[x][y] == AUCUN_JOUEUR)
                && isPossible(x, y, numJ);
    }

    /**
     *  Methode retournant un entier selon l'avance du jeu se deroulant sur ce
     * plateau. Les valeurs de retours possibles sont :
     * - -2 : si la partie est toujours en cours.
     * - -1 : si la partie est finie, et ce par un match nul.
     * - N : si le joueur dont la signature numerique est N a gagne la partie.
     */
    @Override
    public int finPartie() {
        if (last == null) {
            return -2;
        }
        if (espaceLibre == 0 || passe == nbJ) {
            int[] score = new int[nbJ];
            for (int i = 0; i < obtenirNombreLignes(); i++) {
                for (int j = 0; j < obtenirNombreColonnes(); j++) {
                    int index = plateau[i][j];
                    if (index != AUCUN_JOUEUR) {
                        score[index % 2]++;
                    }
                }
            }
            int temp = -1, draw = 0, max = 0;
            for (int i = 0; i < nbJ; i++) {
                if (score[i] > temp) {
                    temp = score[i];
                    max = i;
                    draw = 0;
                } else if (score[i] == temp) {
                    draw++;
                }
            }
            if (draw > 0) {
                return -1;
            }
            return max;
        }
        return -2;
    }

    /**
     *  Methode retournant si les preconditions de jeux pour pouvoir jouees
     * sont encore reunies, et ce pour le joueur dont la signature numerique est
     * passee en parametre. Dans ce jeu, cette precondition est respectee si et
     * seulement si ce joueur peut retourner des pions adverses en jouant un
     * coup.
     *
     * @param numJ Identifiant du joueur dont on verifie s'il peut jouer.
     * @return true si la situation lui permet de jouer, false sinon.
     */
    @Override
    public boolean precondition(int numJ) {
        for (int i = 0; i < obtenirNombreLignes(); i++) {
            for (int j = 0; j < obtenirNombreColonnes(); j++) {
                if (plateau[i][j] == AUCUN_JOUEUR && isPossible(i, j, numJ)) {
                    passe = 0;
                    return true;
                }
            }
        }
        passe++;
        pileCoup.add(null);
        return false;
    }

    /**
     *  Implementation de l'interface IScore, cette methode retourne le nombre
     * de pions que le joueur dont la sign. num. est passee en parametre.
     *
     * @param numJ L'identifiant du joueur, dont on veut connaitre le score.
     * @return Un entier representant le score du joueur dont on a fournit
     * l'identifiant.
     */
    @Override
    public int obtenirScore(int numJ) {
        int compteur = 0;
        for (int i = 0; i < obtenirNombreLignes(); i++) {
            for (int j = 0; j < obtenirNombreColonnes(); j++) {
                if (plateau[i][j] == numJ) {
                    compteur += 1;
                }
            }
        }
        return compteur;
    }

    /**
     *  Methode qui retourne vrai si il est possible de jouer pour le joueur
     * dont l'entier associe est passe en parametre, a l'emplacement du coup
     * passe en parametre, et ce, sans tester toutes les possibilites.
     *
     * @param x L'index de ligne ou le joueur veut ajouter son pion.
     * @param y L'index de colonne ou le joueur veut ajouter son pion.
     * @param numJ Identifiant du joueur souhaitant ajouter son pion en x, y.
     * @return true s'il existe une direction autour de ce pion qui verifie les
     * conditions de l'othello, false sinon.
     */
    private boolean isPossible(int x, int y, int numJ) {
        int boucle = 1;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int entI = x + (i * boucle);
                int entJ = y + (j * boucle);
                Coup ent = new Coup(entI, entJ);
                boucle++;
                int extI = x + (i * boucle);
                int extJ = y + (j * boucle);
                Coup ext = new Coup(extI, extJ);
                while (dedans(ext) && (plateau[entI][entJ] == (numJ + 1) % 2)) {
                    if (plateau[extI][extJ] == numJ) {
                        return true;
                    }
                    entI = ext.obtenirLigne();
                    entJ = ext.obtenirColonne();
                    ent = new Coup(entI, entJ);
                    boucle++;
                    extI = x + (i * boucle);
                    extJ = y + (j * boucle);
                    ext = new Coup(extI, extJ);
                }
                boucle = 1;
            }
        }
        return false;
    }

    /**
     *  Permet de lancer l'evaluation de la partie, et ce pour un joueur dont la
     * signature numerique est passee en parametre, selon une certaine
     * aggressivite et pour un type d'evaluation particulier.
     *
     *  Cette methode verifie tout d'abord si le module d'evaluation n'est pas
     * deja existant, ce qui permet d'eviter son instanciation multiple.
     * (Principe du design pattern singleton).
     *
     * @param numero Entier etant la signature numerique d un joueur.
     * @param type Entier representant la generation d'evaluation (1, ou 2)
     * @param aggr Entier representant l'aggressivite du joueur. (subjectif)
     * @return Entier evaluant l'avantage ou le desavantage pour un joueur.
     */
    @Override
    public int runEvaluation(int numero, int aggr, int type) {
        if (moduleEval == null) {
            moduleEval = new EvaluationOthello();
        }
        return moduleEval.evaluation(numero, aggr, type);
    }

    /**
     *  Methode permettant de revenir en arriere dans la partie en cours.
     */
    @Override
    public void retourArriere() {
        if (pileCoup.get(pileCoup.size() - 1) == null) {
            pileCoup.remove(pileCoup.size() - 1);
            passe--;
            last = pileCoup.get(pileCoup.size() - 1);
        }
        else{
            int x = pileCoup.get(pileCoup.size() - 1).obtenirLigne();
            int y = pileCoup.get(pileCoup.size() - 1).obtenirColonne();
            int numJ = plateau[x][y];
            plateau[x][y] = AUCUN_JOUEUR;
            pileCoup.remove(pileCoup.size() - 1);
            espaceLibre++;
            while (pileCoup.get(pileCoup.size() - 1) != null) {
                x = pileCoup.get(pileCoup.size() - 1).obtenirLigne();
                y = pileCoup.get(pileCoup.size() - 1).obtenirColonne();
                plateau[x][y] = (numJ + 1) % 2;
                pileCoup.remove(pileCoup.size() - 1);
            }
            pileCoup.remove(pileCoup.size() - 1);
            if (pileCoup.size() > 0) {
                last = pileCoup.get(pileCoup.size() - 1);
            } else {
                last = null;
            }
            moduleEval = null;
            passe = 0;
        }
    }

    /**
     *  Cette classe interne fournit au client, la possibilite d'evaluer le
     * statut de la partie. Plus cette evaluation est elevee, plus la situation
     * est favorable.
     */
    public class EvaluationOthello {

        private int[][] evalMoi = {{100, -10, 11, 6, 6, 11, -10, 100},
            {-10, -20, 1, 2, 2, 1, -20, -10},
            {10, 1, 5, 4, 4, 5, 1, 10},
            {6, 2, 4, 8, 8, 4, 2, 6},
            {6, 2, 4, 8, 8, 4, 2, 6},
            {10, 1, 5, 4, 4, 5, 1, 10},
            {-10, -20, 1, 2, 2, 1, -20, -10},
            {100, -10, 11, 6, 6, 11, -10, 100}};
        private int[][] evalEnnemi = {{100, -10, 11, 6, 6, 11, -10, 100},
            {-10, -20, 1, 2, 2, 1, -20, -10},
            {10, 1, 5, 4, 4, 5, 1, 10},
            {6, 2, 4, 8, 8, 4, 2, 6},
            {6, 2, 4, 8, 8, 4, 2, 6},
            {10, 1, 5, 4, 4, 5, 1, 10},
            {-10, -20, 1, 2, 2, 1, -20, -10},
            {100, -10, 11, 6, 6, 11, -10, 100}};

        private EvaluationOthello() {
        }

        /**
         *  Cette methode permet de retourner l'estimation de la situation du
         * jeu selon un entier, eleve si la partie est favorable au joueur qu'on
         * desire evaluer, bas voir negatif sinon.
         *
         * @param numero Entier etant la signature numerique d un joueur.
         * @param type Entier representant la generation d'evaluation (1, ou 2)
         * @param agg Entier representant l'aggressivite du joueur. (subjectif)
         * @return Entier evaluant l'avantage ou le desavantage pour un joueur.
         */
        public int evaluation(int numero, int agg, int type) {
            int compteur = 0;
            if (espaceLibre < 4 && (type == 2)) {
                for (int i = 0; i < obtenirNombreLignes(); i++) {
                    for (int j = 0; j < obtenirNombreColonnes(); j++) {
                        if (plateau[i][j] == numero) {
                            compteur += 1;
                        }
                    }
                }
                return compteur;
            }
            if (type == 2) {
                compteur -= 3 * agg * coupPossible((numero + 1) % 2).size();
                compteur += 3 * agg * coupPossible(numero).size();
            }
            if (espaceLibre >= 5 || (type == 1)) {
                switch (type) {
                    default:
                        ;
                    case 1:
                        compteur += evaluationType1(numero, agg);
                        break;
                    case 2:
                        compteur += evaluationType2(numero, agg);
                        break;
                }
            }
            return compteur;
        }

        /**
         *  Evaluation de generation 1. celle ci ne fait que compter les pions
         * aux deux joueurs, et retourne un resultat selon.
         *
         * @param numero Entier representant la signature numerique d'un joueur
         * @param aggressivite Entier representant l'aggressivite d'un joueur.
         * @return Entier, l'evaluation du plateau pour un joueur.
         */
        private int evaluationType1(int numero, int aggressivite) {
            int attaque = 0;
            int defense = 0;
            if (aggressivite > 0) {
                attaque = aggressivite;
            } else {
                defense = aggressivite;
            }
            int compteur = 0;
            int xMax = obtenirNombreLignes();
            int yMax = obtenirNombreColonnes();
            for (int i = 0; i < xMax; i++) {
                for (int j = 0; j < yMax; j++) {
                    if (plateau[i][j] == numero) {
                        compteur += 1;
                        if (((i == 0) || (i == xMax - 1))
                                && ((j == 0) || (j == yMax - 1))) {
                            compteur += (5 + attaque);
                        } else if ((i == 0) || (j == 0)
                                || (i == xMax - 1) || (j == yMax - 1)) {
                            compteur += 1;
                        }
                    } else if (plateau[i][j] != AUCUN_JOUEUR) {
                        compteur -= 1;
                        if (((i == 0) || (i == xMax - 1))
                                && ((j == 0) || (j == yMax - 1))) {
                            compteur -= (5 + defense);
                        } else if ((i == 0) || (j == 0)
                                || (i == xMax - 1) || (j == yMax - 1)) {
                            compteur -= 1;
                        }
                    }
                }
            }
            return compteur;
        }

        /**
         *  Cette methode fait une evaluation de generation 2. Elle compte non
         * seulement les pions, et les coins, mais en plus prend en compte le
         * concept de "pions defintifs", en effet, lorsqu'un coin est obtenu,
         * les cases adjacentes deviennent strategiquement interessante.
         * Cette evaluation prend en compte cela.
         *
         * @param numero Entier representant la signature numerique d'un joueur
         * @param aggressivite Entier representant l'aggressivite d'un joueur.
         * @return Entier, l'evaluation du plateau pour un joueur.
         */
        private int evaluationType2(int numero, int aggressivite) {
            int compteur = 0;
            int xMax = obtenirNombreLignes();
            int yMax = obtenirNombreColonnes();
            majGrille(evalMoi, numero);
            majGrille(evalEnnemi, (numero + 1) % 2);
            for (int i = 0; i < xMax; i++) {
                for (int j = 0; j < yMax; j++) {
                    if (plateau[i][j] == numero) {
                        compteur += evalMoi[i][j];
                    } else if (plateau[i][j] != AUCUN_JOUEUR) {
                        compteur -= evalEnnemi[i][j];
                    }
                }
            }
            return compteur;
        }

        /**
         *  Methode permettant de savoir si une case est dite definitve ou pas.
         *
         * @param i Entier representant la ligne i.
         * @param j Entier, representant la ligne j.
         * @param numJ Entier, etant la signature numerique d'un des joueurs.
         * @param grilleEval, evalMoi ou evalEnnemi. Represente lequel des
         * deux joueurs on s'interesse.
         * @return true si la case ne peut plus etre retournee, false sinon.
         */
        private boolean estCaseDefinitive(int i, int j, int numJ,
                int[][] grilleEval) {
            if (!dedans(new Coup(i, j))) {
                return true;
            } else if ((grilleEval[i][j] == grilleEval[0][0])
                    && (plateau[i][j] == numJ)) {
                return true;
            } else {
                int compteur = 0;
                for (int x = i - 1; (x < i + 2) && (compteur <= 3); x++) {
                    for (int y = j - 1; (y < j + 2) && (compteur <= 3); y++) {
                        if (x != i || y != j) {
                            if (dedans(new Coup(x, y))) {
                                if (grilleEval[x][y] == grilleEval[0][0]) {
                                    if (plateau[x][y] == numJ) {
                                        compteur += 1;
                                    }
                                }
                            } else {
                                compteur += 1;
                            }
                        }
                    }
                }
                if (compteur >= 4) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        /**
         *  Methode mettant a jour la grille d'evaluation, selon la position des
         * 2 joueurs sur le plateau.
         *
         * @param numJ Entier, etant la signature numerique d'un des joueurs.
         * @param grilleEval, evalMoi ou evalEnnemi. Represente lequel des
         * deux joueurs on s'interesse.
         */
        private void majGrille(int[][] grilleEval, int numJ) {
            for (int i = 0; i < grilleEval.length; i++) {
                for (int j = 0; j < grilleEval.length; j++) {
                    if (estCaseDefinitive(i, j, numJ, grilleEval)) {
                        grilleEval[i][j] = 100;
                    }
                }
            }
        }
    }
}

