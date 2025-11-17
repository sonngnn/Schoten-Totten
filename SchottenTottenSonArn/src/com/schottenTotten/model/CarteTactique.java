package com.schottenTotten.model;

import com.schottenTotten.AnsiColor;

public class CarteTactique extends Carte {
    private String capacite;

    public CarteTactique(int valeur, Couleur couleur, String capacite) {
        super(valeur, couleur);
        this.capacite = capacite;
    }

    public String getCapacite() {
        return capacite;
    }

    @Override
    public String getTypeCarte() {
        return "Tactique";
    }

    @Override
    public String toString() {
        return getCouleur().getANSIColor() + "Tactique " + getCapacite() + AnsiColor.RESET;
    }
}
