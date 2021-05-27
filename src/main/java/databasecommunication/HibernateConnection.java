package databasecommunication;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class HibernateConnection {

	private static final Logger logger = Logger.getLogger(HibernateConnection.class.getName());

	private HibernateConnection() {
	}

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			return new Configuration().configure().buildSessionFactory();
		} catch (HibernateException ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.log(Level.WARNING, "SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
