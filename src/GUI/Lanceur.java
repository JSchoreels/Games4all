package GUI;

import Listener.JMenuAbout;
import Listener.DateListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 *  Cette classe affiche l'interface graphique principale du projet, qui permet
 * a n'importe quel utilisateur de pouvoir (inter)agir avec le projet tout
 * entier. Elle s'occupe d'agencer bon nombre d'elements avec lesquels le joueur
 * peut faire des actions, et de gerer ces actions.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.3
 */
public class Lanceur extends JFrame {

    private JButton jButton3;
    public static JLabel jLabel1 = new JLabel("Date");
    private JLabel jLabel2, jLabel3, jLabel4, jLabel5, jLabel6;
    private JPanel jPanel4, jPanel6;
    private JPanel marge_nord, marge_sud, marge_ouest, marge_est;
    private JList jLog;
    private Font petitf = new Font("Default", Font.PLAIN, 11);
    private Font grasf = new Font("Default", Font.BOLD, 10);
    private Sequenceur sequence;
    private final Help help;
    private final FabriqueMarges square = new FabriqueMarges();

    /**
     *  Constructeur de la classe, initialise les objets de la GUI et
     * fait correspondre les Actions associees.
     */
    public Lanceur() {
        initialiseLookAndFeel();
        help = new Help();
        sequence = new Sequenceur();
        initialise();
        JLabel[] tab = {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6};
        sequence.ajoutLabels(tab);
        sequence.ajoutZoneJeu(jPanel6);
        sequence.ajoutBoutonNext(jButton3);
        sequence.ajoutLog(jLog);
        initialiseDate();
        setVisible(true);
    }

    /**
     *  Cette methode initilise un nouveau Thread qui affichera dans le cote
     * gauche de la barre d'etat, l'heure et la date actuelle.
     */
    private void initialiseDate() {
        Thread date = new Thread(new DateListener());
        date.run();
    }

    /**
     *  Cette methode permet d'initialiser le <b>LookAndFeel</b> que la GUI
     * utilisera par defaut, il s'agit d'utiliser les objets graphiques de l'OS
     * dans ce cas.
     */
    private void initialiseLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "Le theme demande n'est pas supporte.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "La classe pour la nouvelle apparence non trouvee.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "La classe necessaire au changement n'est pas instanciable.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "Le programme n'arrive pas a acceder a votre demande.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *  Cette methode est la plus importante car c'est elle qui ajoute les
     * objets a la fenetre, tout en les disposants comme il faut et en leur
     * attribuant leur action, si necessaire.
     */
    private void initialise() {
        /*1.1 - Parametres de la JFrame*/
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Games 4 all");
        Image icon = (new ImageIcon("./src/Images/icoG4a.gif")).getImage();
        setIconImage(icon);
        setMinimumSize(new java.awt.Dimension(725, 500));
        Dimension scSi = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((scSi.width - 725) / 2, (scSi.height - 500) / 2, 725, 500);
        setLayout(new BorderLayout());
        /*----------------------------FIN de 1.1------------------------------*/
        /*1.2 - Initialisation de la barre de menu et des Menus*/
        JMenuBar jMenuBar1 = new JMenuBar();

        JMenu jMenu1 = new JMenu("Fichier");
        jMenu1.setMnemonic(KeyEvent.VK_F);
        JMenuItem jMenuItem1 = new JMenuItem("Nouvelle partie", KeyEvent.VK_N);
        jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,
                InputEvent.ALT_DOWN_MASK));
        jMenu1.add(jMenuItem1);

        JMenu jMenu4 = new JMenu("Choisir jeu");
        jMenu4.setMnemonic(KeyEvent.VK_C);
        JMenuItem jMenuItem3_1 = new JMenuItem("Morpion");
        jMenuItem3_1.setIcon(new ImageIcon("./src/Images/icoMorpion.gif"));
        jMenu4.add(jMenuItem3_1);
        JMenuItem jMenuItem3_2 = new JMenuItem("Puissance 4");
        jMenuItem3_2.setIcon(new ImageIcon("./src/Images/icoPuissance4.gif"));
        jMenu4.add(jMenuItem3_2);
        JMenuItem jMenuItem3_3 = new JMenuItem("Othello");
        jMenuItem3_3.setIcon(new ImageIcon("./src/Images/icoOthello.gif"));
        jMenu4.add(jMenuItem3_3);
        JMenuItem jMenuItem3_4 = new JMenuItem("Avance");
        jMenuItem3_4.setIcon(new ImageIcon("./src/Images/icoAvance.gif"));
        jMenu4.add(jMenuItem3_4);
        jMenu1.add(jMenu4);

        JSeparator jSeparator0 = new JSeparator();
        jMenu1.add(jSeparator0);

        JMenuItem jMenuItem11 = new JMenuItem("Annuler", KeyEvent.VK_Z);
        jMenuItem11.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                InputEvent.CTRL_DOWN_MASK));
        jMenu1.add(jMenuItem11);

        JSeparator jSeparator1 = new JSeparator();
        jMenu1.add(jSeparator1);

        JMenuItem jMenuItem4 = new JMenuItem("Quitter", KeyEvent.VK_Q);
        jMenuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.ALT_DOWN_MASK));
        jMenu1.add(jMenuItem4);
        jMenuBar1.add(jMenu1);

        JMenu jMenu2 = new JMenu("Options");
        jMenu2.setMnemonic(KeyEvent.VK_O);
        JMenu jMenu3 = new JMenu("Apparences");
        JMenuItem jMenuItem5 = new JMenuItem("Metal");
        jMenu3.add(jMenuItem5);
        JMenuItem jMenuItem6 = new JMenuItem("Nimbus");
        jMenu3.add(jMenuItem6);
        JMenuItem jMenuItem7 = new JMenuItem("OS");
        jMenu3.add(jMenuItem7);
        jMenu2.add(jMenu3);

        JCheckBoxMenuItem triche = new JCheckBoxMenuItem("Jouables ?");
        triche.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
                InputEvent.CTRL_DOWN_MASK));
        jMenu2.add(triche);

        JCheckBoxMenuItem historique = new JCheckBoxMenuItem("Log");
        historique.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                InputEvent.CTRL_DOWN_MASK));
        jMenu2.add(historique);

        JCheckBoxMenuItem wideSc = new JCheckBoxMenuItem("Mode etire");
        wideSc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        jMenu2.add(wideSc);

        JSeparator jSeparator2 = new JSeparator();
        jMenu2.add(jSeparator2);

        JMenuItem jMenuItem8 = new JMenuItem("Joueurs", KeyEvent.VK_J);
        jMenuItem8.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
                InputEvent.ALT_DOWN_MASK));
        jMenu2.add(jMenuItem8);
        jMenuBar1.add(jMenu2);

        JMenu jMenu5 = new JMenu("Aide");
        jMenu5.setMnemonic(KeyEvent.VK_A);

        JMenuItem jMenuItem10 = new JMenuItem("Aide", KeyEvent.VK_A);
        jMenuItem10.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                InputEvent.ALT_DOWN_MASK));
        jMenuItem10.setIcon(new ImageIcon("./src/Images/icoAide.gif"));
        jMenu5.add(jMenuItem10);

        jMenuBar1.add(jMenu5);
        JMenuItem jMenuItem9 = new JMenuItem("About", KeyEvent.VK_A);
        jMenuItem9.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.ALT_DOWN_MASK));
        jMenuItem9.setIcon(new ImageIcon("./src/Images/icoAbout.gif"));
        jMenu5.add(jMenuItem9);

        setJMenuBar(jMenuBar1);
        /*----------------------------FIN de 1.2------------------------------*/
        /*1.3 - Les listener necessaire aux jMenuItem*/
        jMenuItem1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.nouvellePartie();
            }
        });

        jMenuItem3_1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.changerJeu(0);
            }
        });

        jMenuItem3_2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.changerJeu(1);
            }
        });

        jMenuItem3_3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.changerJeu(2);
            }
        });

        jMenuItem3_4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.changerJeu(3);
            }
        });

        jMenuItem4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        jMenuItem5.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                apparence(1);
            }
        });

        jMenuItem6.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                apparence(2);
            }
        });

        jMenuItem7.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                apparence(3);
            }
        });

        jMenuItem8.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.changerJoueurs();
            }
        });

        jMenuItem9.addActionListener(new JMenuAbout());

        jMenuItem10.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                help.setVisible(true);
            }
        });

        jMenuItem11.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                sequence.annuleCoup();
            }
        });
        /*----------------------------FIN de 1.3------------------------------*/
        /*1.4 - Les composants pour la barre d'etat*/
        JPanel jPanel1 = new JPanel(new GridLayout(1, 2, 10, 5));
        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jLabel1.setFont(petitf);
        jPanel1.add(jLabel1);

        jLabel2 = new JLabel();
        jLabel2.setHorizontalAlignment(JLabel.RIGHT);
        jLabel2.setFont(petitf);
        jPanel1.add(jLabel2);
        add(jPanel1, BorderLayout.SOUTH);
        /*----------------------------FIN de 1.4------------------------------*/
        /*1.5 - Les composants internes de la JFrame principale*/
        JPanel jPanel2 = new JPanel(new BorderLayout());
        /*1.5.1 - Les composants d'informations*/
        JPanel jPanel3 = new JPanel(null);
        jPanel3.setPreferredSize(new java.awt.Dimension(150, 410));
        TitledBorder tb = new TitledBorder(new javax.swing.border.EtchedBorder(),
                "Jeu en cours :", 0, TitledBorder.ABOVE_TOP, grasf);
        jLabel3 = new JLabel("Aucun");
        jLabel3.setFont(petitf);
        jLabel3.setBorder(tb);
        jLabel3.setBounds(1, 5, 148, 40);
        jPanel3.add(jLabel3);
        tb = new TitledBorder(new javax.swing.border.EtchedBorder(),
                "Joueur :", 0, TitledBorder.ABOVE_TOP, grasf);
        jLabel4 = new JLabel("-");
        jLabel4.setFont(petitf);
        jLabel4.setBorder(tb);
        jLabel4.setBounds(1, 50, 148, 40);
        jPanel3.add(jLabel4);
        tb = new TitledBorder(new javax.swing.border.EtchedBorder(),
                "Scores :", 0, TitledBorder.ABOVE_TOP, grasf);
        JPanel jPanel5 = new JPanel(new GridLayout(2, 1));
        jPanel5.setBorder(tb);
        jLabel5 = new JLabel();
        jLabel5.setFont(new Font("Default", Font.PLAIN, 10));
        jPanel5.add(jLabel5);
        jLabel6 = new JLabel();
        jLabel6.setFont(new Font("Default", Font.PLAIN, 10));
        jPanel5.add(jLabel6);
        jPanel5.setBounds(1, 95, 148, 58);
        jPanel3.add(jPanel5);
        JButton jButton1 = new JButton("Nouvelle Partie");
        jButton1.setBounds(15, 175, 120, 25);
        jButton1.setFont(petitf);
        jPanel3.add(jButton1);
        JButton jButton2 = new JButton("Joueurs");
        jButton2.setBounds(34, 205, 82, 25);
        jButton2.setFont(petitf);
        jPanel3.add(jButton2);
        jButton3 = new JButton("Jouer !");
        jButton3.setBounds(34, 235, 82, 25);
        jButton3.setFont(petitf);
        jButton3.setEnabled(false);
        jPanel3.add(jButton3);
        jLog = new JList();
        jLog.setFont(petitf);
        jLog.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tb = new TitledBorder(null, "Log :", 0, TitledBorder.ABOVE_TOP, grasf);
        final JScrollPane jsp = new JScrollPane(jLog);
        jsp.setBorder(tb);
        int policy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
        jsp.setHorizontalScrollBarPolicy(policy);
        jsp.setBounds(1, 270, 148, 150);
        jsp.setVisible(false);
        jPanel3.add(jsp);
        jPanel2.add(jPanel3, BorderLayout.WEST);
        /*----------------------------FIN de 1.5.1------------------------*/
        /*1.5.1b - JPanel supplementaire (Pour avoir taille dynamique)*/
        JPanel jPanelSup = new JPanel(new BorderLayout());
        /*----------------------------FIN de 1.5.1b-----------------------*/
        /*1.5.2 - Le separateur*/
        JSeparator jSeparator4 = new JSeparator();
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanelSup.add(jSeparator4, BorderLayout.WEST);
        /*----------------------------FIN de 1.5.2------------------------*/
        /*1.5.3 - Le panel qui contiendra l'affichage*/
        jPanel6 = new JPanel(new GridLayout(1, 1));
        jPanel6.add(new JLabel(new ImageIcon("./src/Images/first.gif")));
        jPanel4 = new JPanel(new BorderLayout());
        jPanel4.setPreferredSize(new java.awt.Dimension(550, 400));
        jPanelSup.add(jPanel4, BorderLayout.CENTER);
        jPanel2.add(jPanelSup, BorderLayout.CENTER);
        /*1.5.3b - Les marges pour le maintient du plateau carre*/
        marge_nord = new JPanel();
        jPanel4.add(marge_nord, BorderLayout.NORTH);
        marge_sud = new JPanel();
        jPanel4.add(marge_sud, BorderLayout.SOUTH);
        marge_ouest = new JPanel();
        jPanel4.add(marge_ouest, BorderLayout.WEST);
        marge_est = new JPanel();
        jPanel4.add(marge_est, BorderLayout.EAST);
        jPanel4.add(jPanel6, BorderLayout.CENTER);
        jPanel4.addComponentListener(square);
        /*---------------------------FIN de 1.5.3b----------------------------*/
        /*---------------------------FIN de 1.5.3-----------------------------*/
        add(jPanel2, BorderLayout.CENTER);
        /*----------------------------FIN de 1.5------------------------------*/
        /*1.6 - Les Listener pour les boutons*/
        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.nouvellePartie();
            }
        });

        jButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.changerJoueurs();
            }
        });

        triche.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                sequence.activeTriche(e.getStateChange());
            }
        });

        historique.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    jsp.setVisible(true);
                } else {
                    jsp.setVisible(false);
                }
            }
        });

        wideSc.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    jPanel4.removeComponentListener(square);
                    plein_ecran();
                } else {
                    jPanel4.addComponentListener(square);
                    ComponentEvent ce = new ComponentEvent(jPanel4, 101);
                    square.componentResized(ce);
                }
            }
        });
        /*----------------------------FIN de 1.6------------------------------*/
    }

    /**
     *  Cette methode permet d'annuler les marges faites pour le mode
     * "Plein ecran", qui ne necessite aucune marge.
     */
    private void plein_ecran() {
        Dimension toAdjust = new Dimension(0, 0);
        marge_ouest.setPreferredSize(toAdjust);
        marge_ouest.setSize(toAdjust);
        marge_est.setPreferredSize(toAdjust);
        marge_est.setSize(toAdjust);
        marge_nord.setPreferredSize(toAdjust);
        marge_nord.setSize(toAdjust);
        marge_sud.setPreferredSize(toAdjust);
        marge_sud.setSize(toAdjust);
    }

    /**
     *  Cette methode est appelee par les <b>JMenuItem</b> concernant
     * l'apparence selon le choix effectue, l'action appelle cette methode en
     * fournissant le parametre identifiant le LookAndFeel a faire apparaitre.
     *
     * @param choix LookAndFeel a affiche.
     */
    private void apparence(int choix) {
        try {
            String laf = "", mod = "";
            switch (choix) {
                case 1:
                    laf = "javax.swing.plaf.metal.MetalLookAndFeel";
                    mod = "Metal";
                    break;
                case 2:
                    laf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
                    mod = "Nimbus";
                    break;
                default:
                    laf = UIManager.getSystemLookAndFeelClassName();
                    mod = "OS";
                    break;
            }
            sequence.changerEtat(new String[]{"Apparence " + mod + " choisie", "3"});
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "Le theme demande n'est pas supporte.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "La classe pour la nouvelle apparence non trouvee.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "La classe necessaire au changement n'est pas instanciable.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du changement!\n"
                    + "Le programme n'arrive pas a acceder a votre demande.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        java.awt.Window[] windows = getWindows();
        for (int i = 0; i < windows.length; i++) {
            SwingUtilities.updateComponentTreeUI(windows[i]);
            windows[i].pack();
        }
    }

    /**
     *  Cette classe permet de creer les marges de bonnes dimensions pour que
     * les plateaux de jeux restent carre.
     */
    class FabriqueMarges implements ComponentListener {

        /**
         *  Cette methode met a jout les marges necessaires pour garder le
         * plateau de jeu carre.
         *
         * @param ce Evenement genere par le composant auquel il est attache.
         */
        @Override
        public void componentResized(ComponentEvent ce) {
            int min = Math.min(jPanel4.getHeight(), jPanel4.getWidth());
            if (min == jPanel4.getHeight()) {
                int max = jPanel4.getWidth();
                int rest = (max - min) / 2;
                Dimension dim = new Dimension(rest, min - 2 * (marge_sud.getHeight()));
                marge_ouest.setPreferredSize(dim);
                marge_ouest.setSize(dim);
                marge_est.setPreferredSize(dim);
                marge_est.setSize(dim);
                Dimension nul = new Dimension(min, 0);
                marge_nord.setPreferredSize(nul);
                marge_nord.setSize(nul);
                marge_sud.setPreferredSize(nul);
                marge_sud.setSize(nul);
            } else {
                int max = jPanel4.getHeight();
                int rest = (max - min) / 2;
                Dimension dim = new Dimension(min, rest);
                marge_nord.setPreferredSize(dim);
                marge_nord.setSize(dim);
                marge_sud.setPreferredSize(dim);
                marge_sud.setSize(dim);
                Dimension nul = new Dimension(0, min - 2 * (marge_sud.getHeight()));
                marge_ouest.setPreferredSize(nul);
                marge_ouest.setSize(nul);
                marge_est.setPreferredSize(nul);
                marge_est.setSize(nul);
            }
        }

        /**
         *  Methode vide.
         *
         * @param ce Evenement genere par le composant auquel il est attache.
         */
        @Override
        public void componentMoved(ComponentEvent ce) {
        }

        /**
         *  Methode vide.
         *
         * @param ce Evenement genere par le composant auquel il est attache.
         */
        @Override
        public void componentShown(ComponentEvent ce) {
        }

        /**
         *  Methode vide.
         *
         * @param ce Evenement genere par le composant auquel il est attache.
         */
        @Override
        public void componentHidden(ComponentEvent ce) {
        }
    }
}

