package model.enums;

public enum WinSituation {
    SPIELERGEWINNT("Sie gewinnen ! Sie bekommen doppelten Einsatz zurück."),
    COUPIERGEWINNT("Der Croupier hat gewonnen. Sie verlieren Ihren Einsatz."),
    H1("Sie gewinnen mit Hand 1, verlieren mit Hand2."),
    H2("Sie gewinnen mit Hand 2, verlieren mit Hand 1."),
    H1H2("Sie gewinnen Ihren Split mit beiden Händen."),
    H0("Sie verlieren mit beiden Händen."),
    UNENTSCHIEDEN("Unentschieden. Geld zurück");

    private final String message;

    WinSituation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
