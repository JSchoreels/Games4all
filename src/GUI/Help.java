package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *  Cette classe affiche une fenetre avec l'aide du programme, elle permet de
 * naviguer parmi les differents documents d'aide.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.0
 */
public class Help extends JFrame {

    private URL temp, first;
    private int index = 0;
    private JPanel navigate;
    private JEditorPane display;
    private JButton prec = new JButton("precedent", 
            new ImageIcon("./src/Images/prec.gif"));
    private JButton suiv = new JButton("suivant",
            new ImageIcon("./src/Images/suiv.gif"));
    private JButton home = new JButton("Home");
    private ArrayList<URL> pile = new ArrayList<URL>();

    /**
     *  Constructeur fait directement appel a la methode d'initialisation.
     */
    public Help() {
        initialise();
        setVisible(false);
    }

    /**
     *  Cette methode initialise tous les composants de la fenetre d'aide,
     * fournit leur(s) Listener(s) et affiche la fenetre.
     */
    private void initialise() {
        /* 1.0 Initialisation des parametres de la Frame */
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Aide");
        Image icon = (new ImageIcon("./src/Images/icoAide.gif")).getImage();
        setIconImage(icon);
        setAlwaysOnTop(true);
        Dimension dim =
                java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(dim.width / 3, 3 * (dim.height / 5)));
        setPreferredSize(new Dimension(dim.width / 3, 3 * (dim.height / 5)));
        setLocation(2 * (dim.width / 3), dim.height / 5);
        setLayout(new BorderLayout());
        /*------------------------------Fin de 1.0----------------------------*/
        /* 1.2 Ajout des composants de la fenetre */
        navigate = new JPanel();
        navigate.add(prec);
        navigate.add(home);
        suiv.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        navigate.add(suiv);
        navigate.setBorder(new javax.swing.border.EtchedBorder());
        navigate.setVisible(true);
        add(navigate, BorderLayout.NORTH);

        display = new JEditorPane();
        display.setEditable(false);
        try {
            first = new URL("file:./src/Composants/Help/Help2.htm");
            pile.add(first);
            display.setPage(pile.get(index));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.toString());
        }

        JScrollPane scroll = new JScrollPane(display);
        display.setPreferredSize(display.getSize());
        add(scroll, BorderLayout.CENTER);
        /*------------------------------Fin de 1.2----------------------------*/
        /* 1.3 Ajout des Listener */
        prec.addActionListener(new PrecedentListener());

        suiv.addActionListener(new SuivantListener());

        home.addActionListener(new AccueilListener());

        display.addMouseListener(new ListenerAffichage());

        display.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                temp = e.getURL();
            }
        });
        /*------------------------------Fin de 1.3----------------------------*/
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>prec</b>. Elle permet de revenir sur les pages precedentes.
     */
    class PrecedentListener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (index > 0) {
                try {
                    index--;
                    display.setPage(pile.get(index));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Une erreur s'est produite lors de la recuperation "
                            + "de la page precedente. Cette page ne peut donc "
                            + "etre affichee.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>suiv</b>. Elle permet de revenir sur les pages suivantes.
     */
    class SuivantListener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (index < pile.size() - 1) {
                try {
                    index++;
                    display.setPage(pile.get(index));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Une erreur s'est produite lors de la recuperation "
                            + "de la page suivante. Cette page ne peut donc "
                            + "etre affichee.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     *  Cette classe permet d'ajouter une Action lors du clic sur le bouton
     * <b>home</b>. Elle permet de revenir a la page d'accueil.
     */
    class AccueilListener implements ActionListener {

        /**
         *  Cette methode est appelee lorsqu'une Action est executee.
         * @param e Evenement genere par une Action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                temp = new URL("file:./src/Composants/Help/Help2.htm");
                if (temp != null && !temp.equals(pile.get(index))) {
                    if (index != pile.size() - 1) {
                        for (int i = pile.size() - 1; i > index; i--) {
                            pile.remove(i);
                        }
                    }
                    index++;
                    pile.add(temp);
                    display.setPage(temp);
                }
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Suite a une erreur dans l'URL du document, l'aide "
                        + "ne peut afficher la page d'accueil.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null,
                        "Une erreur s'est produite lors de la recuperation "
                        + "de la page d'accueil. Cette page ne peut donc "
                        + "etre affichee.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     *  Cette classe permet d'ecouter les clics sur le document en cours de
     * visualisation, et de faire naviguer si l'evenement le permet.
     */
    class ListenerAffichage implements MouseListener {

        /**
         *  Methode vide.
         *
         * @param e Evenement genere par la souris.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         *  Methode vide.
         *
         * @param e Evenement genere par la souris.
         */
        @Override
        public void mousePressed(MouseEvent e) {
        }

        /**
         *  Cette methode fait afficher le document que le lien mis en cache
         * dans <b>temp</b> indique.
         *
         * @param e Evenement genere par la souris.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            try {
                if (temp != null && !temp.equals(pile.get(index))) {
                    if (index != pile.size() - 1) {
                        for (int i = pile.size() - 1; i > index; i--) {
                            pile.remove(i);
                        }
                    }
                    index++;
                    pile.add(temp);
                    display.setPage(temp);
                    temp = null;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Une erreur s'est produite lors de la recuperation "
                        + "de la page que vous souhaitez. Cette page ne "
                        + "peut donc etre affichee.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         *  Methode vide.
         *
         * @param e Evenement genere par la souris.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         *  Methode vide.
         *
         * @param e Evenement genere par la souris.
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
