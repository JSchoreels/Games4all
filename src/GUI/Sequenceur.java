package GUI;

import Composants.Coup;
import Composants.IScore;
import Composants.Joueur;
import Composants.ListeJeux;
import Composants.Log;
import Composants.Plateau;
import Composants.PlateauMorpion;
import Composants.PlateauOthello;
import Composants.PlateauPuissance4;
import GUI.Affichage.Affichage;
import GUI.Affichage.Affichage_Morpion;
import GUI.Affichage.Affichage_Othello;
import GUI.Affichage.Affichage_Puissance4;
import GUI.Etat.Etat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *  Classe permettant de lancer une partie, gere l'affichage de celle-ci,
 * gere l'affichage des Dialog selon certaines conditions, et le message d'etat
 * dans la barre d'etat.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 3.1
 */
public class Sequenceur {

    private int type_jeu = -1, tour = 0, taille = -1;
    private int[] score = {0, 0};
    private Joueur[] players;
    private Plateau jeu;
    private Affichage visuel;
    private Etat status;
    private JButton jouer;
    private ListenerHumain hilistener = new ListenerHumain();
    private final Vector<Log> tab_log = new Vector<Log>();
    private boolean inGame, open_dial, triche;
    private JLabel label_joueur, label_jeu;
    private JLabel[] label_score;
    private JPanel zone_jeu;
    private JList liste_log;
    private final OptJoueurs config_joueur = new OptJoueurs(this);
    private final ChoixJeuAvance config_jeu = new ChoixJeuAvance(this);

    /**
     *  Constructeur de la classe, constructeur vide.
     */
    public Sequenceur() {
    }

    /**
     *  Cette methode permet d'ajouter au champ de <b>JLabel</b> et
     * <b>JLabel[]</b> correspondant, les labels qui pourront ainsi etre
     * modifie selon l'etat du jeu et afficher l'information adequate.
     *
     * @param labels Tableau contenant les <b>JLabel</b> necessaires. Ces labels
     * doivent etre fournis dans l'ordre suivant : 1- label d'etat, 2- label du
     * jeu en cours, 3- label du joueur en cours, 4- 2 label d'affichage de
     * score.
     */
    public void ajoutLabels(JLabel[] labels) {
        String[] initial = new String[]{"Bienvenue dans Games 4 all!", "5"};
        status = new Etat(labels[0], initial);

        label_jeu = labels[1];
        label_joueur = labels[2];
        JLabel[] tableau = new JLabel[]{labels[3], labels[4]};
        label_score = tableau;
    }

    /**
     *  Cette methode permet d'ajouter au champ <b>JPanel</b> correspondant,
     * le panel sur lequel le plateau de jeu s'affichera.
     *
     * @param display <b>JPanel</b> qui recevra l'affichage des jeux.
     */
    public void ajoutZoneJeu(JPanel display) {
        zone_jeu = display;
    }

    /**
     *  Cette methode permet d'ajouter au champ <b>JButton</b> correspondant,
     * le bouton qui executera le tour d'un joueur de type Intelligence
     * artificielle.
     *
     * @param bouton <b>JButton</b> qui, lors d'un clic executera le code
     * associe pour faire jouer l'IA.
     */
    public void ajoutBoutonNext(JButton bouton) {
        jouer = bouton;
        jouer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jouer();
            }
        });
    }

    /**
     *  Cette methode permet d'ajouter au champ <b>JList</b> correspondant,
     * la liste qui affichera les Log, mais aussi lui associer ce qu'il se passe
     * lorsqu'un element est selectionne.
     *
     * @param log <b>JList</b> qui sera affichee en tant que Log du programme.
     */
    public void ajoutLog(JList log) {
        liste_log = log;
        liste_log.setListData(tab_log);
        liste_log.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = liste_log.getSelectedIndex();
                if (index >= 0) {
                    JOptionPane.showMessageDialog(null,
                            tab_log.get(index).getFull(),
                            "Log - detail",
                            JOptionPane.INFORMATION_MESSAGE);
                    liste_log.clearSelection();
                }
            }
        });
    }

    /**
     *  Cette methode s'occupe de creer une nouvelle partie, avec les memes
     * joueurs et le meme jeu (ainsi que taille de grille), si les conditions
     * pour lancer une nouvelle partie sont remplies. Ces conditions sont :
     * avoir des joueurs en instance. Dans le cas contraire, la methode ouvre
     * un dialogue demandant l'information manquante.
     */
    public void nouvellePartie() {
        if (!open_dial) {
            if (players != null) {
                clorePartie();
                initialiseJeu();
                montreEtAttend();
            } else {
                if (type_jeu != -1) {
                    config_joueur.setVisible(true);
                } else {
                    config_jeu.setVisible(true);
                }
            }
        }
    }

    /**
     *  Cette methode permet changer de jeu. Soit directement avec les reglages
     * natifs des jeux, via le <b>JMenuItem</b> correspondant. Soit acceder au
     * dialogue de jeu avance.
     *
     * @param jeu_choisi Entier representant le choix du jeu a lancer ou
     * l'affichage du dialogue de jeu avance.
     */
    public void changerJeu(int jeu_choisi) {
        if (!open_dial) {
            int rep = -1;
            if (inGame) {
                rep = JOptionPane.showConfirmDialog(null, "Souhaitez-vous "
                        + "reellement changer de jeu maintenant ?\n",
                        "Changer de jeu ?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
            }
            if (!inGame || rep == 0) {
                if (jeu_choisi == 3) {
                    config_jeu.setVisible(true);
                } else {
                    changerTypeEtTaille(jeu_choisi);
                    majJoueurs();
                    nouvellePartie();
                }
            }
        }
    }

    /**
     *  Cette methode est appelee lorsqu'une action est lancee par un
     * <b>JMenuItem</b> ou <b>JButton</b> correspondant, et affiche le dialogue
     * concernant les joueurs, si un type de partie est prevu. Sinon, la methode
     * propose de choisir un jeu et d'en creer une partie.
     */
    public void changerJoueurs() {
        if (!open_dial) {
            if (type_jeu != -1) {
                config_joueur.setVisible(!open_dial);
            } else {
                int rep = -1;
                rep = JOptionPane.showConfirmDialog(null, "Avant de creer des "
                        + "joueurs, veuillez choisir un jeu.\n"
                        + "Choisir maintenant ?",
                        "Erreur",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE);
                if (rep == 0) {
                    changerJeu(3);
                }
            }
        }
    }

    /**
     *  Cette methode permet de preparer le type d'un jeu en cours de
     * preparation. Elle remplace le type de jeu actuel, par le futur, et
     * la taille du plateau actuel, par le futur.
     *
     * @param type Entier designant le futur jeu.
     * @param lignes Entier designant la taille de la grille du futur jeu.
     */
    public void changerTypeEtTaille(int type, int lignes) {
        if (type_jeu != type) {
            score = new int[]{0, 0};
            type_jeu = type;
        }
        taille = lignes;
    }

    /**
     *  Cette methode permet de preparer le type d'un jeu en cours de
     * preparation. Elle remplace le type de jeu actuel, par le futur, et
     * la taille du plateau actuel, par la taille native du futur jeu.
     *
     * @param type Entier designant le futur jeu.
     */
    public void changerTypeEtTaille(int type) {
        int lignes = 0;
        switch (type) {
            case 0:
                lignes = 3;
                break;
            case 1:
                lignes = 0;
                break;
            case 2:
                lignes = 8;
                break;
            default:
                type_jeu = -1;
                break;
        }
        changerTypeEtTaille(type, lignes);
    }

    /**
     *  Cette methode initialise ce qui sera necessaire au deroulement de la
     * partie qui debute : un nouveau plateau de jeu, un nouvel affichage, les
     * Listener necessaire pour le fonctionnement du jeu et affiche le nouvel
     * Etat du jeu.
     *  Elle met aussi a jour le label du jeu en cours, le Log et l'index des
     * joueurs.
     */
    public void initialiseJeu() {
        label_jeu.setText(ListeJeux.liste[type_jeu]);
        switch (type_jeu) {
            case 0:
                jeu = new PlateauMorpion(taille);
                visuel = new Affichage_Morpion(zone_jeu);
                tab_log.add(0, new Log(1));
                break;
            case 1:
                jeu = new PlateauPuissance4();
                visuel = new Affichage_Puissance4(zone_jeu);
                tab_log.add(0, new Log(2));
                break;
            case 2:
                jeu = new PlateauOthello(taille);
                visuel = new Affichage_Othello(zone_jeu);
                tab_log.add(0, new Log(3));
                break;
            default:
                jeu = null;
                visuel = null;
                break;
        }
        majLog();
        tour = 0;

        visuel.initialise(jeu);
        changerEtat(new String[]{"Nouveau jeu lance", "3"});

        boolean test = true;
        for (int i = 0; i < players.length && test; i++) {
            if (players[i].getTypeJoueur() == 0) {
                test = false;
            }
        }
        if (test == true) {
            JOptionPane.showMessageDialog(zone_jeu,
                    "Il n'y a que des IA, le programme risque\n"
                    + "de paraitre lent, mais rien d'anormal.",
                    "Attention",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        ouvrirPartie();
    }

    /**
     *  Cette methode remplace le champ tableau de <b>Joueur</b> actuel, par
     * celui des nouveau joueur.
     *
     * @param bleus Tableau de <b>Joueur</b> qui remplace l'ancien.
     */
    public void remplaceJoueurs(Joueur[] bleus) {
        players = bleus;
    }

    /**
     *  Cette methode remplace le champ <b>boolean</b> actuel, <i>true</i> si
     * un dialogue est actuellement affiche, <i>false</i> si aucun dialogue est
     * affiche.
     *
     * @param statut <i>true</i> si un dialogue est affiche, <i>false</i> si
     * aucun dialogue n'est affiche.
     */
    public void statutDialogues(boolean statut) {
        open_dial = statut;
    }

    /**
     *  Cette methode permet d'activer la visibilite, durant une partie, des
     * coups sur lesquels le joueur actuel peut jouer.
     *
     * @param value <i>1</i> s'il faut afficher l'aide, <i>0</i> sinon.
     */
    public void activeTriche(int value) {
        if (value == 1) {
            triche = true;
        } else {
            triche = false;
        }
        if (inGame) {
            java.util.ArrayList<Coup> toLight = null;
            if (triche) {
                toLight = jeu.coupPossible(tour);
            }
            visuel.afficherHalo(toLight);
        }
    }

    /**
     *  Cette methode permet d'annuler le dernier <b>Coup</b> applique au jeu.
     */
    public void annuleCoup(){
        if (jeu != null && jeu.obtenirDernierCoup() != null) {
        	String newEtat = "";
            jeu.retourArriere();
            tour = (tour + 1) % 2;
            visuel.toutActualiser(jeu);
            if(!inGame)
                tab_log.remove(0);
            newEtat = tab_log.get(0) + ", coup annule";
            tab_log.remove(0);
            majLog();
            ouvrirPartie();
            montreEtAttend();
            changerEtat(new String[]{newEtat, "3"});
        }
    }

    /**
     *  Cette methode permet de renseigner l'appelant sur l'etat de la partie
     * courante, si elle existe.
     *
     * @return <i>true</i> si une partie existe et n'est pas terminee,
     * <i>false</i> sinon.
     */
    public boolean jeuEnCours() {
        return inGame;
    }

    /**
     *  Cette methode permet de renseigner l'appelant sur quel jeu est ou a ete
     * joue dernierement.
     *
     * @return Entier indiquant quel jeu a ete lance dernierement. 0- Morpion,
     * 1- Puissance4 et 2- Othello.
     */
    public int obtenirTypeJeu() {
        return type_jeu;
    }

    /**
     *  Cette methode permet de renseigner l'appelant sur les joueurs actuels
     * en lui fournissant la reference vers le tableau de <b>Joueur</b>.
     *
     * @return Tableau de <b>Joueur</b> actuellement utilise.
     */
    public Joueur[] obtenirJoueurs() {
        return players;
    }

    /**
     *  Cette methode permet de changer les informations diffusees dans la barre
     * d'etat.
     *
     * @param etat Tableau de <b>String</b>, contenant la phrase a indiquer dans
     * la barre d'etat, et le temp durant lequel il faut l'afficher.
     */
    public void changerEtat(String[] etat) {
        status.changerEtat(etat);
    }

    /**
     *  Cette methode permet de modifier/ajouter tous les objets influences par
     * l'etat en cours de partie. Elle arme les boutons qui doivent l'etre, et
     * ajoute les listener concernant le jeu actuel.
     */
    public void ouvrirPartie() {
        if(!inGame){
            inGame = true;
            zone_jeu.addMouseListener(hilistener);
            zone_jeu.addMouseListener(visuel.obtenirListener());
            zone_jeu.addComponentListener(visuel.obtenirListenerEchelle());
            jouer.setEnabled(false);
        }
    }

    /**
     *  Cette methode permet de modifier/supprimer tous objets influences par
     * l'etat de non partie actuelle. Elle desarme les boutons qui ne doivent
     * plus l'etre, et supprime les listener concernant le jeu actuel.
     */
    public void clorePartie() {
        if (inGame) {
            inGame = false;
            zone_jeu.removeMouseListener(hilistener);
            zone_jeu.removeMouseListener(visuel.obtenirListener());
            zone_jeu.removeComponentListener(visuel.obtenirListenerEchelle());
            jouer.setEnabled(false);
            tab_log.add(0, new Log(-1));
            majLog();
        }
    }

    /**
     *  Cette methode permet de mettre a jour les informations diffusees dans
     * les divers labels du jeu, comme le pseudo du joueur actuel, a cet index,
     * et le score de chaque joueur.
     */
    private void majExtra() {
        label_joueur.setText(players[tour].getName());
        if (jeu instanceof IScore) {
            for (int i = 0; i < label_score.length; i++) {
                label_score[i].setText(players[i].getName() + " - " + score[i]
                        + " - " + ((IScore) jeu).obtenirScore(i) + " pts");
            }
        } else {
            for (int i = 0; i < label_score.length; i++) {
                label_score[i].setText(players[i].getName() + " - " + score[i]);
            }
        }
    }

    /**
     *  Cette methode permet de mettre a jour les informations qui se trouve
     * dans la <b>JList</b>, une fois que la liste a ete modifiee.
     */
    private void majLog() {
        liste_log.setListData(tab_log);
    }

    /**
     *  Cette methode est la principale lors d'une partie. Elle verifie que la
     * partie ne soit pas terminee. Si elle l'est, elle lance le script de fin
     * de partie. Si la partie continue, elle met a jour les informations,
     * verifie que le joueur courant puisse jouer, si ca n'est pas le cas, fait
     * passer le tour du joueur. Elle actualise les indications des coups
     * jouables, si demande. Et pour terminer, elle active ce qui est necessaire
     * au joueur actuel pour le laisser jouer.
     */
    public void montreEtAttend() {
        int gamestatus = jeu.finPartie();
        if (gamestatus != -2) {
            if (inGame) {
                partieFinie(gamestatus);
            }
        } else {
            majExtra();
            if (!jeu.precondition(tour)) {
                tour = (tour + 1) % players.length;
                montreEtAttend();
            } else {
                if (triche) {
                    visuel.afficherHalo(jeu.coupPossible(tour));
                }
                if (players[tour].getTypeJoueur() == 0) {
                    jouer.setEnabled(false);
                    changerEtat(new String[]{"C'est a vous", "3"});
                } else {
                    jouer.setEnabled(true);
                    jouer.setFocusable(true);
                    changerEtat(new String[]{"-", "0"});
                }
            }
        }
    }

    /**
     *  Cette methode est appelee une seule fois par partie, lorsque celle-ci
     * finie. Elle affiche qui a gagne ou s'il s'agit d'un match nul. Elle
     * augmente aussi le score du gagnant.
     *
     * @param stat Entier representant le type de fin de partie. -1 signifie
     * match nul, et pour les valeurs >= 0, l'index du gagnant.
     */
    public void partieFinie(int stat) {
        if (stat != -2) {
            changerEtat(new String[]{"Partie terminee", "5"});
            visuel.eteindreHalo();
            clorePartie();
            if (stat == -1) {
                JOptionPane.showMessageDialog(zone_jeu,
                        "Match nul!",
                        "Fin partie",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                score[stat]++;
                majExtra();
                JOptionPane.showMessageDialog(zone_jeu,
                        "c'est " + players[stat].getName() + " qui a gagne!",
                        "Fin partie",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     *  Cette methode est appelee lorsqu'un joueur a effectue son coup et s'est
     * decide a le jouer. La methode verifie si son coup est autorise, si oui,
     * elle l'applique au plateau, actualise l'affichage, met a jour le log
     * et incremente l'index du joueur. Si non, elle avertit dans l'etat que le
     * coup n'est pas valide.
     */
    private void jouer() {
        java.awt.Cursor curse = jouer.getCursor();
        jouer.setCursor(curse.getPredefinedCursor(curse.WAIT_CURSOR));
        Coup toDo;
        changerEtat(new String[]{"Attendez...", "3"});
        toDo = players[tour].obtenirCoup(jeu, tour);
        if (jeu.verification(toDo, tour)) {
            jeu.appliquer(toDo, tour);
            visuel.actualise(jeu, tour);
            tab_log.add(0, new Log(players[tour].getName(),
                    jeu.obtenirDernierCoup()));
            tour = (tour + 1) % 2;
            jouer.setCursor(curse.getPredefinedCursor(curse.DEFAULT_CURSOR));
            majLog();
            montreEtAttend();
        } else {
            changerEtat(new String[]{"Mauvais coup", "3"});
            jouer.setCursor(curse.getPredefinedCursor(curse.DEFAULT_CURSOR));
        }
    }

    /**
     *  Cette methode est appelee pour mettre a jour les joueurs, car les
     * joueurs dependent du type de jeu sur lequel ils se trouvent, surtout pour
     * les joueurs de type IA.
     */
    public void majJoueurs() {
        if(players != null)
            config_joueur.majTypeJoueurs();
    }

    /**
     *  Cette classe est prevue pour reagir lors d'une partie, des clics d'un
     * joueur humain, ce qui permet a celui-ci de jouer un <b>Coup</b> par un
     * clic. Car si le clic se fait sur une coordonnee ou s'applique ce Listener
     * alors, le <b>Coup</b> recevable de ce joueur sera mis-a-jour, et
     * appellera la methode <i>jouer</i>.
     */
    private class ListenerHumain extends JButton implements MouseListener {

        /**
         * Methode vide.
         *
         * @param e Evenement genere par un clic de souris.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         * Methode vide.
         *
         * @param e Evenement genere par un clic de souris.
         */
        @Override
        public void mousePressed(MouseEvent e) {
        }

        /**
         *  Cette methode va rechercher ou le <b>Coup</b> a ete fait, et si
         * le joueur actuel, a cet index est Humain, alors ce <b>Coup</b> va
         * lui etre fourni pour le mettre a jour, et ensuite la methode
         * <i>jouer</i> sera appelee. S'il ne s'agit pas d'un Humain, rien n'est
         * fait.
         *
         * @param e Evenement genere par le relachement d'une touche de la
         * souris.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (players[tour].getTypeJoueur() == 0) {
                Coup cp = visuel.obtenirOu();
                ((Composants.Humain) players[tour]).update(cp);
                jouer();
            } else {
                changerEtat(new String[]{"C'est a l'ordinateur!", "3"});
            }
        }

        /**
         * Methode vide.
         *
         * @param e Evenement genere par l'entre de la souris dans la zone ou
         * s'applique le Listener.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Methode vide.
         *
         * @param e Evenement genere par la sortie de la souris dans la zone
         * ou s'applique le Listener.
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}

