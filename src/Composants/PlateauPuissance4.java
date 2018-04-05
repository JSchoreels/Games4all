package Composants;

import java.util.ArrayList;

/**
 *  Cette classe, permet de modeliser un plateau du jeu Puissance 4.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public class PlateauPuissance4 extends Plateau {

    private int p = 4;
    private int[] colonnes = {6, 6, 6, 6, 6, 6, 6};
    protected EvaluationPuissance4 moduleEval = null;

    /**
     *  Constructeur par defaut, retournant un plateau de taille 6x7.
     */
    public PlateauPuissance4() {
        super(6, 7);
    }

    /**
     *  Cette methode permet d'appliquer un coup passe en parametre sur le
     * plateau, pour le joueur dont la signature numerique estpassee en
     * parametre. Ce coup doit etre prealablement verifie pour etre applique.
     *
     * @param cp Le <b>Coup</b> a ajoute au plateau.
     * @param numeroJ L'identifiant du joueur.
     */
    @Override
    public void appliquer(Coup cp, int numeroJ) {
        int y = cp.obtenirColonne();
        cp = new Coup(colonnes[y] - 1, y);
        last = cp;
        pileCoup.add(cp);
        plateau[colonnes[y] - 1][y] = numeroJ;
        downEspaceLibre(cp.obtenirLigne(), y);
    }

    /**
     *  Cette methode verifie si le coup passe en parametre, est valide, et ce,
     * pour le joueur dont la signature numerique est passee en parametre.
     *
     * @param cp Le <b>Coup</b> a verifier.
     * @param numeroJ L'identifiant du joueur qui souhaite faire ce <b>Coup</b>.
     * @return true si le joueur peut faire ce <b>Coup</b>, false sinon.
     */
    @Override
    public boolean verification(Coup cp, int numeroJ) {
        int y = cp.obtenirColonne();
        return (dedans(cp) && (colonnes[y] > 0));
    }

    /**
     *  Cette methode retourne sous forme d'un ArrayList de Coup, les coups
     * encore jouables pour un joueur dont la signature numerique est passee en
     * parametre. Cette methode surcharge celle fournie par Plateau, etant plus
     * performante.
     *
     * @param numero Identifiant du joueur qui souhaite connaitre les
     * <b>Coup</b> disponibles pour lui.
     * @return une liste des <b>Coup</b> possibles.
     */
    @Override
    public ArrayList<Coup> coupPossible(int numero) {
        ArrayList<Coup> montableau = new ArrayList<Coup>();
        for (int j = 0; j < obtenirNombreColonnes(); j++) {
            Coup newest = new Coup(colonnes[j] - 1, j);
            if (verification(newest, numero)) {
                montableau.add(newest);
            }
        }
        return montableau;
    }

    /**
     *  Cette methode reduit l'espace libre du plateau.
     *
     * @param x Entier representant la ligne ou a ete ajoute le pion.
     * @param y Entier representant la colonne ou a ete ajoute le pion.
     */
    @Override
    protected void downEspaceLibre(int x, int y) {
        super.downEspaceLibre(x, y);
        colonnes[y]--;
    }

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
    @Override
    public int finPartie() {
        if (last == null) {
            return -2;
        }
        int x = last.obtenirLigne(), y = last.obtenirColonne();
        int horiz = 1, verti = 1, diag1 = 1, diag2 = 1;
        int color = plateau[x][y];
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int temp = 0;
                for (int k = 1; k < p; k++) {
                    int cx = x + (i * k), cy = y + (j * k);
                    if (dedans(new Coup(cx, cy)) && plateau[cx][cy] == color) {
                        temp++;
                    } else {
                        break;
                    }
                }
                if ((i == -1 || i == 1) && i == j) {
                    diag1 += temp;
                } else if ((i == -1 || i == 1) && i == (-j)) {
                    diag2 += temp;
                } else if (i == 0 && (j == -1 || j == 1)) {
                    horiz += temp;
                } else if (j == 0 && (i == -1 || i == 1)) {
                    verti += temp;
                }
            }
        }
        if (verti >= p || horiz >= p || diag1 >= p || diag2 >= p) {
            return obtenirNumJoueur(last.obtenirLigne(), last.obtenirColonne());
        }
        if (espaceLibre == 0) {
            return -1;
        }
        return -2;
    }

    /**
     *  Methode retournant si les preconditions de jeux pour pouvoir jouees
     * sont encore reunies, et ce pour le joueur dont la signature numerique est
     * passee en parametre. Dans ce jeu, cette precondition est respectee a tout
     * moment.
     *
     * @param numeroJ Identifiant du joueur dont on verifie s'il peut jouer.
     * @return true si la situation lui permet de jouer, false sinon.
     */
    @Override
    public boolean precondition(int numeroJ) {
        return true;
    }

    /**
     *  Permet de lancer l'evaluation de la partie, et ce pour un joueur dont la
     * signature numerique est passee en parametre, selon une certaine
     * aggressivite et pour un type d'evaluation particulier. Cette methode
     * verifie tout d'abord si le module d'evaluation n'est pas deja
     * existant, ce qui permet d'eviter son instanciation multiple.
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
            moduleEval = new EvaluationPuissance4();
        }
        return moduleEval.evaluation(numero, aggr, type);
    }

    /**
     *  Methode permettant de revenir en arriere dans la partie en cours.
     */
    @Override
    public void retourArriere() {
        int x = last.obtenirLigne();
        int y = last.obtenirColonne();
        plateau[x][y] = IPlateau.AUCUN_JOUEUR;
        if (pileCoup.size() > 1) {
            last = pileCoup.get(pileCoup.size() - 2);
        } else {
            last = null;
        }
        pileCoup.remove(pileCoup.size() - 1);
        espaceLibre++;
        colonnes[y]++;
    }

    /**
     *  Cette classe interne fournit au client, la possibilite d'evaluer le
     * statut de la partie. Plus cette evaluation est elevee, plus la situation
     * est favorable.
     */
    public class EvaluationPuissance4 {

        private EvaluationPuissance4() {
        }

        /**
         *
         * @param numero Entier etant la signature numerique d un joueur.
         * @param type Entier representant la generation d'evaluation (1, ou 2)
         * @param aggressivite Entier representant l'aggressivite du joueur.
         * (subjectif)
         * @return Entier evaluant l'avantage ou le desavantage pour un joueur.
         */
        public int evaluation(int numero, int aggressivite, int type) {
            if (finPartie() != -2) {
                if (finPartie() == numero) {
                    return Integer.MAX_VALUE;
                } else if (finPartie() == -1) {
                    return 0;
                } else {
                    return Integer.MIN_VALUE;
                }
            }
            int compteur = 0;
            compteur += (int) 0.5*aggressivite*contributionLigne(numero);
            compteur += (int) 0.5*aggressivite*contributionDiagonal1(numero);
            compteur += (int) 0.5*aggressivite*contributionDiagonal2(numero);
            compteur -= contributionLigne((numero+1)%2);
            compteur -= contributionDiagonal1((numero+1)%2);
            compteur -= contributionDiagonal2((numero+1)%2);
            return compteur;
        }

        private int contributionLigne(int numero){
            int compteur = 0;
            int xMax = obtenirNombreLignes();
            int yMax = obtenirNombreColonnes();
            for (int i = 0; i < xMax; i++){
                int miniCompteur = 0;
                int accu = 0;
                for (int j = 0; j < yMax; j++){
                    if (plateau[i][j] != (numero+1)%2){
                        miniCompteur += ++accu;
                        if (plateau[i][j] == numero){
                            miniCompteur += accu;
                        }
                    }
                    else{
                        if (accu < 4){
                            miniCompteur = 0;
                        }
                        compteur += miniCompteur*(i+1);
                        accu = 0;
                    }
                }
                compteur += miniCompteur*(i+1);
            }
            return compteur;
        }

        private int contributionDiagonal1(int numero){
            int compteur = 0;
            int xMax = obtenirNombreLignes();
            int yMax = obtenirNombreColonnes();
            for (int i = 0; i < xMax ; i++){
                int miniCompteur = 0;
                int accu = 0;
                for (int k = 0; k < yMax && i + k < xMax; k++){
                    if (plateau[i+k][k] != (numero+1)%2){
                        miniCompteur += ++accu;
                        if (plateau[i+k][k] == numero){
                            miniCompteur += accu*(i+k+1);
                        }
                    }
                    else{
                        if (accu < 4){
                            miniCompteur = 0;
                        }
                        compteur += miniCompteur;
                        accu = 0;
                    }
                }
                compteur += miniCompteur;
            }
            for (int j = 1; j < yMax; j++){
                int miniCompteur = 0;
                int accu = 0;
                for (int k = 0; k < xMax && j + k < yMax; k++){
                    if (plateau[k][j+k] != (numero+1)%2){
                        miniCompteur += ++accu;
                        if (plateau[k][j+k] == numero){
                            miniCompteur += accu*(k+1);
                        }
                    }
                    else{
                        if (accu < 4){
                            miniCompteur = 0;
                        }
                        compteur += miniCompteur;
                        accu = 0;
                    }
                }
                compteur += miniCompteur;
            }
            return compteur;
        }

        private int contributionDiagonal2(int numero){
            int compteur = 0;
            int xMax = obtenirNombreLignes();
            int yMax = obtenirNombreColonnes();
            for (int j = 0; j < yMax; j++){
                int miniCompteur = 0;
                int accu = 0;
                for (int k = 0; k < xMax && j - k > 0; k++){
                    if (plateau[k][j-k] != (numero+1)%2){
                        miniCompteur += ++accu;
                        if (plateau[k][j-k] == numero){
                            miniCompteur += accu*(k+1);
                        }
                    }
                    else{
                        if (accu < 4){
                            miniCompteur = 0;
                        }
                        compteur += miniCompteur;
                        accu = 0;
                    }
                }
                compteur += miniCompteur;
            }
            for (int i = 1; i < xMax; i++){
                int miniCompteur = 0;
                int accu = 0;
                for (int k = 0; k < yMax && i + k < xMax; k++){
                    if (plateau[i+k][yMax-1-k] != (numero+1)%2){
                        miniCompteur += ++accu;
                        if (plateau[i+k][yMax-1-k] == numero){
                            miniCompteur += accu*(i+k+1);
                        }
                    }
                    else{
                        if (accu < 4){
                            miniCompteur = 0;
                        }
                        compteur += miniCompteur;
                        accu = 0;
                    }
                }
                compteur += miniCompteur;
            }
            return compteur;
        }
    }
}

