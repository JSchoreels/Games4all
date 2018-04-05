import GUI.Lanceur;
/**
 *  Cette classe permet de demarrer le projet, avec l'interface graphique.
 * 
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.0
 */
public class Demarreur {

    /**
     *  Il s'agit de la methode principale qui permet de faire executer des
     * instructions a un programme. Ici, cette methode fait demarrer le jeu
     * tout entier.
     * 
     * @param args parametres optionnels pouvant etre fourni. Mais dans ce cas,
     * ne sert a rien.
     */
    public static void main(String[] args){
        new Lanceur();
    }
}