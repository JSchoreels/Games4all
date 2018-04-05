package GUI.Affichage;

import Composants.Coup;
import Composants.IPlateau;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *  Il s'agit d'une implentation particuliere d'<i>Affichage</i>, concernant
 * le jeu du Puissance4.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.1
 * @see Affichage
 */
public class Affichage_Puissance4 extends Affichage {

    /**
     *  Constructeur d'<i>Affichage_Puissance4</i>, qui va initialiser la classe
     * mere et les images necessaires au jeu, et en fait une copie.
     *
     * @param pan JPanel sur lequel le jeu sera affiche.
     */
    public Affichage_Puissance4(JPanel pan) {
        super(pan);

        vide = new ImageIcon("./src/GUI/Affichage/Puissance4/vide.gif");

        pion = new ImageIcon[2];
        pion[0] = new ImageIcon("./src/GUI/Affichage/Puissance4/0.gif");
        pion[1] = new ImageIcon("./src/GUI/Affichage/Puissance4/1.gif");

        bordure = new ImageIcon[8];
        bordure[0] = new ImageIcon("./src/GUI/Affichage/Puissance4/bhg.gif");
        bordure[1] = new ImageIcon("./src/GUI/Affichage/Puissance4/bh.gif");
        bordure[2] = new ImageIcon("./src/GUI/Affichage/Puissance4/bhd.gif");
        bordure[3] = new ImageIcon("./src/GUI/Affichage/Puissance4/bg.gif");
        bordure[4] = new ImageIcon("./src/GUI/Affichage/Puissance4/bd.gif");
        bordure[5] = new ImageIcon("./src/GUI/Affichage/Puissance4/bbg.gif");
        bordure[6] = new ImageIcon("./src/GUI/Affichage/Puissance4/bb.gif");
        bordure[7] = new ImageIcon("./src/GUI/Affichage/Puissance4/bbd.gif");

        clonerSources();
    }

    /**
     *  Cette méthode initialise l'affichage du plateau de jeu dans l'interface
     * graphique.
     *
     * @param plateau Instance du jeu a partir de laquelle on peut acceder a
     * diverses ressources du jeu.
     */
    @Override
    public void initialise(IPlateau plateau) {
        /*1.0 - Initialisation des variables & données utiles*/
        current.setVisible(false);
        current.removeAll();
        int lignes = plateau.obtenirNombreLignes() + 1;
        int cols = plateau.obtenirNombreColonnes();
        current.setLayout(new java.awt.GridLayout(lignes + 2, cols + 2));
        /*-----------------------------Fin de 1.0-----------------------------*/
        for (int i = 0; i < (cols + 2); i++) {
            current.add(new JLabel());
        }
        /*1.1 - Mise en place du dessus du décor*/
        current.add(new JLabel(bordure[0]));

        for (int i = 0; i < cols; i++) {
            current.add(new JLabel(bordure[1]));
        }

        current.add(new JLabel(bordure[2]));
        /*-----------------------------Fin de 1.1-----------------------------*/
        /*1.2 - Mise en place du jeu et décor sur le coté*/
        display = new JLabel[lignes][cols];

        for (int i = 0; i < lignes-1; i++) {
            for (int j = 0; j < cols + 2; j++) {
                if (j == 0) {
                    current.add(new JLabel(bordure[3]));
                } else if (j == cols + 1) {
                    current.add(new JLabel(bordure[4]));
                } else {
                    display[i][j - 1] = new JLabel(vide);
                    display[i][j - 1].setDoubleBuffered(true);
                    current.add(display[i][j - 1]);
                }
            }
        }

        current.add(new JLabel(bordure[5]));

        for (int i = 1; i < cols + 1; i++) {
            current.add(new JLabel(bordure[6]));
        }

        current.add(new JLabel(bordure[7]));
        /*-----------------------------Fin de 1.2-----------------------------*/
        /*1.3 - Finalisation*/
        current.setVisible(true);
        echelleAuto();
        /*-----------------------------Fin de 1.3-----------------------------*/
    }

    /**
     *  Cette methode s'occupe de mettre a jour la representation graphique du
     * jeu.
     *
     * @param plateau Instance du jeu a partir de laquelle on peut acceder a
     * diverses ressources du jeu.
     * @param tour Donne l'index du joueur en cours.(Index signifie "jeton")
     */
    @Override
    public void actualise(IPlateau plateau, int tour) {
        Coup last = plateau.obtenirDernierCoup();
        int x = last.obtenirLigne();
        int y = last.obtenirColonne();
        /*for(int i = 0; i < x; i++){
        display[i][y].setIcon(pion[tour]);
        current.repaint();
        try{
        Thread.sleep(500);
        }catch(Exception e){}
        display[i][y].setIcon(vide);
        }*/
        display[x][y].setIcon(pion[tour]);
    }

    /**
     *  Cette methode delivre le MouseListener necessaire au calcul du clic.
     *
     * @return Le MouseListener adequat au Morpion.
     */
    @Override
    public MouseListener obtenirListener() {
        if (ml == null) {
            ml = new ListenerSouris();
        }
        return ml;
    }

    /**
     *  La classe <i>ListenerSouris</i> implemente <b>MouseListener</b> car
     * elle lui est necessaire pour calculer sur quelle case du jeu, le joueur
     * a clique.
     */
    class ListenerSouris implements MouseListener {

        /**
         *  Methode Vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         *  Elle calcule la case sur lequel le joueur a clique, et l'enregistre
         * dans le champ <b>where</b>. Si jamais l'utilisateur clic, mais qu'un
         * nullPointer est genere, alors un Coup impossible sera mis en place
         * dans <b>where</b>, de sorte que le clic soit invalidé.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            try {
                java.awt.Point pt = current.getMousePosition(true);
                double tailleX = (current.getWidth() / (display[0].length + 2));
                double tailleY = (current.getHeight() / (display.length + 2));
                int x = -1;
                int y = -1;
                if (pt.getX() >= tailleX) {
                    x = (int) ((pt.getX() - tailleX) / tailleX);
                }
                y = (int) (pt.getY() / tailleY);
                y = display.length - y;
                where = new Coup(y, x);
            } catch (NullPointerException ex) {
                where = new Coup(-1, -1);
            }
        }

        /**
         *  Methode Vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
        }

        /**
         *  Methode Vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         *  Methode Vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
