package com.schottenTotten.controller;

import com.schottenTotten.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ReglesDuJeu {

    public static Combinaison evaluerCombinaison(List<Carte> cartes) {
        if (cartes.size() != 3) {
            return null;
        }

        Collections.sort(cartes);

        if (estSuiteCouleur(cartes)) {
            return new Combinaison(TypeCombinaison.SUITE_COULEUR, cartes.get(2).getValeur());
        } else if (estBrelan(cartes)) {
            return new Combinaison(TypeCombinaison.BRELAN, cartes.get(0).getValeur());
        } else if (estCouleur(cartes)) {
            return new Combinaison(TypeCombinaison.COULEUR, cartes.get(2).getValeur());
        } else if (estSuite(cartes)) {
            return new Combinaison(TypeCombinaison.SUITE, cartes.get(2).getValeur());
        } else {
            int somme = cartes.stream().mapToInt(Carte::getValeur).sum();
            return new Combinaison(TypeCombinaison.SOMME, somme);
        }
    }

    public static int comparerCombinaisons(Combinaison c1, Combinaison c2) {
        if (c1.getType().ordinal() < c2.getType().ordinal()) {
            return 1;
        } else if (c1.getType().ordinal() > c2.getType().ordinal()) {
            return -1;
        } else {
            return Integer.compare(c1.getValeur(), c2.getValeur());
        }
    }

    private static boolean estSuiteCouleur(List<Carte> cartes) {
        return estCouleur(cartes) && estSuite(cartes);
    }

    private static boolean estBrelan(List<Carte> cartes) {
        int valeur = cartes.get(0).getValeur();
        return cartes.stream().allMatch(c -> c.getValeur() == valeur);
    }

    private static boolean estCouleur(List<Carte> cartes) {
        Couleur couleur = cartes.get(0).getCouleur();
        return cartes.stream().allMatch(c -> c.getCouleur() == couleur);
    }

    private static boolean estSuite(List<Carte> cartes) {
        int val1 = cartes.get(0).getValeur();
        int val2 = cartes.get(1).getValeur();
        int val3 = cartes.get(2).getValeur();
        return (val2 == val1 + 1) && (val3 == val2 + 1);
    }

    public static boolean adversairePeutBattreCombinaison(Combinaison combinaisonJoueur, List<Carte> cartesAdversaire, List<Carte> cartesRestantes, List<Carte> mainAdversaire) {
        List<Carte> cartesDisponibles = new ArrayList<>(cartesRestantes);
        cartesDisponibles.addAll(mainAdversaire);

        Set<Carte> cartesDejaJouees = new HashSet<>(cartesAdversaire);
        cartesDisponibles.removeAll(cartesDejaJouees);

        int cartesManquantes = 3 - cartesAdversaire.size();
        if (cartesManquantes <= 0) {
            Combinaison combinaisonAdversaire = evaluerCombinaison(cartesAdversaire);
            if (combinaisonAdversaire != null) {
                int comparaison = comparerCombinaisons(combinaisonAdversaire, combinaisonJoueur);
                return comparaison > 0;
            }
            return false;
        }

        List<List<Carte>> combinaisonsPossibles = genererCombinaisons(cartesDisponibles, cartesManquantes);

        for (List<Carte> combo : combinaisonsPossibles) {
            List<Carte> combinaisonComplete = new ArrayList<>(cartesAdversaire);
            combinaisonComplete.addAll(combo);
            Combinaison combinaisonAdversaire = evaluerCombinaison(combinaisonComplete);
            if (combinaisonAdversaire != null) {
                int comparaison = comparerCombinaisons(combinaisonAdversaire, combinaisonJoueur);
                if (comparaison > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<List<Carte>> genererCombinaisons(List<Carte> cartes, int k) {
        List<List<Carte>> resultats = new ArrayList<>();
        genererCombinaisonsRecursif(cartes, k, 0, new ArrayList<>(), resultats);
        return resultats;
    }

    private static void genererCombinaisonsRecursif(List<Carte> cartes, int k, int index, List<Carte> current, List<List<Carte>> resultats) {
        if (current.size() == k) {
            resultats.add(new ArrayList<>(current));
            return;
        }
        for (int i = index; i < cartes.size(); i++) {
            current.add(cartes.get(i));
            genererCombinaisonsRecursif(cartes, k, i + 1, current, resultats);
            current.remove(current.size() - 1);
        }
    }
}
