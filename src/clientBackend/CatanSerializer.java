package clientBackend;

import clientBackend.transport.TransportModel;

import com.google.gson.Gson;

/**
 * Serializes and deserializes between the model's internal representation and
 * JSON used for communicating with the server
 */
public class CatanSerializer {
	final Gson gson = new Gson();
	
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
	 * using the Gson open-source library.
	 * 
	 * @param gsonString a JSON representation of the internal model
	 */
	public void deserializeModel(String gsonString) {
		TransportModel transportModel = gson.fromJson(gsonString, TransportModel.class);
		
		System.out.println(transportModel);
		
		// TODO: Distribute model information to appropriate class?
	}
	
	/**
	 * Converts any Catan object into a json string
	 * 
	 * @param o the object to be serialized
	 * @return the JSON representation of the object
	 * */
	public String serializeObject(Object o) {
		String serializedObject = gson.toJson(o);
		
		return serializedObject;
	}
	
	/**
	 * Converts any JSON string into a Catan object
	 * @param gsonString a JSON representation of a Catan object
	 * @return the JSON representation of the object
	 * */
	public Object deserializeObject(String gsonString) {
		Object deserializedObject = gson.fromJson(gsonString, Object.class);
		
		return deserializedObject;
	}
}
