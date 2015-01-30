package clientBackend;

/**
 * Serializes and deserializes between the model's internal representation and
 * JSON used for communicating with the server
 */
public class CatanSerializer {
	
	/**
	 * Converts the model's internal representation into a JSON string using the
	 * Gson open-source library
	 * @return a JSON representation of the internal model
	 */
	public String serializeModel() {
		return null;
	}
	
	/**
	 * Converts the given JSON string into the model's internal representation
	 * using the Gson open-source library
	 * @param gson a JSON representation of the internal model
	 */
	public void deserializeModel(String gson) {
		
	}
	
	/**
	 * Converts any Catan object into a json string
	 * @param o the object to be serialized
	 * @return the JSON representation of the object
	 * */
	public String serializeObject(Object o) {
		return null;
	}
	
	/**
	 * Converts any JSON string into a Catan object
	 * @param gson a JSON representation of a Catan object
	 * @return the JSON representation of the object
	 * */
	public Object deserializeObject(String gson) {
		return null;
	}
}
