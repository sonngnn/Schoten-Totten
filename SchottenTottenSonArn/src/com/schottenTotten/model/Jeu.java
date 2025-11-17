package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private List<Borne> bornes;
    private Pioche pioche;
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurCourant;
    private int moveCount = 0; // Compteur de coups

    public Jeu(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurCourant = joueur1;
        this.pioche = new Pioche();
        initialiserBornes();
        distribuerCartesInitiales();
    }

    private void initialiserBornes() {
        bornes = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            bornes.add(new Borne(i, joueur1, joueur2));
        }
    }

    private void distribuerCartesInitiales() {
        for (int i = 0; i < 6; i++) {
            joueur1.ajouterCarteMain(pioche.piocherCarteClan());
            joueur2.ajouterCarteMain(pioche.piocherCarteClan());
        }
    }

    public List<Borne> getBornes() {
        return bornes;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public Pioche getPioche() {
        return pioche;
    }

    public void changerJoueurCourant() {
        if (joueurCourant.equals(joueur1)) {
            joueurCourant = joueur2;
        } else {
            joueurCourant = joueur1;
        }
    }

    public void verifierBornes() {
        for (Borne borne : bornes) {
            borne.verifierEtRevendique(pioche);
        }
    }

    public boolean verifierFinPartie() {
        int bornesJoueur1 = bornesControleesParJoueur(joueur1);
        int bornesJoueur2 = bornesControleesParJoueur(joueur2);

        if (bornesJoueur1 >= 5 || bornesJoueur2 >= 5) {
            return true;
        }

        if (joueurControleTroisBornesAdjacentes(joueur1) || joueurControleTroisBornesAdjacentes(joueur2)) {
            return true;
        }

        return false;
    }

    private boolean joueurControleTroisBornesAdjacentes(Joueur joueur) {
        int compteur = 0;
        for (Borne borne : bornes) {
            if (borne.getProprietaire() != null && borne.getProprietaire().equals(joueur)) {
                compteur++;
                if (compteur >= 3) {
                    return true;
                }
            } else {
                compteur = 0;
            }
        }
        return false;
    }

    private int bornesControleesParJoueur(Joueur joueur) {
        int compteur = 0;
        for (Borne borne : bornes) {
            if (borne.getProprietaire() != null && borne.getProprietaire().equals(joueur)) {
                compteur++;
            }
        }
        return compteur;
    }

    public Joueur determinerGagnant() {
        if (joueurControleTroisBornesAdjacentes(joueur1) || bornesControleesParJoueur(joueur1) >= 5) {
            return joueur1;
        } else if (joueurControleTroisBornesAdjacentes(joueur2) || bornesControleesParJoueur(joueur2) >= 5) {
            return joueur2;
        } else {
            return null;
        }
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void incrementMoveCount() {
        moveCount++;
    }
}
