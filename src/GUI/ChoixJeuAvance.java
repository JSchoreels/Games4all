package GUI;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

/**
 *  Cette classe permet de demander a l'utilisateur le jeu auquel il souhaite
 * jouer, tout en lui proposant certaines variantes pour certains jeux, et en
 * lui demandant s'il souhaite continuer avec les meme joueurs (s'ils ont deja
 * ete crees) ou de les modifier.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 2.1
 */
public class ChoixJeuAvance extends JDialog {

    private Sequenceur seq;
    private Font petitf = new Font("Default", Font.PLAIN, 11);
    private Font grasf = new Font("Default", Font.BOLD, 10);
    private JButton non;
    private JLabel question;
    private JRadioButton radio1, radio2, radio3;
    private JSlider slider1;
    private JPanel taille;
    private int type;

    /**
     *  Constructeur permettant de d'initialiser et de sauver en champ
     * <b>seq</b> l'instance existente du sequenceur.
     *
     * @param seq Instance du <b>Sequenceur</b> utilise dans le Lanceur.
     */
    public ChoixJeuAvance(Sequenceur seq) {
        this.seq = seq;
        seq.statutDialogues(true);
        initialise();
        setVisible(false);
    }

    /**
     *  Cette methode initialise tous les composants de la fenetre de Dialog,
     * fournit leur(s) Listener(s) et affiche la fenetre.
     */
    private void initialise() {
        /*1.1 - Parametres de la JDialog*/
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Choix jeu - avance");
        Image icon = (new ImageIcon("./src/Images/icoAvance.gif")).getImage();
        setIconImage(icon);
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(300, 225));
        setLocationRelativeTo(rootPane);
        setResizable(false);
        setLayout(null);
        /*---------------------------FIN de 1.1-------------------------------*/
        /*1.2 - Ajout des objets*/
        /*1.2.1 - Ajout des radioButton*/
        javax.swing.ButtonGroup bg = new javax.swing.ButtonGroup();
        radio1 = new JRadioButton("Morpion");
        radio1.setFont(petitf);
        bg.add(radio1);
        radio2 = new JRadioButton("Puissance4");
        radio2.setFont(petitf);
        bg.add(radio2);
        radio3 = new JRadioButton("Othello");
        radio3.setFont(petitf);
        bg.add(radio3);

        TitledBorder tb = new TitledBorder(new javax.swing.border.EtchedBorder(),
                "Jeux :", 0, TitledBorder.ABOVE_TOP, grasf);
        JPanel choix = new JPanel(new GridLayout(1, 3));
        choix.setBorder(tb);
        choix.add(radio1);
        choix.add(radio2);
        choix.add(radio3);
        choix.setBounds(2, 2, 292, 45);
        add(choix);
        /*--------------------------FIN de 1.2.1------------------------------*/
        /*1.2.2 - Ajout des slider*/
        tb = new TitledBorder(null,
                "Taille de la grille :",
                0,
                TitledBorder.ABOVE_TOP, grasf);
        taille = new JPanel(new GridLayout(1, 2));
        taille.setBorder(tb);
        taille.add(new JLabel("nombre de lignes :"));

        slider1 = new JSlider(2, 10, 3);
        slider1.setMinorTickSpacing(1);
        slider1.setPaintTicks(true);
        slider1.setSnapToTicks(true);
        slider1.setPaintLabels(true);
        slider1.setFont(petitf);

        taille.add(slider1);
        taille.setBounds(2, 50, 291, 85);
        add(taille);
        /*--------------------------FIN de 1.2.2------------------------------*/
        /*1.2.3 - Ajout du dialogue*/
        JPanel dialog = new JPanel(new GridLayout(2, 1));
        JPanel confirm = new JPanel(new GridLayout(1, 2));
        JButton oui = new JButton("Oui");
        oui.setFont(petitf);
        non = new JButton("annuler");
        non.setFont(petitf);
        question = new JLabel("Aller configurer les joueurs ?");
        dialog.add(question);
        confirm.add(non);
        confirm.add(oui);
        dialog.add(confirm);
        dialog.setBounds(2, 140, 291, 50);
        add(dialog);
        /*--------------------------FIN de 1.2.3------------------------------*/
        /*---------------------------FIN de 1.2-------------------------------*/
        /*1.3 - Ajout des Listener*/
        /*1.3.1 - Listener radio*/
        radio1.addActionListener(new Radio1Listener());
        radio2.addActionListener(new Radio2Listener());
        radio3.addActionListener(new Radio3Listener());
        /*--------------------------FIN de 1.3.1------------------------------*/
        /*1.3.2 - Listener boutons*/
        non.addActionListener(new NonListener());
        oui.addActionListener(new OuiListener());
        /*--------------------------FIN de 1.3.2------------------------------*/
        /*---------------------------FIN de 1.3-------------------------------*/
        radio1.setSelected(true);
        java.util.Hashtable ht = slider1.createStandardLabels(1, 2);
        slider1.setLabelTable(ht);
    }

    /**
     *  Cette methode permet de garder la <b>DefaultCloseOperation</b> sur
     * HIDE_ON_CLOSE, car il utilise la methode Override, et donc signale
     * bien au sequenceur que le Dialog s'est arrete.
     */
    @Override
    public void setVisible(boolean on) {
        seq.statutDialogues(on);
        super.setVisible(on);
        if(on && seq.obtenirJoueurs() != null){
            non.setText("non");
            question.setText("Souhaitez-vous continuer avec ces joueurs ?");
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>radio1</b>. Elle permet de mettre a jour les composants de la fenetre.
     */
    class Radio1Listener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            type = 0;
            slider1.setEnabled(true);
            slider1.setMinimum(2);
            slider1.setMaximum(10);
            slider1.setMinorTickSpacing(1);
            slider1.setValue(3);
            java.util.Hashtable ht = slider1.createStandardLabels(1, 2);
            slider1.setLabelTable(ht);
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>radio2</b>. Elle permet de mettre a jour les composants de la fenetre.
     */
    class Radio2Listener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            type = 1;
            slider1.setEnabled(false);
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>radio3</b>. Elle permet de mettre a jour les composants de la fenetre.
     */
    class Radio3Listener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            type = 2;
            slider1.setEnabled(true);
            slider1.setMinimum(4);
            slider1.setMaximum(12);
            slider1.setMinorTickSpacing(2);
            slider1.setValue(8);
            java.util.Hashtable ht = slider1.createStandardLabels(2, 4);
            slider1.setLabelTable(ht);
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>non</b>. Elle permet de reagir selon la reponse du joueur.
     */
    class NonListener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         *
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            if (seq.obtenirJoueurs() != null) {
                seq.changerTypeEtTaille(type, slider1.getValue());
                seq.clorePartie();
                seq.changerJoueurs();
                seq.changerEtat(new String[]{"Partie preparee", "5"});
            } else {
                seq.changerEtat(new String[]{"Nouveau jeu annule", "5"});
            }
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>oui</b>. Elle permet de reagir selon la reponse du joueur.
     */
    class OuiListener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         * 
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            seq.changerTypeEtTaille(type, slider1.getValue());

            if (seq.obtenirJoueurs() != null) {
                seq.majJoueurs();
                seq.nouvellePartie();
            } else {
                seq.changerJoueurs();
            }

            seq.changerEtat(new String[]{"Partie preparee", "5"});
        }
    }
}
