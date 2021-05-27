package blackjackobjects;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, String> {
	private final Logger logger = Logger.getLogger(MoneyConverter.class.getName());

	@Override
	public String convertToDatabaseColumn(Money money) {
		String dbString = "";
		if (money == null) {
			try {
				throw new SQLException("Fehler beim auslesen aus der Datenbank");
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Fehler", e);
			}
		} else {
			dbString = money.toString();
			return dbString;
		}
		return dbString;
	}

	@Override
	public Money convertToEntityAttribute(String dbMoney) {
		Money money = null;

		if (dbMoney == null) {
			try {
				throw new SQLException("Fehler beim Konvertieren zum DatenBank Format");
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Fehler", e);
			}
		} else {
			money = Money.toMoney(dbMoney.replace(",", ".").replace("€", ""));
		}
		return money;
	}
}