package com.schottenTotten.model;

public class Combinaison {
    private TypeCombinaison type;
    private int valeur;

    public Combinaison(TypeCombinaison type, int valeur) {
        this.type = type;
        this.valeur = valeur;
    }

    public TypeCombinaison getType() {
        return type;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return type + " (Valeur: " + valeur + ")";
    }
}
