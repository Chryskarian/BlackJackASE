package tests;

import blackjackobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTest {

    private Money money;
    private final double startMoney = 100.50d;
    final double firstValue = 100d;
    final double secondValue = 0.50d;
    final double thirdValue = 100.99d;

    @BeforeEach
    public void initMoney() {
        money = Money.toMoney(startMoney);
    }

    @Test
    public void testAddition() {
        money = Money.add(money, firstValue);
        money = Money.add(money, secondValue);
        money = Money.add(money, thirdValue);

        assertEquals(startMoney + firstValue + secondValue + thirdValue, money.getAmount().doubleValue());
    }

    @Test
    public void testSubtraction() {
        money = Money.subtract(money, firstValue);
        money = Money.subtract(money, secondValue);
        money = Money.subtract(money, thirdValue);

        assertEquals(startMoney - firstValue - secondValue - thirdValue, money.getAmount().doubleValue());
    }

    @Test
    public void testMultiplication() {
        money = Money.multiply(money, 2);
        money = Money.multiply(money, 3);
        money = Money.multiply(money, 4);

        assertEquals(startMoney * 2 * 3 * 4, money.getAmount().doubleValue());
    }
}
