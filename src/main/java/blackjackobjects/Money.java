package blackjackobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@SuppressWarnings("serial")
public class Money implements Serializable {

	public static final Currency EUR = Currency.getInstance("EUR");
	private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

	private final BigDecimal amount;
	private final Currency currency;

	public static Money toMoney(Object value) {
		return euro(Money.typeConverter(value));
	}

	public static int compare(Object object, Object object2) {
		int result = 0;

		BigDecimal objectConverted = Money.typeConverter(object);
		BigDecimal object2Converted = Money.typeConverter(object2);
		int bigResult = objectConverted.compareTo(object2Converted);

		// DER ZWEITE WERT IST GRÖßER
		if (bigResult > 0) {
			result = -1;
			// DER ERSTE WERT IST GRÖßER
		} else if (bigResult < 0) {
			result = 1;
			// BEIDE WERTE SIND GLEICH
		} else if (bigResult == 0) {
			result = 0;
		}
		return result;
	}

	public static Money divide(Object value, int divideValue) {
		return euro(Money.typeConverter(value).divide(Money.typeConverter(divideValue)));
	}

	public static Money add(Object value, Object addValue) {
		return euro(Money.typeConverter(value).add(Money.typeConverter(addValue)));
	}

	public static Money subtract(Object value, Object subtractValue) {
		return euro(Money.typeConverter(value).subtract(Money.typeConverter(subtractValue)));
	}

	public static Money multiply(Object value, int multiplyFactor) {
		return euro(Money.typeConverter(value).multiply(Money.typeConverter(multiplyFactor)));
	}

	private static Money euro(BigDecimal amount) {
		return new Money(amount, EUR);
	}

	private Money(BigDecimal amount, Currency currency) {
		this(amount, currency, DEFAULT_ROUNDING);
	}

	private Money(BigDecimal amount, Currency currency, RoundingMode rounding) {
		this.currency = currency;
		this.amount = amount.setScale(currency.getDefaultFractionDigits(), rounding);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	private Currency getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return getAmount() + getCurrency().getSymbol();
	}

	private static final BigDecimal typeConverter(Object inputType) {

		if (inputType instanceof String) {
			return BigDecimal.valueOf(Double.parseDouble((String) inputType));
		} else if (inputType instanceof Integer) {
			return BigDecimal.valueOf(Double.parseDouble(Integer.toString((int) inputType)));
		} else if (inputType instanceof Money) {
			return BigDecimal.valueOf(Money.toDouble((Money) inputType));
		} else if (inputType instanceof Double) {
			return BigDecimal.valueOf((double) inputType);
		}
		return new BigDecimal(0);
	}

	private static double toDouble(Money money) {
		return Double.parseDouble(money.toString().replace("€", ""));
	}
}