package com.schottenTotten.ai;

import com.schottenTotten.model.Jeu;
import com.schottenTotten.model.Joueur;

public interface StrategieIA {
    void jouerTour(Jeu jeu, Joueur ia);
}
