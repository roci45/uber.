package ar.edu.unju.fi.model;

public enum TipoAutomovil {
    X(0.0),       // Tarifa base (0% adicional)
    LUXE(0.10),    // 10% adicional
    PREMIUM(0.20); // 20% adicional

    private final double tarifaAdicional;

    TipoAutomovil(double tarifaAdicional) {
        this.tarifaAdicional = tarifaAdicional;
    }

    public double getTarifaAdicional() {
        return tarifaAdicional;
    }
}