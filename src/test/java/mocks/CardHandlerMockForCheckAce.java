package mocks;

import blackjackobjects.Person;
import model.handlers.CardHandler;

public class CardHandlerMockForCheckAce extends CardHandler {

    @Override
    public void givePlayingCard(Person person) {
        while (!getDeck().viewCard().getCardColour().equals("A")) {
            getDeck().getCard();
        }
        super.givePlayingCard(person);
    }
}
