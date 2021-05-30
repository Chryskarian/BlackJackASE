package model.handlers;

import blackjackobjects.Card;
import blackjackobjects.Person;
import blackjackobjects.PlayingCardDeck;
import databasecommunication.Player;
import model.enums.DrawSituation;

public class CardHandler {


    public PlayingCardDeck deck;

    public PlayingCardDeck getDeck() {
        return deck;
    }

    public void createNewDeck() {
        deck = new PlayingCardDeck();
    }

    public void croupierTurn(Person croupier) {
        do {
            givePlayingCard(croupier);
        } while (croupier.getPointsOnHand() < 17);
        checkIfBust(croupier);
    }

    public DrawSituation checkIfBust(Person person) {
        if (person instanceof Player && ((Player) person).isHand2()) {
            if (((Player) person).getSplitPlayerPoints() > 21) {
                ((Player) person).setBust2(true);
                return DrawSituation.HAND2JA;
            }
            return DrawSituation.NEIN;

        } else if (person.getPointsOnHand() > 21) {
            person.setBust(true);
            return DrawSituation.JA;
        }
        return DrawSituation.NEIN;
    }

    public void givePlayingCard(Person person) {
        checkForAce(person);
        Card card = deck.getCard();
        if (person instanceof Player && ((Player) person).isHand2()) {
            ((Player) person).addToSplitPlayerHand(card);
            ((Player) person).addSplitPlayerPoints(card.getCardValue());
        } else {
            person.addHand(card);
            person.addPointsOnHand(card.getCardValue());
        }
    }

    private void checkForAce(Person person) {
        if (person.getPointsOnHand() >= 11 && deck.viewCard().getCardColour().equals("A")) {
            // wechselt von 11 auf 1 Punkt
            deck.viewCard().switchAce();
        }
        if (person instanceof Player && ((Player) person).isHand2() && ((Player) person).getSplitPlayerPoints() >= 11 &&
                deck.viewCard().getCardColour().equals("A")) {
            // wechselt von 11 auf 1 Punkt
            deck.viewCard().switchAce();
        }
    }

}
