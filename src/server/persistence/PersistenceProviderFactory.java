package server.persistence;

/**
 * This factory returns the proper {@link IPersistenceProvider}, based on
 * whether the implementation based on the database implementation.
 */
public class PersistenceProviderFactory {
	/**
	 * This method is where you pass in a key that matches to
	 * a value in the config file. For example, the name will be SQL
	 * or NoSQL. This key will retrieve an associated jar's file path that 
	 * will be used to instantiate the correct IPersistenceProvider. This
	 * IPersistenceProvider will then be returned.
	 * @param name A file name for the associated plugin .jar
	 * @return {@link IPersistenceProvider}
	 */
	public IPersistenceProvider getPersistenceProvider(String name) {
		// TODO: Implement in the afterlife.
		return null;
	}
}
