package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *  Cette classe qui implemente <b>ActionListener</b>, permet juste d'afficher
 * un <b>JOptionPane</b> affichant des informations concernant le programme.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.0
 */
public class JMenuAbout implements ActionListener {
    public String version = "version 1.0.0";

    /**
     * Cette methode est appelee lorsqu'une Action s'est produite.
     * 
     * @param e Evenement genere par le clic de la souris sur le
     * <b>JMenuItem</b> attache a ce <b>ActionListener</b>.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,
                "Games 4 all\n" + version + "\nProjet Informatique UMons 2010",
                "about",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
