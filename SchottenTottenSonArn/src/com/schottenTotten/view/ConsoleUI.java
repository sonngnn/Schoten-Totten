package com.schottenTotten.view;

import com.schottenTotten.model.*;
import com.schottenTotten.AnsiColor;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
    }

    public void afficherEtatJeu(Jeu jeu) {
        System.out.println("=== État du Jeu ===");
        for (Borne borne : jeu.getBornes()) {
            if (borne.isEstRevendiquee()) {
                String color = borne.getProprietaireColor();
                System.out.print(color + "Borne " + borne.getNumero() + " : Revendiquée par " + borne.getProprietaire().getNom());
                System.out.print(" | Composition : ");
                afficherCartesForceCouleur(borne.getCartesJoueur(jeu.getJoueur1()), color);
                System.out.print(" |-|-|-| ");
                afficherCartesForceCouleur(borne.getCartesJoueur(jeu.getJoueur2()), color);
                System.out.println(AnsiColor.RESET);
            } else {
                System.out.print("Borne " + borne.getNumero() + " : ");
                System.out.print(jeu.getJoueur1().getNom() + " : ");
                afficherCartes(borne.getCartesJoueur(jeu.getJoueur1()));
                System.out.print(" |-|-|-| " + jeu.getJoueur2().getNom() + " : ");
                afficherCartes(borne.getCartesJoueur(jeu.getJoueur2()));
                System.out.println();
            }
        }
    }

    private void afficherCartes(List<Carte> cartes) {
        String affichage = cartes.stream()
                .map(Carte::toString)
                .collect(Collectors.joining(" | "));
        System.out.print(affichage);
    }

    private void afficherCartesForceCouleur(List<Carte> cartes, String proprietaireColor) {
        String affichage = cartes.stream()
                .map(c -> proprietaireColor + c.getCouleur().name() + " " + c.getValeur() + AnsiColor.RESET)
                .collect(Collectors.joining(" | "));
        System.out.print(affichage);
    }

    public void demanderAction(Joueur joueur, Jeu jeu) {
        boolean actionValide = false;
        while (!actionValide) {
            System.out.println("\n" + joueur.getNom() + ", c'est votre tour.");

            List<Carte> main = joueur.getMain();
            System.out.println("Votre main :");
            for (int i = 0; i < main.size(); i++) {
                System.out.println((i + 1) + ". " + main.get(i));
            }

            System.out.print("Choisissez une carte à jouer (entrez le numéro) : ");
            int choixCarte = lireEntier(1, main.size()) - 1;

            Carte carteChoisie = main.get(choixCarte);

            System.out.print("Choisissez une borne (1-9) : ");
            int choixBorne = lireEntier(1, 9) - 1;

            Borne borneChoisie = jeu.getBornes().get(choixBorne);

            if (borneChoisie.isEstRevendiquee()) {
                System.out.println("Cette borne a déjà été revendiquée. Veuillez choisir une autre borne.");
                continue;
            }

            List<Carte> cartesJoueur = borneChoisie.getCartesJoueur(joueur);
            if (cartesJoueur.size() >= 3) {
                System.out.println("Vous avez déjà joué 3 cartes sur cette borne. Veuillez choisir une autre borne.");
                continue;
            }

            // Incrémenter le moveCount avant de jouer la carte
            jeu.incrementMoveCount();
            borneChoisie.ajouterCarte(carteChoisie, joueur, jeu.getMoveCount());
            joueur.retirerCarteMain(carteChoisie);

            Carte nouvelleCarte = jeu.getPioche().piocherCarteClan();
            if (nouvelleCarte != null) {
                joueur.ajouterCarteMain(nouvelleCarte);
            }

            actionValide = true;
        }
    }

    public void afficherResultat(Jeu jeu) {
        System.out.println("\n=== Fin de la partie ===");
        Joueur gagnant = jeu.determinerGagnant();
        if (gagnant != null) {
            System.out.println("Félicitations " + gagnant.getNom() + ", vous avez gagné !");
        } else {
            System.out.println("La partie est terminée. Il n'y a pas de gagnant.");
        }
    }

    private int lireEntier(int min, int max) {
        int choix;
        while (true) {
            try {
                String input = scanner.nextLine();
                choix = Integer.parseInt(input);
                if (choix >= min && choix <= max) {
                    return choix;
                } else {
                    System.out.print("Veuillez entrer un nombre entre " + min + " et " + max + " : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez entrer un nombre entre " + min + " et " + max + " : ");
            }
        }
    }
}
