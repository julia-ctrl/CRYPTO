package org.julia.lab3.client;

public enum CasinoMode {
    LGC("Lcg"),
    MT("Mt");
    final String mode;

    CasinoMode(String mode) {
        this.mode = mode;
    }
}
