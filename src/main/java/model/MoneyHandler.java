package model;

import blackjackobjects.Money;
import databasecommunication.HibernateDatabaseActions;
import databasecommunication.Player;
import model.enums.WinSituation;

import java.sql.Timestamp;

public class MoneyHandler {

    public void giftMoney(Player player) {
        Timestamp newTime = new Timestamp(System.currentTimeMillis());
        player.setPlayerBank(Money.add(player.getPlayerBank(), 1000));
        player.setTimeUntilMoneyIsAvailable(newTime);
        HibernateDatabaseActions.updateTimeStamp(newTime, player.getUsername());
    }

    public void takeMoneyFromBank(Player player, double plusBalance) {
        if (plusBalance <= 0 || Money.compare(player.getPlayerBank(), plusBalance) > 0) {
            throw new IllegalArgumentException("falscher Betrag");
        }
        player.setBalance(Money.add(player.getBalance(), plusBalance));
        player.setPlayerBank(Money.subtract(player.getPlayerBank(), plusBalance));
    }

    public void placeBet(Player player, double bet) {
        if (bet <= 0 || Money.compare(player.getBalance(), bet) > 0) {
            throw new IllegalArgumentException("bet muss > 0 sein oder kleiner als player geld");
        }
        player.setBetAmount(Money.toMoney(bet));
        player.removeFromBalance(Money.toMoney(bet));
    }

    public void moneyBasedOnWinSituation(WinSituation winSituation, Player player){
        switch (winSituation) {
            case H1H2:
            case SPIELERGEWINNT:
                player.addToBalance(Money.multiply(player.getBetAmount(), 2));
                break;
            case UNENTSCHIEDEN:
            case H1:
            case H2:
                player.addToBalance(player.getBetAmount());
                break;
        }
    }


}
