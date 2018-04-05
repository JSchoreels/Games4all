package GUI.Affichage;

import Composants.Coup;
import Composants.IPlateau;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *  Cette classe permet de gerer l'affichage des jeux sur l'interface graphique,
 * de maniere generique, c'est-a-dire en ne connaissant que les methodes de
 * cette classe.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.2
 * @see Affichage_Morpion
 * @see Affichage_Puissance4
 * @see Affichage_Othello
 */
public abstract class Affichage {

    protected JLabel[][] display;
    protected JPanel current;
    protected Coup where = new Coup(-1, -1);
    protected ComponentListener CompLis = new RenduIcones();
    protected MouseListener ml;
    protected ArrayList<Coup> toLight;
    protected ImageIcon vide, videguenuine;
    protected ImageIcon[] pion, pionguenuine, bordure, bordureguenuine;

    /**
     *  Constructeur d'<i>Affichage</i>, va enregistrer le panel dans lequel il
     * va devoir afficher et prepare ce panel pour le redimensionnement d'image.
     *
     * @param panel JPanel sur lequel le jeu s'affichera.
     */
    public Affichage(JPanel panel) {
        current = panel;
    }

    /**
     *  Cette methode permet de recuperer le dernier <b>Coup</b>, ou le joueur
     * a clique avec sa souris.
     *
     * @return le <b>Coup</b> ou le joueur a clique en dernier.
     */
    public Coup obtenirOu() {
        return where;
    }

    /**
     *  Cette méthode permet d'illuminer d'un halo rouge, les cases du jeu en
     * cours, sur lesquelles le joueur peut jouer a ce moment-la de la partie.
     *
     * @param toDo Liste de <b>Coup</b> a entourer d'un halo rouge.
     */
    public void afficherHalo(ArrayList<Coup> toDo) {
        eteindreHalo();
        if (toDo != null) {
            toLight = toDo;
            javax.swing.border.LineBorder lb =
                    new javax.swing.border.LineBorder(java.awt.Color.RED, 1, true);
            for (int i = 0; i < toLight.size(); i++) {
                int li = toLight.get(i).obtenirLigne();
                int co = toLight.get(i).obtenirColonne();
                display[li][co].setBorder(lb);
            }
        }
    }

    /**
     *  Cette methode permet supprimer la bordure lumineuse produite par la
     * methode <b>afficherHalo</b>, en allant rechercher la liste de <b>Coup</b>
     * qui ont ete precedemment entoures.
     */
    public void eteindreHalo() {
        if (toLight != null) {
            for (int i = 0; i < toLight.size(); i++) {
                int li = toLight.get(i).obtenirLigne();
                int co = toLight.get(i).obtenirColonne();
                display[li][co].setBorder(null);
            }
            toLight = null;
        }
    }

    /**
     *  Cette methode est appelee dans le constructeur des classes qui etendent
     * celle-ci. Elle copie toutes les images utilisees pour l'affichage du jeu,
     * et permet d'en garder une copie intacte. Car lors du redimmensionnement,
     * la perte de qualite est obligatoire si l'on travaille sur les images qui
     * sont redimmensionnees.
     */
    protected void clonerSources() {
        videguenuine = new ImageIcon(vide.getImage());
        pionguenuine = pion.clone();
        for (int i = 0; i < pion.length; i++) {
            pionguenuine[i] = new ImageIcon(pion[i].getImage());
        }
        bordureguenuine = bordure.clone();
        for (int i = 0; i < bordure.length; i++) {
            bordureguenuine[i] = new ImageIcon(bordure[i].getImage());
        }
    }

    /**
     *  Cette methode est appelee lors du redimensionnement du panel de jeu.
     * Elle met a jour la taille de toutes les images selon la nouvelle taille
     * du panel.
     */
    protected void echelleAuto() {
        int toback = 0;
        if (bordure.length != 0) {
            toback = 2;
        }
        int width = current.getWidth() / (display[0].length + toback);
        int height = current.getHeight() / (display.length + toback);

        Image newVide = echelle(videguenuine.getImage(), width, height);
        vide.setImage(newVide);

        for (int i = 0; i < bordure.length; i++) {
            Image newBordure = echelle(bordureguenuine[i].getImage(), width, height);
            bordure[i].setImage(newBordure);
        }

        for (int i = 0; i < pion.length; i++) {
            Image newBordure = echelle(pionguenuine[i].getImage(), width, height);
            pion[i].setImage(newBordure);
        }
        current.repaint();
    }

    /**
     *  Cette methode est uniquement appelee par la methode <b>echelleAuto</b>,
     * elle utilise l'image source, et les nouvelles tailles, pour redessiner
     * les images a la taille convenue.
     *
     * @param source L'image sur laquelle on travaille.
     * @param width La nouvelle largeur de l'image.
     * @param height La nouvelle hauteur de l'image.
     * @return Une <b>Image</b> redimensionnee de celle fournie en parametre.
     */
    private Image echelle(Image source, int width, int height) {
        int argb = BufferedImage.TYPE_INT_ARGB;
        BufferedImage buffer = new BufferedImage(width, height, argb);
        Graphics2D g = buffer.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, 0, 0, width, height, null);
        g.dispose();

        return buffer;
    }

    /**
     * Il s'agit de la methode standard pour reimprimer tout le plateau de jeu.
     *
     * @param plateau Le plateau de jeu sur lequel on prend les informations
     * pour afficher l'image qui convient.
     */
    public void toutActualiser(IPlateau plateau) {
        for (int i = 0; i < plateau.obtenirNombreLignes(); i++) {
            for (int j = 0; j < plateau.obtenirNombreColonnes(); j++) {
                int jeton = plateau.obtenirNumJoueur(i, j);
                if (jeton != plateau.AUCUN_JOUEUR) {
                    display[i][j].setIcon(pion[jeton]);
                } else {
                    display[i][j].setIcon(vide);
                }
            }
        }
    }

    /**
     *  Cette methode retourne le ComponentListener utilisé pour mettre a jour
     * la taille des icones.
     * 
     * @return ComponentListener utilise pour mettre a jour les icones
     */
    public ComponentListener obtenirListenerEchelle(){
        return CompLis;
    }


    /**
     * La classe <i>RenduIcones</i> implemente <b>ComponentListener</b> car
     * elle est utilisee pour le redimensionnement des images, lorsque l'objet
     * sur lequel elle est utilisee est redimensionnee.
     */
    class RenduIcones implements ComponentListener {

        /**
         *  Cette methode actionne le redimensionnement des images.
         * 
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentResized(ComponentEvent e) {
            echelleAuto();
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
         *  Methode vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentShown(ComponentEvent e) {
        }

        /**
         *  Methode vide.
         *
         * @param e Type d'evenement genere par le Composant sur lequel il est
         * applique.
         */
        @Override
        public void componentHidden(ComponentEvent e) {
        }
    }

    /**
     * Cette methode est utilise pour initialise l'affichage une premiere fois.
     * L'implementation varie selon le jeu et le decor qui l'entoure.
     *
     * @param plateau Instance du jeu a partir de laquelle on peut acceder a
     * diverses ressources du jeu.
     */
    public abstract void initialise(IPlateau plateau);

    /**
     * Cette methode est utilise pour actualiser l'affichage de la maniere la
     * plus econome possible.
     * L'implementation varie selon le jeu.
     * 
     * @param plateau Instance du jeu à partir duquel il est possible d'acceder
     * aux informations concernant l'etat actuel du jeu.
     * @param tour Donne l'index du joueur en cours.(Index signifie "jeton")
     */
    public abstract void actualise(IPlateau plateau, int tour);

    /**
     * Cette methode permet de donner le Listener qui calcule le <b>Coup</b>
     * selon le clic sur le panel de jeu.
     * L'implementation varie selon le jeu.
     *
     * @return Le MouseListener adequat au jeu en cours.
     */
    public abstract MouseListener obtenirListener();
}
