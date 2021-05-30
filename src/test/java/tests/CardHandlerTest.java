package tests;

import blackjackobjects.Person;
import databasecommunication.Player;
import mocks.CardHandlerMockForCheckAce;
import model.enums.DrawSituation;
import model.handlers.CardHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CardHandlerTest {

    private final CardHandler cardHandler = new CardHandler();

    @BeforeEach
    public void newCardDeck(){
        cardHandler.createNewDeck();
    }

    @Test
    public void testCheckIfBustBust() {
        Person personBust = new Person();
        personBust.addPointsOnHand(22);

        assertEquals(DrawSituation.JA, cardHandler.checkIfBust(personBust));
        assertTrue(personBust.isBust());
    }

    @Test
    public void testCheckIfBustNotBust(){
        Person personNotBust = new Person();
        personNotBust.addPointsOnHand(20);

        assertEquals(DrawSituation.NEIN, cardHandler.checkIfBust(personNotBust));
        assertFalse(personNotBust.isBust());
    }

    @Test
    public void testCheckIfBustPlayerSplitHand2Bust() {
        Player playerSplitBust = new Player();
        playerSplitBust.setHand2(true);
        playerSplitBust.setSplitPlayerPoints(30);

        assertEquals(DrawSituation.HAND2JA, cardHandler.checkIfBust(playerSplitBust));
        assertTrue(playerSplitBust.isBust2());
    }

    @Test
    public void testCheckIfBustPlayerSplitHand2NotBust(){
        Player playerSplitNotBust = new Player();
        playerSplitNotBust.setHand2(true);
        playerSplitNotBust.setSplitPlayerPoints(20);

        assertEquals(DrawSituation.NEIN, cardHandler.checkIfBust(playerSplitNotBust));
        assertFalse(playerSplitNotBust.isBust2());
    }


    @Test
    public void checkForAce1(){
        Person person = new Person();
        person.addPointsOnHand(15);

        CardHandlerMockForCheckAce cardHandlerMock = new CardHandlerMockForCheckAce();
        cardHandlerMock.createNewDeck();
        cardHandlerMock.givePlayingCard(person);

        assertEquals(16, person.getPointsOnHand());
    }

    @Test
    public void checkForAce11(){
        Person person = new Person();
        person.addPointsOnHand(9);

        CardHandlerMockForCheckAce cardHandlerMock = new CardHandlerMockForCheckAce();
        cardHandlerMock.createNewDeck();
        cardHandlerMock.givePlayingCard(person);

        assertEquals(20, person.getPointsOnHand());
    }

    @Test
    public void checkForAce1SplitHand(){
        Player playerAce1 = new Player();
        playerAce1.setHand2(true);
        playerAce1.addSplitPlayerPoints(15);

        CardHandlerMockForCheckAce cardHandlerMock = new CardHandlerMockForCheckAce();
        cardHandlerMock.createNewDeck();
        cardHandlerMock.givePlayingCard(playerAce1);

        assertEquals(16, playerAce1.getSplitPlayerPoints());
    }

    @Test
    public void checkForAce11SplitHand(){

        Player playerAce11 = new Player();
        playerAce11.setHand2(true);
        playerAce11.addSplitPlayerPoints(9);

        CardHandlerMockForCheckAce cardHandlerMock = new CardHandlerMockForCheckAce();
        cardHandlerMock.createNewDeck();
        cardHandlerMock.givePlayingCard(playerAce11);

        assertEquals(20, playerAce11.getSplitPlayerPoints());
    }

    @Test
    public void croupierTurn() {
        Person person = new Person();
        Person person2 = new Person();

        cardHandler.croupierTurn(person);
        cardHandler.croupierTurn(person2);

        assertTrue(person.getPointsOnHand()>=17);
        assertTrue(person2.getPointsOnHand()>=17);
    }

    @Test
    public void givePLayingCardGame() {
        Person person = new Person();
        cardHandler.givePlayingCard(person);

        Person person2 = new Person();
        cardHandler.givePlayingCard(person2);
        cardHandler.givePlayingCard(person2);

        assertEquals(1, person.getHand().size());
        assertEquals(2, person2.getHand().size());
    }

    @Test
    public void givePLayingCardGameSplitHand() {
        Player player = new Player();
        cardHandler.givePlayingCard(player);
        player.setHand2(true);
        cardHandler.givePlayingCard(player);

        Player player2 = new Player();
        cardHandler.givePlayingCard(player2);
        cardHandler.givePlayingCard(player2);
        player2.setHand2(true);
        cardHandler.givePlayingCard(player2);
        cardHandler.givePlayingCard(player2);

        assertEquals(1, player.getHand().size());
        assertEquals(1, player.getSplitPlayerHand().size());

        assertEquals(2, player2.getHand().size());
        assertEquals(2, player2.getSplitPlayerHand().size());
    }

}
