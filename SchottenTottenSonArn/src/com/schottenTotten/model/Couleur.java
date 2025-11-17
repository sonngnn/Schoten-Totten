package com.schottenTotten.model;

import com.schottenTotten.AnsiColor;

public enum Couleur {
    ROUGE,
    BLEU,
    VERT,
    JAUNE,
    VIOLET,
    ORANGE;

    public String getANSIColor() {
        switch (this) {
            case ROUGE: return AnsiColor.RED;
            case BLEU: return AnsiColor.BLUE;
            case VERT: return AnsiColor.GREEN;
            case JAUNE: return AnsiColor.YELLOW;
            case VIOLET: return AnsiColor.PURPLE;
            case ORANGE: return AnsiColor.ORANGE;
            default: return AnsiColor.RESET;
        }
    }
}
