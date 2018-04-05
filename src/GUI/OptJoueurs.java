package GUI;

import Composants.Aleatoire;
import Composants.AlphaBeta;
import Composants.Humain;
import Composants.Joueur;
import Composants.ListeJeux;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *  Cette classe permet de demander au(x) joueur(s) de configurer leur
 * representation dans le jeu. Cette classe reste en stand-by lorsqu'elle n'est
 * pas utilise, et ainsi conserver les informations les concernants.
 * 
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 2.1
 */
public class OptJoueurs extends JDialog {

    private boolean once = false;
    private final Sequenceur seq;
    private int joueur = 0;
    private int[] index = {0, 0}, para1 = {4, 4}, para2 = {5, 5};
    private final int[] MOYEN = {2, 2}, DIFFICILE = {5, 4};
    private String[] pseudo = {"Joueur 1", "Joueur 2"};
    private JLabel nom, jcourant;
    private JPanel pan_pri, englobas, config;
    private JSlider slid1, slid2;
    private JTextField pseudoFiel;
    private JRadioButton radio1, radio2;
    private JButton lancer, pousse, reduit, suivant, precedent;
    private final java.awt.Dimension SIMPLE = new java.awt.Dimension(375, 250);
    private final java.awt.Dimension AVANCE = new java.awt.Dimension(375, 400);
    private final Font petitf = new Font("Default", Font.PLAIN, 11);
    private final Font grasf = new Font("Default", Font.BOLD, 10);
    private JList profils = new JList(new String[]{"facile", "moyen",
                "difficile", "personnalise"});

    /**
     *  Constructeur de la classe, instancie tous les champs en les initialisant
     * et initialise l'affichage des informations, tout en restant cacher.
     * 
     * @param sequence Instance actuelle du <b>Sequenceur</b>.
     */
    public OptJoueurs(Sequenceur sequence) {
        this.seq = sequence;
        initialise();
        setVisible(false);
        rafraichir();
    }

    /**
     *  Cette methode cree tous les objets a ajouter sur la fenetre de dialog,
     * les dispose et ajoute les Listener necessaire.
     */
    private void initialise() {
        /*1.1 - Parametres de la JDialog*/
        int operation = javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
        setDefaultCloseOperation(operation);
        setTitle("Configuration joueurs");
        Image icon = (new ImageIcon("./src/Images/icoJoueurs.gif")).getImage();
        setIconImage(icon);
        setLocationRelativeTo(rootPane);
        setAlwaysOnTop(true);
        setMinimumSize(SIMPLE);
        setResizable(false);
        setLayout(null);
        /*---------------------------FIN de 1.1-------------------------------*/
        /*1.2 - Ajout des objets*/
        /*1.2.1 - Ajout du textField*/
        JPanel infos = new JPanel(new java.awt.BorderLayout(10, 0));
        nom = new JLabel("Pseudo :");
        nom.setFont(grasf);
        infos.add(nom, java.awt.BorderLayout.WEST);
        pseudoFiel = new JTextField();
        pseudoFiel.setFont(petitf);
        infos.add(pseudoFiel, java.awt.BorderLayout.CENTER);
        lancer = new JButton("Lancer Partie!");
        lancer.setFont(petitf);
        lancer.setFocusable(true);
        lancer.setEnabled(false);
        infos.add(lancer, java.awt.BorderLayout.EAST);
        infos.setBounds(5, 2, 340, 24);
        add(infos);
        /*--------------------------FIN de 1.2.1------------------------------*/
        /*1.2.2 - Ajout des boutons*/
        englobas = new JPanel(new java.awt.BorderLayout());
        JPanel boutons = new JPanel(new java.awt.GridLayout(1, 3));
        precedent = new JButton("Precedent");
        precedent.setFont(petitf);
        precedent.setEnabled(false);
        boutons.add(precedent);
        jcourant = new JLabel("Joueur " + (joueur + 1) + "/2");
        jcourant.setFont(grasf);
        jcourant.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        boutons.add(jcourant);
        suivant = new JButton("Suivant");
        suivant.setFont(petitf);
        boutons.add(suivant);
        englobas.add(new javax.swing.JSeparator(), java.awt.BorderLayout.NORTH);
        englobas.add(boutons, java.awt.BorderLayout.CENTER);
        englobas.setBounds(2, 182, 355, 28);
        add(englobas);
        /*--------------------------FIN de 1.2.2------------------------------*/
        /*---------------------------FIN de 1.2-------------------------------*/
        /*1.3 - Creation des panels*/
        /*1.3.1 - Creation du panel simple*/
        pan_pri = new JPanel(null);
        pan_pri.setBounds(2, 30, 355, 155);
        add(pan_pri);
        /*--------------------------FIN de 1.3.1------------------------------*/
        /*1.3.2 - Ajout des objets sur panel simple*/
        javax.swing.ButtonGroup bg = new javax.swing.ButtonGroup();
        radio1 = new JRadioButton("Humain");
        radio1.setFont(petitf);
        bg.add(radio1);
        radio2 = new JRadioButton("Ordinateur");
        radio2.setFont(petitf);
        bg.add(radio2);

        TitledBorder tb = new TitledBorder(null,
                "Type de joueur :",
                0,
                TitledBorder.ABOVE_TOP,
                grasf);
        JPanel radios = new JPanel(new java.awt.GridLayout(2, 1));
        radios.setBorder(tb);
        radios.add(radio1);
        radios.add(radio2);
        radios.setBounds(27, 2, 125, 100);
        pan_pri.add(radios);

        pousse = new JButton("Avance");
        pousse.setFont(petitf);
        pousse.setBounds(0, 125, 118, 25);
        pan_pri.add(pousse);

        javax.swing.JScrollPane jsp = new javax.swing.JScrollPane(profils);
        profils.setFont(petitf);
        profils.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tb = new TitledBorder(null,
                "Profils :",
                0,
                TitledBorder.ABOVE_TOP,
                grasf);
        jsp.setBorder(tb);
        jsp.setBounds(180, 2, 175, 100);
        pan_pri.add(jsp);
        /*--------------------------FIN de 1.3.2------------------------------*/
        /*1.3.3 - Ajout d'objet pour panel avance*/
        reduit = new JButton("Simple");
        reduit.setFont(petitf);
        reduit.setBounds(0, 275, 118, 25);
        pan_pri.add(reduit);

        config = new JPanel(null);
        tb = new TitledBorder(null, "Configuration avancee :", 0,
                TitledBorder.ABOVE_TOP, grasf);
        config.setBorder(tb);
        config.setBounds(2, 110, 355, 150);
        pan_pri.add(config);

        JLabel intel = new JLabel("Intelligence :");
        intel.setFont(petitf);
        intel.setBounds(8, 29, 80, 25);
        config.add(intel);

        slid1 = new JSlider(0, 8);
        slid1.setMinorTickSpacing(1);
        slid1.setPaintTicks(true);
        slid1.setSnapToTicks(true);
        slid1.setPaintLabels(true);
        slid1.setFont(petitf);
        java.util.Hashtable ht = slid1.createStandardLabels(1);
        slid1.setLabelTable(ht);
        slid1.setBounds(100, 20, 225, 55);
        config.setVisible(false);
        config.add(slid1);

        JLabel audace = new JLabel("Agressivite :");
        audace.setFont(petitf);
        audace.setBounds(8, 91, 80, 25);
        config.add(audace);

        slid2 = new JSlider(0, 10);
        slid2.setMinorTickSpacing(1);
        slid2.setPaintTicks(true);
        slid2.setSnapToTicks(true);
        slid2.setPaintLabels(true);
        slid2.setFont(petitf);
        ht = slid2.createStandardLabels(1);
        slid2.setLabelTable(ht);
        slid2.setBounds(100, 82, 225, 55);
        config.add(slid2);
        /*--------------------------FIN de 1.3.3------------------------------*/
        /*---------------------------FIN de 1.3-------------------------------*/
        /*1.4 - Ajout des Listener*/
        /*1.4.0 - Listener Component*/
        addComponentListener(new ComponentEtat());
        /*--------------------------FIN de 1.4.1------------------------------*/
        /*1.4.1 - Listener boutons*/
        pousse.addActionListener(new MontreAvance());
        reduit.addActionListener(new CacheAvance());
        lancer.addActionListener(new LancerPartie());

        precedent.addActionListener(new PrecedentListener());
        suivant.addActionListener(new SuivantListener());
        /*--------------------------FIN de 1.4.1------------------------------*/
        /*1.4.2 - Listener Field*/
        pseudoFiel.addKeyListener(new PseudoListener());
        /*--------------------------FIN de 1.4.2------------------------------*/
        /*1.4.3 - Listener radio*/
        radio1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                index[joueur] = 0;
                rafraichir();
            }
        });
        radio2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                index[joueur] = 1;
                rafraichir();
            }
        });
        /*--------------------------FIN de 1.4.3------------------------------*/
        /*1.4.4 - Listener Slider*/
        slid1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                para1[joueur] = slid1.getValue();
            }
        });
        slid2.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                para2[joueur] = slid2.getValue();
            }
        });
        slid1.addMouseListener(new PersoListener());
        slid2.addMouseListener(new PersoListener());
        /*--------------------------FIN de 1.4.4------------------------------*/
        /*1.4.5 - Listener List*/
        profils.addListSelectionListener(new SelecListener());
        /*--------------------------FIN de 1.4.5------------------------------*/
        /*---------------------------FIN de 1.4-------------------------------*/
    }

    /**
     *  Cette methode permet d'afficher ou desafficher cette fenetre de dialog,
     * tout en permettant de signaler a l'instance de <b>Sequenceur</b> qu'un
     * dialog est actuellement afficher.
     *
     * @param on permet d'afficher (true) ou non (false) ce dialog.
     */
    @Override
    public void setVisible(boolean on) {
        seq.statutDialogues(on);
        super.setVisible(on);
        if (on) {
            String titleb = ListeJeux.liste[seq.obtenirTypeJeu()];
            setTitle("Configuration joueurs - " + titleb);
            rafraichir();
        }
    }

    /**
     *  Cette methode permet de reinstancier les joueurs lors d'un changement
     * de jeu, sans repasser par cette fenetre.
     */
    public void majTypeJoueurs() {
        int type = seq.obtenirTypeJeu();
        Joueur[] tab = new Joueur[2];
        for (int i = 0; i < 2; i++) {
            switch (index[i]) {
                case 1:
                    tab[i] = new Aleatoire(pseudo[i], type);
                    break;
                case 2:
                    tab[i] = new AlphaBeta(pseudo[i], type, 2, 2, 2);
                    break;
                case 3:
                    tab[i] = new AlphaBeta(pseudo[i], type, 5, 4, 2);
                    break;
                case 4:
                    tab[i] = new AlphaBeta(pseudo[i], type,
                            para1[i], para2[i], 2);
                    break;
                default:
                    tab[i] = new Humain(pseudo[i]);
                    break;
            }
        }
        seq.remplaceJoueurs(tab);
    }

    /**
     *  Cette methode permet de rafraichir les informations affichees a l'ecran.
     * Elle est appelee lorsqu'il y a eu une interaction avec le dialog.
     */
    private void rafraichir() {
        pseudoFiel.setText(pseudo[joueur]);
        jcourant.setText("Joueur " + (joueur + 1) + "/2");
        String title;
        if (seq.jeuEnCours()) {
            title = "Reprendre Partie!";
        } else {
            title = "Lancer Partie!";
        }
        lancer.setText(title);
        if (index[joueur] == 0) {
            radio1.setSelected(true);
            profils.setEnabled(false);
            slid1.setEnabled(false);
            slid2.setEnabled(false);
        } else {
            radio2.setSelected(true);
            profils.setEnabled(true);
            profils.setSelectedIndex(index[joueur] - 1);
            if (index[joueur] > 1) {
                slid1.setEnabled(true);
                slid2.setEnabled(true);
                slid1.setValue(para1[joueur]);
                slid2.setValue(para2[joueur]);
            } else {
                slid1.setEnabled(false);
                slid2.setEnabled(false);
            }
        }
    }

    /**
     *  Cette classe implemente <b>ComponentListener</b>, elle doit etre
     * applique sur le composant qui possede le panel d'Etat. Elle permet de
     * faire coller le panel d'Etat au bas du composant qui ne possede pas de
     * Layout.
     */
    class ComponentEtat implements ComponentListener {

        /**
         *  Cette methode fait suivre le panel d'Etat avec le bas du composant.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentResized(ComponentEvent e) {
            englobas.setBounds(2, getHeight() - 68, 355, 28);
        }

        /**
         *  Methode vide.
         * 
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentMoved(ComponentEvent e) {
        }

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentShown(ComponentEvent e) {
        }

        /**
         * Methode vide.
         * 
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentHidden(ComponentEvent e) {
        }
    }

    /**
     *  Cette classe implemente <b>ActionListener</b>, elle permet lorsqu'une
     * Action pour ce Listener a ete lancee, d'afficher le Panel Avance de
     * configuration du joueur.
     */
    class MontreAvance implements ActionListener {

        /**
         *  Cette methode affiche le panel avance.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            pan_pri.setVisible(false);
            for (int i = 0; i < 50; i++) {
                setSize(new java.awt.Dimension(SIMPLE.width,
                        SIMPLE.height + (i * 3)));
            }
            setMinimumSize(AVANCE);
            config.setVisible(true);
            int hauteur = AVANCE.height - 65 - 30;
            pan_pri.setSize(355, hauteur);
            pan_pri.setVisible(true);
            pousse.setVisible(false);
            reduit.setVisible(true);
        }
    }

    /**
     *  Cette classe implemente <b>ActionListener</b>, elle permet lorsqu'une
     * Action pour ce Listener a ete lancee, de cacher le Panel Avance de
     * configuration du joueur.
     */
    class CacheAvance implements ActionListener {

        /**
         *  Cette methode cache le panel avance.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            config.setVisible(false);
            pan_pri.setVisible(false);
            setMinimumSize(SIMPLE);
            for (int i = 0; i < 50; i++) {
                setSize(new java.awt.Dimension(AVANCE.width,
                        AVANCE.height - (i * 3)));
            }
            int hauteur = SIMPLE.height - 65 - 30;
            pan_pri.setSize(355, hauteur);
            pan_pri.setVisible(true);
            reduit.setVisible(false);
            pousse.setVisible(true);
        }
    }

    /**
     *  Cette classe implemente <b>ActionListener</b>, elle permet lorsqu'une
     * Action pour ce Listener a ete lancee, de lancer ou reprendre la partie,
     * selon qu'une partie est en cours ou non.
     */
    class LancerPartie implements ActionListener {

        /**
         *  Cette methode lance/reprend une partie.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            majTypeJoueurs();
            seq.changerEtat(new String[]{"Changement de joueur(s) effectue",
                        "5"});
            if (!seq.jeuEnCours()) {
                seq.nouvellePartie();
            } else {
                seq.montreEtAttend();
            }
        }
    }

    /**
     *  Cette classe implemente <b>ActionListener</b>, elle permet lorsqu'une
     * Action pour ce Listener a ete lancee, de revenir sur un joueur precedent.
     */
    class PrecedentListener implements ActionListener {

        /**
         *  Cette methode montre les informations du joueur precedent.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            joueur--;
            suivant.setEnabled(true);
            if (joueur == 0) {
                precedent.setEnabled(false);
            }
            rafraichir();
        }
    }

    /**
     *  Cette classe implemente <b>ActionListener</b>, elle permet lorsqu'une
     * Action pour ce Listener a ete lancee, de revenir sur un joueur suivant.
     */
    class SuivantListener implements ActionListener {

        /**
         *  Cette methode montre les informations du joueur suivant.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            joueur++;
            precedent.setEnabled(true);
            if (joueur == pseudo.length - 1) {
                suivant.setEnabled(false);
                lancer.setEnabled(true);
            }
            rafraichir();
        }
    }

    /**
     *  Cette classe implemente <b>KeyListener</b>, elle permet lorsqu'un
     * Evenement pour ce Listener a ete lancee, de gerer ce qui se passe dans
     * le <b>JTextField</b> du pseudo, comme limiter la taille du pseudo et
     * enregistrer ce qui y a ete note.
     */
    class PseudoListener implements KeyListener {

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par un <b>KeyEvent</b>.
         */
        @Override
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par un <b>KeyEvent</b>.
         */
        @Override
        public void keyPressed(KeyEvent e) {
        }

        /**
         * Limite et enregistre ce qui a ete note dans le champ de texte.
         *
         * @param e Type d'evenement genere par un <b>KeyEvent</b>.
         */
        @Override
        public void keyReleased(KeyEvent e) {
            if (pseudoFiel.getText().length() > 12) {
                pseudoFiel.setText(pseudo[joueur]);
            } else {
                pseudo[joueur] = pseudoFiel.getText();
            }
        }
    }

    /**
     *  Cette classe implemente <b>MouseListener</b>, elle permet lorsqu'un
     * Evenement pour ce Listener a ete lancee, de notifier a la <b>JList</b>
     * que le slide sur lequel il est ajoute a ete personnalise.
     */
    class PersoListener implements MouseListener {

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par un <b>MouseEvent</b>.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par un <b>MouseEvent</b>.
         */
        @Override
        public void mousePressed(MouseEvent e) {
        }

        /**
         * Cette methode est appelle
         *
         * @param e Type d'evenement genere par un <b>MouseEvent</b>.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            int dernierIndex = 3;
            int indexCourant = profils.getSelectedIndex();
            if (0 < indexCourant && indexCourant < dernierIndex) {
                profils.setSelectedIndex(dernierIndex);
            }
        }

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par un <b>MouseEvent</b>.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Methode vide.
         *
         * @param e Type d'evenement genere par un <b>MouseEvent</b>.
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     *  Cette classe implemente <b>ListSelectionListener</b>, elle permet
     * lorsqu'un Evenement pour ce Listener a ete lancee, de notifier aux
     * sliders que leur valeur doit etre mise a jour selon la valeur
     * selectionee.
     */
    class SelecListener implements ListSelectionListener {

        /**
         *  Cette methode change les valeurs des sliders selon certaines
         * conditions de selection.
         *
         * @param e Type d'evenement genere par un <b>ListSelectionEvent</b>.
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            index[joueur] = profils.getSelectedIndex() + 1;
            if (index[joueur] == 2) {
                slid1.setValue(MOYEN[0]);
                slid2.setValue(MOYEN[1]);
            } else if (index[joueur] == 3) {
                slid1.setValue(DIFFICILE[0]);
                slid2.setValue(DIFFICILE[1]);
            } else if (index[joueur] == 4) {
                if (pousse.isVisible()) {
                    pousse.doClick();
                }
            }
            rafraichir();
        }
    }
}
