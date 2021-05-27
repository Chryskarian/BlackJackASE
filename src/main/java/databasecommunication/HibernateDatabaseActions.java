package databasecommunication;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class HibernateDatabaseActions {

	private HibernateDatabaseActions() {
	}

	/**
	 * Nimmt alle Daten aus dem @See Spieler, und fügt diese Daten der DatenBank
	 * hinzu
	 * 
	 * @param player (Player)
	 */

	public static void registerUser(Player player) {
		Session session = HibernateConnection.getSessionFactory().openSession();

		session.beginTransaction();
		session.save(player);
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * Nimmt alle Daten aus @See Spieler, und aktualisiert diese in der DatenBank
	 * 
	 * @param player (Player)
	 */

	public static void saveUser(Player player) {
		Session session = HibernateConnection.getSessionFactory().openSession();

		session.beginTransaction();
		session.update(player);
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * Fügt das neue Passowrt der DatenBank hinzu, dass in vorher generiert wurde
	 * und in @See Spieler gespiechert wurde.
	 * 
	 * @param username   (String)
	 * @param password   (String)
	 * @param salt       (String)
	 * @param iterations (int)
	 */

	public static void passwortVergessen(String username, String password, String salt, int iterations) {

		Session session = HibernateConnection.getSessionFactory().openSession();

		session.beginTransaction();

		Query<?> query = session.createQuery("UPDATE Player SET PASSWORD = :password, SALT = :salt, ITERATIONS = :iterations WHERE USERNAME = :name");
		query.setParameter("password", password);
		query.setParameter("salt", salt);
		query.setParameter("iterations", iterations);
		query.setParameter("name", username);
		query.executeUpdate();

		session.getTransaction().commit();
		session.close();
	}

	/**
	 * Holt die gespeicherten Daten, des zugehörigen Benutzernamen aus der DatenBank
	 * 
	 * @param username (String)
	 * @return player (Player)
	 */

	public static Player anmelden(String username) {
		Session session = HibernateConnection.getSessionFactory().openSession();

		session.beginTransaction();

		Query<?> query = session.createQuery("from Player where username=:name");
		query.setParameter("name", username);
		Player player = (Player) query.uniqueResult();

		session.close();
		return player;
	}

	public static void updateTimeStamp(Timestamp timestamp, String username) {
		Session session = HibernateConnection.getSessionFactory().openSession();

		session.beginTransaction();

		Query<?> query = session.createQuery("UPDATE Player SET DATUM = :date WHERE USERNAME = :name");
		query.setParameter("date", timestamp);
		query.setParameter("name", username);
		query.executeUpdate();

		session.getTransaction().commit();
		session.close();
	}
}