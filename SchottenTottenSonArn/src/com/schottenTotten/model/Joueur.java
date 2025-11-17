package com.schottenTotten.model;

import com.schottenTotten.ai.StrategieIA;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private String nom;
    private List<Carte> main;
    private TypeJoueur type;
    private StrategieIA strategie;

    public Joueur(String nom, TypeJoueur type) {
        this.nom = nom;
        this.type = type;
        this.main = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public List<Carte> getMain() {
        return main;
    }

    public TypeJoueur getType() {
        return type;
    }

    public StrategieIA getStrategie() {
        return strategie;
    }

    public void setStrategie(StrategieIA strategie) {
        this.strategie = strategie;
    }

    public void ajouterCarteMain(Carte carte) {
        main.add(carte);
    }

    public void retirerCarteMain(Carte carte) {
        main.remove(carte);
    }

    public void jouerTour(Jeu jeu) {
        if (type == TypeJoueur.IA && strategie != null) {
            strategie.jouerTour(jeu, this);
        }
    }
}
