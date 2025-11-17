package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Pioche {
    private Stack<Carte> cartesClan;

    public Pioche() {
        cartesClan = new Stack<>();
        initialiserCartesClan();
    }

    private void initialiserCartesClan() {
        for (Couleur couleur : Couleur.values()) {
            for (int valeur = 1; valeur <= 9; valeur++) {
                cartesClan.add(new CarteClan(valeur, couleur));
            }
        }
        Collections.shuffle(cartesClan);
    }

    public Carte piocherCarteClan() {
        if (!cartesClan.isEmpty()) {
            return cartesClan.pop();
        } else {
            return null; // Pioche vide
        }
    }

    public boolean estVide() {
        return cartesClan.isEmpty();
    }

    public List<Carte> getCartesRestantes() {
        return new ArrayList<>(cartesClan);
    }
}
