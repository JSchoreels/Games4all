package Listener;

import GUI.Lanceur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

/**
 *  Cette classe qui implemente <b>Runnable</b> pour etre lancee dans un Thread
 * a part. Cette classe permet juste d'afficher la date et l'heure dans
 * un <b>JLabel</b>.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.0
 */
public class DateListener implements Runnable {

    private Date myTime;
    private Timer myTimer;
    private SimpleDateFormat Formatter;

    /**
     * Le constructeur initialise de nombreux outils qui seront utilises pour
     * l'affichage de la date et de l'heure. Le Timer viendra reveiller l'Action
     * qui mettra a jour, toutes les secondes, l'heure a afficher.
     */
    public DateListener() {
        myTime = new Date();
        Formatter = new SimpleDateFormat();
        Formatter.applyPattern("dd MMMMM yyyy - kk:mm:ss");
        myTimer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                myTime.setTime(System.currentTimeMillis());
                String current = Formatter.format(myTime);
                Lanceur.jLabel1.setText(current);
            }
        });
    }

    /**
     *  Cette methode met en marche le nouveau thread.
     */
    @Override
    public void run() {
        myTimer.start();
    }
}
