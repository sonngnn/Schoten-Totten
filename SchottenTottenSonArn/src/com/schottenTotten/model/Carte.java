package com.schottenTotten.model;

import com.schottenTotten.AnsiColor;

public abstract class Carte implements Comparable<Carte> {
    private int valeur;
    private Couleur couleur;

    public Carte(int valeur, Couleur couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
    }

    public int getValeur() {
        return valeur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public abstract String getTypeCarte();

    @Override
    public String toString() {
        return getCouleur().getANSIColor() + getCouleur().name() + " " + getValeur() + AnsiColor.RESET;
    }

    @Override
    public int compareTo(Carte other) {
        return Integer.compare(this.valeur, other.valeur);
    }
}
