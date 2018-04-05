package GUI.Etat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JLabel;

/**
 *  Cette classe permet d'afficher des messages dans la barre d'etat, pendant
 * un temps determine ou jusqu'a ce qu'il soit remplace.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.0
 */
public class Etat {

    private String[] etat;
    private JLabel jetat;
    private Timer time;

    /**
     *  Constructeur d'<i>Etat</i>, initialise entre autre le <b>Timer</b> et le
     * demarre, enregistre le <b>JLabel</b> dans lequel afficher les
     * informations.
     *
     * @param label JLabel dans lequel les messages seront imprimes.
     * @param initialMessage Message a afficher a l'initialisation.
     */
    public Etat(JLabel label, String[] initialMessage) {
        etat = initialMessage;
        time = new Timer(1000, new Minuteur());
        jetat = label;
        jetat.setText(initialMessage[0]);
        time.start();
    }

    /**
     * Cette methode va changer le message affiche, par celui fournit, sans
     * attendre la fin du temps de l'affichage du message courant, s'il y en a
     * un.
     * 
     * @param etat Nouveau message a afficher.
     */
    public void changerEtat(String[] etat) {
        this.etat = etat;
        jetat.setText(etat[0]);
    }

    /**
     * Cette methode s'occupe de diminuer le temps lorsqu'il y a un message
     * affiche. Une fois le temps du message termine, il est simplement
     * efface.
     */
    class Minuteur implements ActionListener {

        /**
         * Cette methode est reveillee par le Timer, la methode
         * <b>actionPerformed</b> est alors appelee.
         * 
         * @param e Evenement genere par le Timer.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (etat != null) {
                int toReduce = Integer.parseInt(etat[1]);
                if (toReduce > 0) {
                    etat[1] = "" + (--toReduce);
                } else {
                    etat = null;
                    jetat.setText("-");
                }
            }
        }
    }
}
