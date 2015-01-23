package clientBackend.model;

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
}
