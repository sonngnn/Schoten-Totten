package com.schottenTotten.ai;

import com.schottenTotten.model.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StrategieAleatoire implements StrategieIA {

    private Random random = new Random();

    @Override
    public void jouerTour(Jeu jeu, Joueur ia) {
        List<Carte> main = ia.getMain();
        if (main.isEmpty()) {
            return;
        }

        Carte carteChoisie = main.get(random.nextInt(main.size()));

        List<Borne> bornesNonRevendiquees = jeu.getBornes().stream()
                .filter(b -> !b.isEstRevendiquee() && b.getCartesJoueur(ia).size() < 3)
                .collect(Collectors.toList());

        if (bornesNonRevendiquees.isEmpty()) {
            return;
        }

        Borne borneChoisie = bornesNonRevendiquees.get(random.nextInt(bornesNonRevendiquees.size()));

        jeu.incrementMoveCount();
        borneChoisie.ajouterCarte(carteChoisie, ia, jeu.getMoveCount());
        ia.retirerCarteMain(carteChoisie);

        Carte nouvelleCarte = jeu.getPioche().piocherCarteClan();
        if (nouvelleCarte != null) {
            ia.ajouterCarteMain(nouvelleCarte);
        }
    }
}
