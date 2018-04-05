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
 * le jeu du Morpion.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.1
 * @see Affichage
 */
public class Affichage_Morpion extends Affichage {

    /**
     *  Constructeur d'<i>Affichage_Morpion</i>, qui va initialiser la classe
     * mere et les images necessaires au jeu, et en fait une copie.
     *
     * @param panel JPanel sur lequel le jeu sera affiche.
     */
    public Affichage_Morpion(JPanel panel) {
        super(panel);

        vide = new ImageIcon("./src/GUI/Affichage/Morpion/vide.gif");

        pion = new ImageIcon[2];
        pion[0] = new ImageIcon("./src/GUI/Affichage/Morpion/o.gif");
        pion[1] = new ImageIcon("./src/GUI/Affichage/Morpion/x.gif");

        bordure = new ImageIcon[0];
        
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
        int lignes = plateau.obtenirNombreLignes();
        int cols = plateau.obtenirNombreColonnes();
        current.setLayout(new java.awt.GridLayout(lignes, cols));
        /*-----------------------------Fin de 1.0-----------------------------*/
        /*1.1 - Mise en place du jeu et décor sur le coté*/
        display = new JLabel[lignes][cols];
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < cols; j++) {
                display[i][j] = new JLabel(vide);
                current.add(display[i][j]);
            }
        }
        /*-----------------------------Fin de 1.1-----------------------------*/
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
                double tailleX = (current.getWidth() / (display[0].length));
                double tailleY = (current.getHeight() / (display.length));
                int x = (int) (pt.getX() / tailleX);
                int y = (int) (pt.getY() / tailleY);
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
