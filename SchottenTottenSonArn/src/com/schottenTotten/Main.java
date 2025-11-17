package com.schottenTotten;

import com.schottenTotten.model.*;
import com.schottenTotten.view.ConsoleUI;
import com.schottenTotten.controller.JeuFactory;
import com.schottenTotten.ai.StrategieAleatoire;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Bienvenue dans Schotten-Totten ===");

        System.out.print("Entrez le nom du Joueur 1 : ");
        String nomJoueur1 = scanner.nextLine();

        System.out.print("Le Joueur 1 est-il une IA ? (oui/non) : ");
        String reponseJoueur1 = scanner.nextLine();
        TypeJoueur typeJoueur1 = reponseJoueur1.equalsIgnoreCase("oui") ? TypeJoueur.IA : TypeJoueur.HUMAIN;

        System.out.print("Entrez le nom du Joueur 2 : ");
        String nomJoueur2 = scanner.nextLine();

        System.out.print("Le Joueur 2 est-il une IA ? (oui/non) : ");
        String reponseJoueur2 = scanner.nextLine();
        TypeJoueur typeJoueur2 = reponseJoueur2.equalsIgnoreCase("oui") ? TypeJoueur.IA : TypeJoueur.HUMAIN;

        String nomJ1Colore = AnsiColor.RED + nomJoueur1 + AnsiColor.RESET;
        String nomJ2Colore = AnsiColor.BLUE + nomJoueur2 + AnsiColor.RESET;

        Joueur joueur1 = new Joueur(nomJ1Colore, typeJoueur1);
        Joueur joueur2 = new Joueur(nomJ2Colore, typeJoueur2);

        if (typeJoueur1 == TypeJoueur.IA) {
            joueur1.setStrategie(new StrategieAleatoire());
        }
        if (typeJoueur2 == TypeJoueur.IA) {
            joueur2.setStrategie(new StrategieAleatoire());
        }

        System.out.print("Choisissez la variante du jeu (classique/tactique) : ");
        String variante = scanner.nextLine();

        Jeu jeu = JeuFactory.creerJeu(variante, joueur1, joueur2);

        ConsoleUI consoleUI = new ConsoleUI();

        System.out.println("\n=== Début de la partie ===\n");

        while (!jeu.verifierFinPartie()) {
            consoleUI.afficherEtatJeu(jeu);

            Joueur joueurCourant = jeu.getJoueurCourant();

            if (joueurCourant.getType() == TypeJoueur.HUMAIN) {
                consoleUI.demanderAction(joueurCourant, jeu);
            } else {
                joueurCourant.jouerTour(jeu);
            }

            jeu.verifierBornes();

            if (jeu.verifierFinPartie()) {
                break;
            }

            jeu.changerJoueurCourant();
        }

        consoleUI.afficherResultat(jeu);

        scanner.close();
    }
}
