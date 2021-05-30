package tests;

import blackjackobjects.Card;
import blackjackobjects.Money;
import blackjackobjects.Person;
import databasecommunication.Player;
import model.enums.RoundType;
import model.enums.WinSituation;
import model.handlers.EvaluationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

public class EvaluationHandlerTest {

    private final EvaluationHandler evaluationHandler = new EvaluationHandler();

    private final ImageIcon karoImage5 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/5_of_diamonds.png");
    private final Card karo5 = new Card("5", 5, karoImage5);
    private final ImageIcon karoImage6 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/6_of_diamonds.png");
    private final Card karo6 = new Card("6", 6, karoImage6);
    private final ImageIcon karoImage10 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/10_of_diamonds.png");
    private final Card karo10 = new Card("10", 10, karoImage10);
    private final ImageIcon karoImageA = new ImageIcon("src/main/resources/KartenDeckImages/Karo/ace_of_diamonds.png");
    private final Card karoA = new Card("A", 11, karoImageA);

    @Test
    public void evaluateStartSplit(){
        Player player = new Player();
        player.setBetAmount(Money.toMoney(10));
        player.setBalance(Money.toMoney(100));
        player.addHand(karoA);
        player.addHand(karoA);

        assertEquals(RoundType.SPLIT, evaluationHandler.evaluateStart(player));
    }

    @Test
    public void evaluateStartSplitToLowBalance(){
        Player playerToLowAmountForSplit = new Player();
        playerToLowAmountForSplit.setBetAmount(Money.toMoney(10));
        playerToLowAmountForSplit.setBalance(Money.toMoney(5));
        playerToLowAmountForSplit.addHand(karoA);
        playerToLowAmountForSplit.addHand(karoA);

        assertEquals(RoundType.NORMAL, evaluationHandler.evaluateStart(playerToLowAmountForSplit));
    }

    @Test
    public void evaluateStartDouble(){
        Player player = new Player();
        player.setBetAmount(Money.toMoney(10));
        player.setBalance(Money.toMoney(100));
        player.addHand(karo5);
        player.addHand(karo6);
        player.addPointsOnHand(11);

        assertEquals(RoundType.DOPPEL, evaluationHandler.evaluateStart(player));
    }

    @Test
    public void evaluateStartDoubleToLowBalance(){
        Player playerToLowAmountForDouble = new Player();
        playerToLowAmountForDouble.setBetAmount(Money.toMoney(10));
        playerToLowAmountForDouble.setBalance(Money.toMoney(5));
        playerToLowAmountForDouble.addHand(karo5);
        playerToLowAmountForDouble.addHand(karo6);
        playerToLowAmountForDouble.addPointsOnHand(11);

        assertEquals(RoundType.NORMAL, evaluationHandler.evaluateStart(playerToLowAmountForDouble));
    }

    @Test
    public void evaluateStartBlackJack(){
        Player player = new Player();
        player.setBetAmount(Money.toMoney(10));
        player.setBalance(Money.toMoney(100));
        player.addHand(karoA);
        player.addHand(karo10);
        player.addPointsOnHand(21);

        assertEquals(RoundType.BLACKJACK, evaluationHandler.evaluateStart(player));
    }

    @Test
    public void evaluateStartNormal(){
        ImageIcon karoImage5 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/5_of_diamonds.png");
        Card karo5 = new Card("5", 5, karoImage5);
        ImageIcon karoImage10 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/10_of_diamonds.png");
        Card karo10 = new Card("10", 10, karoImage10);

        Player player = new Player();
        player.setBetAmount(Money.toMoney(10));
        player.setBalance(Money.toMoney(100));
        player.addHand(karo5);
        player.addHand(karo10);
        player.addPointsOnHand(15);

        assertEquals(RoundType.NORMAL, evaluationHandler.evaluateStart(player));
    }

    @Test
    public void evaluateStopNormalCroupierWinPlayerBust(){
        Person croupier = new Person();
        croupier.setPointsOnHand(18);
        Player player = new Player();
        player.setPointsOnHand(22);
        player.setBust(true);

        assertEquals(WinSituation.COUPIERGEWINNT, evaluationHandler.evaluateStopNormal(player, croupier));
    }

    @Test
    public void evaluateStopNormalCroupierWin(){
        Person croupier = new Person();
        croupier.setPointsOnHand(20);
        Player player = new Player();
        player.setPointsOnHand(18);

        assertEquals(WinSituation.COUPIERGEWINNT, evaluationHandler.evaluateStopNormal(player, croupier));
    }

    @Test
    public void evaluateStopNormalTie(){
        Person croupier = new Person();
        croupier.setPointsOnHand(20);
        Player player = new Player();
        player.setPointsOnHand(20);

        assertEquals(WinSituation.UNENTSCHIEDEN, evaluationHandler.evaluateStopNormal(player, croupier));
    }

    @Test
    public void evaluateStopNormalPlayerWinCroupierBust(){
        Person croupier = new Person();
        croupier.setPointsOnHand(22);
        croupier.setBust(true);
        Player player = new Player();
        player.setPointsOnHand(18);

        assertEquals(WinSituation.SPIELERGEWINNT, evaluationHandler.evaluateStopNormal(player, croupier));
    }

    @Test
    public void evaluateStopNormalPlayerWin(){
        Person croupier = new Person();
        croupier.setPointsOnHand(17);
        Player player = new Player();
        player.setPointsOnHand(20);

        assertEquals(WinSituation.SPIELERGEWINNT, evaluationHandler.evaluateStopNormal(player, croupier));
    }

    @Test
    public void evaluateStopSplitCroupierBust(){
        Person croupier = new Person();
        croupier.setPointsOnHand(22);
        croupier.setBust(true);
        Player player = new Player();
        player.setPointsOnHand(20);
        player.setSplitPlayerPoints(21);

        assertEquals(WinSituation.H1H2, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitPlayerWon(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(20);
        player.setSplitPlayerPoints(21);

        assertEquals(WinSituation.H1H2, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitPlayerHand1Bust(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(22);
        player.setBust(true);
        player.setSplitPlayerPoints(21);

        assertEquals(WinSituation.H2, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitHand2Won(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(18);
        player.setSplitPlayerPoints(21);

        assertEquals(WinSituation.H2, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitPlayerHand2Bust(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(20);
        player.setSplitPlayerPoints(22);
        player.setBust2(true);

        assertEquals(WinSituation.H1, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitHand1Won(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(21);
        player.setSplitPlayerPoints(18);

        assertEquals(WinSituation.H1, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitCroupierWon(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(17);
        player.setSplitPlayerPoints(18);

        assertEquals(WinSituation.H0, evaluationHandler.evaluateStopSplit(player, croupier));
    }

    @Test
    public void evaluateStopSplitH1H2Bust(){
        Person croupier = new Person();
        croupier.setPointsOnHand(19);
        Player player = new Player();
        player.setPointsOnHand(22);
        player.setBust(true);
        player.setSplitPlayerPoints(22);
        player.setBust2(true);

        assertEquals(WinSituation.H0, evaluationHandler.evaluateStopSplit(player, croupier));
    }
}
