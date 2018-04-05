package Composants;

/**
 *  Cette classe permet de modeliser un <b>Plateau</b> du jeu du Morpion.
 * Celle-ci herite de la classe abstraite <b>Plateau</b>.
 * 
 * @author Jonathan Schoreels & Alexandre Devaux
 */
public class PlateauMorpion extends Plateau {

    protected EvaluationMorpion moduleEval = null;

    /**
     *  Constructeur qui prend en parametre un seul entier, representant le
     * nombre de lignes ET des colonnes de la grille creee, le morpion se jouant
     * sur une grille carre.
     *
     * @param n Entier designant la taille du plateau.
     */
    public PlateauMorpion(int n) {
        super(n, n);
    }

    /**
     *  Methode qui permet d'appliquer un coup passe en parametre, pour le
     * joueur dont la signature numerique a ete passee en parametre, sur la
     * plateau. Attention, ceci applique le coup, sans verifier si il est bon.
     * La verification se fait dans une autre methode.
     *
     * @param cp Le <b>Coup</b> a ajoute au plateau.
     * @param numeroJ L'identifiant du joueur.
     */
    @Override
    public void appliquer(Coup cp, int numeroJ) {
        downEspaceLibre(cp.obtenirLigne(), cp.obtenirColonne());
        last = cp;
        pileCoup.add(cp);
        plateau[cp.obtenirLigne()][cp.obtenirColonne()] = numeroJ;
    }

    /**
     * Methode retournant un entier selon l'avance du jeu se deroulant sur ce
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
        int xMax = obtenirNombreLignes(), yMax = obtenirNombreColonnes();
        int n = plateau.length;
        boolean condHoriz = true;
        boolean condVerti = true;
        boolean condDiago1 = true;
        boolean condDiago2 = true;
        for (int i = 0; (i < xMax - 1) && (condHoriz || condVerti); i++) {
            condVerti = condVerti && (plateau[i][y] == plateau[i + 1][y]);
            condHoriz = condHoriz && (plateau[x][i] == plateau[x][i + 1]);
        }
        if (condHoriz || condVerti) {
            return obtenirNumJoueur(x, y);
        }

        if (x == y) {
            for (int i = 0; (i < yMax - 1) && condDiago1; i++) {
                condDiago1 = condDiago1 && (plateau[i][i]
                        == plateau[i + 1][i + 1]);
            }
            if (condDiago1) {
                return obtenirNumJoueur(x, y);
            }
        }

        if (x + y == (xMax - 1)) {
            for (int i = 0; (i < yMax - 1) && condDiago2; i++) {
                condDiago2 = condDiago2
                        && (plateau[i][n - i - 1] == plateau[i + 1][n - i - 2]);
            }
            if (condDiago2) {
                return obtenirNumJoueur(x, y);
            }
        }
        if (espaceLibre == 0) {
            return -1;
        }
        return -2;
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
    @Override
    public boolean precondition(int numeroJ) {
        return true;
    }

    /**
     *  Methode retournant vrai si le coup entre en parametre, pour le joueur
     * dont la signature numerique est passee en parametre, est valable, false
     * sinon.
     *
     * @param cp Le <b>Coup</b> a verifier.
     * @param numeroJ L'identifiant du joueur qui souhaite faire ce <b>Coup</b>.
     * @return true si le joueur peut faire ce <b>Coup</b>, false sinon.
     */
    @Override
    public boolean verification(Coup cp, int numeroJ) {
        int x = cp.obtenirLigne(), y = cp.obtenirColonne();
        if (!dedans(cp) || plateau[x][y] != AUCUN_JOUEUR) {
            return false;
        }
        return true;
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
            moduleEval = new EvaluationMorpion();
        }
        return moduleEval.evaluation(numero, aggr, type);
    }

    /**
     * Methode permettant de revenir en arriere dans la partie en cours.
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
    }

    /**
     *  Cette classe interne fournit au client, la possibilite d'evaluer le
     * statut de la partie. Plus cette evaluation est elevee, plus la situation
     * est favorable.
     */
    public class EvaluationMorpion {

        private EvaluationMorpion() {
        }

        /**
         *  Cette methode evalue la partie selon les criteres suivants :
         * Si la partie est :  - gagnee, l'evaluation retourne Integer.MAX_VALUE
         * - perdue, l'evaluation retourne Integer.MIN_VALUE
         * - nulle, l'evaluation retourne 0
         * Quand la partie est en cours, l'evaluation est la somme des scores de
         * chaque alignement present sur la grille.
         * Pour une rangee de X pions, l'evaluation est incrementee de (X+1)*X/2
         * X est positif si la rangee a est au joueur qui demande l'evaluation,
         * negatif si la rangee est au joueur ennemi a celui ci.
         * Si la rangee n'es pas constituee que de pions d'une meme couleur,
         * elle vaut un score nul.
         *
         * @param numero Entier etant la signature numerique d un joueur.
         * @param type Entier representant la generation d'evaluation (1, ou 2)
         * @param aggressivite Entier representant l'aggressivite du joueur.
         * (subjectif)
         * @return Entier evaluant l'avantage ou le desavantage pour un joueur.
         */
        public int evaluation(int numero, int aggressivite, int type) {
            int attaque = 0;
            int defense = 0;
            if (aggressivite > 0) {
                attaque = aggressivite;
            }
            int xMax = obtenirNombreLignes();
            int yMax = obtenirNombreColonnes();
            int compteur = 0;
            if (finPartie() != -2) {
                if (finPartie() == numero) {
                    return Integer.MAX_VALUE;
                } else if (finPartie() == -1) {
                    return 0;
                } else {
                    return Integer.MIN_VALUE;
                }
            } else {
                int accu = 0;
                int miniCompteur = 0;
                for (int i = 0; i < xMax; i++) {
                    accu = 0;
                    miniCompteur = 0;
                    for (int k = 0; k < yMax; k++) {
                        if (obtenirNumJoueur(i, k) == numero) {
                            if (accu < 0) {
                                miniCompteur = 0;
                                break;
                            } else {
                                accu += (1 + attaque);
                                miniCompteur += accu;
                            }
                        } else if (obtenirNumJoueur(i, k) != AUCUN_JOUEUR) {
                            if (accu > 0) {
                                miniCompteur = 0;
                                break;
                            } else {
                                accu -= (1 + defense);
                                miniCompteur += accu;
                            }
                        }
                    }
                    compteur += miniCompteur;
                }
                for (int j = 0; j < yMax; j++) {
                    accu = 0;
                    miniCompteur = 0;
                    for (int k = 0; k < yMax; k++) {
                        if (obtenirNumJoueur(k, j) == numero) {
                            if (accu < 0) {
                                miniCompteur = 0;
                                break;
                            } else {
                                accu += (1 + attaque);
                                miniCompteur += accu;
                            }
                        } else if (obtenirNumJoueur(k, j) != AUCUN_JOUEUR) {
                            if (accu > 0) {
                                miniCompteur = 0;
                                break;
                            } else {
                                accu -= (1 + defense);
                                miniCompteur += accu;
                            }
                        }
                    }
                    compteur += miniCompteur;
                }
                accu = 0;
                miniCompteur = 0;
                for (int i = 0; i < xMax; i++) {
                    if (obtenirNumJoueur(i, i) == numero) {
                        if (accu < 0) {
                            miniCompteur = 0;
                            break;
                        } else {
                            accu += (1 + attaque);
                            miniCompteur += accu;
                        }
                    } else if (obtenirNumJoueur(i, i) != AUCUN_JOUEUR) {
                        if (accu > 0) {
                            miniCompteur = 0;
                            break;
                        } else {
                            accu -= (1 + defense);
                            miniCompteur += accu;
                        }
                    }
                }
                compteur += miniCompteur;
                accu = 0;
                miniCompteur = 0;
                for (int i = 0; i < yMax; i++) {
                    if (obtenirNumJoueur(i, xMax - i - 1) == numero) {
                        if (accu < 0) {
                            miniCompteur = 0;
                            break;
                        } else {
                            accu += (1 + attaque);
                            miniCompteur += accu;
                        }
                    } else if (obtenirNumJoueur(i, xMax - i - 1)
                            != AUCUN_JOUEUR) {
                        if (accu > 0) {
                            miniCompteur = 0;
                            break;
                        } else {
                            accu -= (1 + defense);
                            miniCompteur += accu;
                        }
                    }
                }
                compteur += miniCompteur;
            }
            return compteur;
        }
    }
}
