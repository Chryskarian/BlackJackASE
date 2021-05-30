package tests;

import blackjackobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;



public class MoneyTest {

    private Money money;

    @BeforeEach
    public void initMoney(){
        money = Money.toMoney(100.50d);
    }

    @Test
    public void testAddition(){
        Money.add(money, 100);
        Money.add(money, 0.50);
        Money.add(money, 100.99);


    }

}
