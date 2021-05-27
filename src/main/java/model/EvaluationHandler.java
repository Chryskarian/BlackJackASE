package model;

import blackjackobjects.Money;
import blackjackobjects.Person;
import databasecommunication.Player;
import model.enums.RoundType;
import model.enums.WinSituation;

public class EvaluationHandler {

    public RoundType evaluateStart(Player player) {

        if (player.getHand().get(0).getCardValue() == player.getHand().get(1).getCardValue() && Money.compare(player.getBetAmount(), player.getBalance()) >= 0) {
            return RoundType.SPLIT;
        } else if (player.getPointsOnHand() >= 9 && player.getPointsOnHand() <= 11 && Money.compare(player.getBetAmount(), player.getBalance()) >= 0) {
            return RoundType.DOPPEL;
        } else if (player.getPointsOnHand() == 21) {
            return RoundType.BLACKJACK;
        } else {
            return RoundType.NORMAL;
        }
    }

    private WinSituation evaluateWinSituation(Player player, Person croupier) {
        // Hat der Croupier mehr Punkte als der Spieler gewinnt der Croupier
        if (player.isBust() || player.getPointsOnHand() < croupier.getPointsOnHand() && !croupier.isBust()) {
            return WinSituation.COUPIERGEWINNT;
        } else if (player.getPointsOnHand() == croupier.getPointsOnHand() && !croupier.isBust()) {
            return WinSituation.UNENTSCHIEDEN;
        } else {
            // Ansonsten gewinnt der Spieler
            return WinSituation.SPIELERGEWINNT;
        }
    }

    private WinSituation evaluateWinSituationSplit(Player player, Person croupier) {
        int spH1 = player.getPointsOnHand();
        int spH2 = player.getSplitPlayerPoints();
        int cp = croupier.getPointsOnHand();
        //Hand 1 und 2 gewinnen
        if (croupier.isBust() && !player.isBust() && !player.isBust2() || !player.isBust() && !player.isBust2() && spH1 > cp && spH2 > cp) {
            return WinSituation.H1H2;
            // Situationen in denen nur Hand 2 gewinnt
        } else if (player.isBust() && !player.isBust2() && spH2 > cp || !player.isBust() && !player.isBust2() && spH1 <= cp && spH2 > cp) {
            return WinSituation.H2;
            // Situationen in denen nur Hand 1 gewinnt
        } else if (player.isBust2() && !player.isBust() && spH1 > cp || !player.isBust() && !player.isBust2() && spH2 <= cp && spH1 > cp) {
            return WinSituation.H1;
        } else {
            // sonst gewinnt keine Hand, Croupier gewinnt
            return WinSituation.H0;
        }
    }

    public WinSituation evaluateStopNormal(Player player, Person croupier) {
        return evaluateWinSituation(player, croupier);
    }

    public WinSituation evaluateStopSplit(Player player, Person croupier) {
        return evaluateWinSituationSplit(player, croupier);
    }

}
