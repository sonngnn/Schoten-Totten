package com.schottenTotten.controller;

import com.schottenTotten.model.Jeu;
import com.schottenTotten.model.Joueur;

public class JeuFactory {
    public static Jeu creerJeu(String variante, Joueur joueur1, Joueur joueur2) {
        if (variante.equalsIgnoreCase("classique")) {
            return new Jeu(joueur1, joueur2);
        } else if (variante.equalsIgnoreCase("tactique")) {
            return new Jeu(joueur1, joueur2);
        } else {
            throw new IllegalArgumentException("Variante inconnue : " + variante);
        }
    }
}
