package Composants;

import java.text.SimpleDateFormat;

/**
 *  Cette classe permet de sauver une action precise parmis plusieurs types,
 * et de pouvoir en redonner une description plus ou moins detaillee.
 * Cette classe est immuable, une fois une instance de Log creee, elle ne peut
 * pas etre modifiee.
 *
 * @author Jonathan Schoreels & Alexandre Devaux
 * @version 1.0
 */
public class Log {

    private String date, joueur, fact;
    private final int type;

    /**
     *  Constructeur de la classe pour enregistrer un Coup joue sur le plateau.
     *
     * @param joueur Le joueur ayant place un jeton sur le plateau.
     * @param coup L'endroit ou le jeton a ete place.
     */
    public Log(String joueur, Coup coup) {
        type = 0;
        this.joueur = joueur;
        fact = "(" + coup.obtenirLigne() + "," + coup.obtenirColonne() + ")";
        time();
    }

    /**
     *  Constructeur de la classe pour enregistrer un evenement autre que le
     * Coup d'un joueur sur le plateau.
     *
     * @param type Le type d'evenement a enregistrer.
     */
    public Log(int type) {
        this.type = type;
        time();
    }

    /**
     *  Cette methode s'occupe d'enregistrer dans le champ <b>date</b>,
     * l'instant de la creation du <i>Log</i>, donc de l'evenement.
     */
    private void time() {
        long current = System.currentTimeMillis();
        java.util.Date creation = new java.util.Date(current);
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd MMMMM yyyy - kk:mm:ss");
        date = format.format(creation);
    }

    /**
     *  Cette methode convertit l'instance de <i>Log</i> actuelle en une
     * representation compacte de l'evenement.
     *
     * @return Un String representant l'instance actuelle, sans detail.
     */
    @Override
    public String toString() {
        switch (type) {
            case 0:
                return joueur + " - " + fact;
            case 1:
                return "Nouveau Morpion";
            case 2:
                return "Nouveau Puissance4";
            case 3:
                return "Nouveau Othello";
            default:
                return "Fin de partie";
        }
    }

    /**
     *  Cette methode convertit l'instance de <i>Log</i> actuelle en une
     * representation complete de l'evenement.
     * 
     * @return Un String representant l'instance actuelle, avec details.
     */
    public String getFull() {
        switch (type) {
            case 0:
                return date + "\n" + joueur + " - " + fact;
            case 1:
                return date + "\n" + "Nouvelle partie de Morpion";
            case 2:
                return date + "\n" + "Nouvelle partie de Puissace 4";
            case 3:
                return date + "\n" + "Nouvelle partie d'Othello";
            default:
                return date + "\n" + "Fin de partie";
        }
    }
}
