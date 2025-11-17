package com.schottenTotten.model;

public class CarteClan extends Carte {

    public CarteClan(int valeur, Couleur couleur) {
        super(valeur, couleur);
    }

    @Override
    public String getTypeCarte() {
        return "Clan";
    }
}
