package com.schottenTotten.model;

import com.schottenTotten.controller.ReglesDuJeu;
import com.schottenTotten.AnsiColor;

import java.util.ArrayList;
import java.util.List;

public class Borne {
    private int numero;
    private Joueur joueur1;
    private Joueur joueur2;
    private List<Carte> cartesJoueur1;
    private List<Carte> cartesJoueur2;
    private boolean estRevendiquee;
    private Joueur proprietaire;
    private String proprietaireColor = AnsiColor.RESET;

    // Tour d'achèvement (pose de la 3eme carte) pour chaque joueur
    private int joueur1CompletionTurn = -1;
    private int joueur2CompletionTurn = -1;

    public Borne(int numero, Joueur joueur1, Joueur joueur2) {
        this.numero = numero;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.cartesJoueur1 = new ArrayList<>();
        this.cartesJoueur2 = new ArrayList<>();
        this.estRevendiquee = false;
        this.proprietaire = null;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isEstRevendiquee() {
        return estRevendiquee;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public String getProprietaireColor() {
        return proprietaireColor;
    }

    public List<Carte> getCartesJoueur(Joueur joueur) {
        if (joueur.equals(joueur1)) {
            return cartesJoueur1;
        } else if (joueur.equals(joueur2)) {
            return cartesJoueur2;
        } else {
            return null;
        }
    }

    public void ajouterCarte(Carte carte, Joueur joueur) {
        if (estRevendiquee) {
            System.out.println("Cette borne a déjà été revendiquée.");
            return;
        }

        List<Carte> cartesJoueur = getCartesJoueur(joueur);
        if (cartesJoueur.size() >= 3) {
            System.out.println("Vous avez déjà joué 3 cartes sur cette borne.");
            return;
        }

        cartesJoueur.add(carte);
        // Si c'est la troisième carte de ce joueur sur cette borne, on enregistre le tour d'achèvement
        if (cartesJoueur.size() == 3) {
            int completionTurn = joueur.equals(joueur1) ? joueur1CompletionTurn : joueur2CompletionTurn;
            if (completionTurn == -1) {
                if (joueur.equals(joueur1)) {
                    joueur1CompletionTurn = joueur.getType() == TypeJoueur.IA ? 
                        -1 : // IA ou non, on utilise le moveCount du jeu
                        -1; 
                }

                // Toutefois, on a besoin du moveCount du jeu. On peut l'obtenir via Jeu, mais Borne n'y a pas accès direct.
                // On va passer par un setter. Après réflexion, Borne ne connaît pas Jeu.
                // On a besoin d'une solution: On va stocker le moveCount dans la borne via un callback. 
                // Plus simple: Appeler jeu.incrementerMoveCount() lorsque le joueur joue.
                // Changement de stratégie: le moveCount doit être passé en paramètre de ajouterCarte.
            }
        }
    }

    public void verifierEtRevendique(Pioche pioche) {
        if (estRevendiquee) {
            return;
        }

        if (peutRevendiquee(joueur1, pioche)) {
            estRevendiquee = true;
            proprietaire = joueur1;
            proprietaireColor = AnsiColor.RED;
        } else if (peutRevendiquee(joueur2, pioche)) {
            estRevendiquee = true;
            proprietaire = joueur2;
            proprietaireColor = AnsiColor.BLUE;
        }
    }

    private boolean peutRevendiquee(Joueur joueur, Pioche pioche) {
        List<Carte> cartesJoueur = getCartesJoueur(joueur);
        if (cartesJoueur.size() < 3) {
            return false;
        }

        Combinaison combinaisonJoueur = ReglesDuJeu.evaluerCombinaison(cartesJoueur);
        if (combinaisonJoueur == null) {
            return false;
        }

        Joueur adversaire = (joueur.equals(joueur1)) ? joueur2 : joueur1;
        List<Carte> cartesAdversaire = getCartesJoueur(adversaire);
        Combinaison combinaisonAdversaire = null;
        if (cartesAdversaire.size() == 3) {
            combinaisonAdversaire = ReglesDuJeu.evaluerCombinaison(cartesAdversaire);
        }

        List<Carte> cartesRestantes = pioche.getCartesRestantes();

        boolean adversairePeutBattre = ReglesDuJeu.adversairePeutBattreCombinaison(combinaisonJoueur, cartesAdversaire, cartesRestantes, adversaire.getMain());
        if (adversairePeutBattre) {
            return false;
        }

        // Si les deux ont 3 cartes, vérifier l'égalité
        if (combinaisonAdversaire != null) {
            int comparaison = ReglesDuJeu.comparerCombinaisons(combinaisonJoueur, combinaisonAdversaire);
            if (comparaison < 0) {
                // Adversaire plus fort
                return false;
            } else if (comparaison > 0) {
                // Joueur est plus fort
                return true;
            } else {
                // Egalité parfaite (par ex somme égale)
                // Règle : le joueur ayant posé sa 3e carte en premier l'emporte
                int turnJ1 = getCompletionTurn(joueur1);
                int turnJ2 = getCompletionTurn(joueur2);

                // Le joueur ayant le plus petit turnCompletion gagne
                if (turnJ1 == -1 && turnJ2 == -1) {
                    // Personne n'a complété, improbable
                    return false;
                }

                if (turnJ1 == -1) {
                    // Joueur2 seulement a complété
                    return joueur.equals(joueur2);
                }

                if (turnJ2 == -1) {
                    // Joueur1 seulement a complété
                    return joueur.equals(joueur1);
                }

                // Les deux ont complété
                if (turnJ1 < turnJ2) {
                    // Joueur1 a complété avant
                    return joueur.equals(joueur1);
                } else if (turnJ2 < turnJ1) {
                    // Joueur2 a complété avant
                    return joueur.equals(joueur2);
                } else {
                    // Parfaitement même tour ?? Très improbable
                    return joueur.equals(joueur1); // Par défaut
                }
            }
        } else {
            // Adversaire n'a pas 3 cartes, Joueur gagne si on ne peut plus être battu
            return true;
        }
    }

    // On a besoin de connaître l'ordre dans lequel les 3e cartes ont été posées.
    // Pour cela, on va stocker le moveCount lorsqu'une 3eme carte est posée.
    // On modifie la signature de ajouterCarte pour prendre un moveCount.
    public void ajouterCarte(Carte carte, Joueur joueur, int moveCount) {
        if (estRevendiquee) {
            System.out.println("Cette borne a déjà été revendiquée.");
            return;
        }

        List<Carte> cartesJoueur = getCartesJoueur(joueur);
        if (cartesJoueur.size() >= 3) {
            System.out.println("Vous avez déjà joué 3 cartes sur cette borne.");
            return;
        }

        cartesJoueur.add(carte);
        if (cartesJoueur.size() == 3) {
            if (joueur.equals(joueur1)) {
                if (joueur1CompletionTurn == -1) {
                    joueur1CompletionTurn = moveCount;
                }
            } else {
                if (joueur2CompletionTurn == -1) {
                    joueur2CompletionTurn = moveCount;
                }
            }
        }
    }

    public int getCompletionTurn(Joueur joueur) {
        if (joueur.equals(joueur1)) {
            return joueur1CompletionTurn;
        } else {
            return joueur2CompletionTurn;
        }
    }
}
