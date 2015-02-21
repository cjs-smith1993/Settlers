package clientBackend;

import clientBackend.model.CatanException;
import clientBackend.model.Facade;
import clientBackend.transport.TransportModel;

import com.google.gson.Gson;

/**
 * Serializes and deserializes between the model's internal representation and
 * JSON used for communicating with the server
 */
public class CatanSerializer {
	final Gson gson = new Gson();
	private static CatanSerializer serializer;
	private Facade facade;

	public static CatanSerializer getInstance() {
		if (serializer == null) {
			serializer = new CatanSerializer();
		}
		
		return serializer;
	}

	private CatanSerializer() {
		facade = Facade.getInstance();
	}

	/**
	 * Converts the given JSON string into the model's internal representation
	 * using the Gson open-source library.
	 *
	 * @param gsonString
	 *            a JSON representation of the internal model
	 * @throws CatanException
	 */
	public void deserializeModel(String gsonString) throws CatanException {
		TransportModel transportModel = this.gson.fromJson(gsonString, TransportModel.class);

		this.facade.initializeModel(transportModel);
	}

	public void deserializeModel(TransportModel model) throws CatanException {
		this.facade.initializeModel(model);
	}

	/**
	 * Converts any Catan object into a json string
	 *
	 * @param o
	 *            the object to be serialized
	 * @return the JSON representation of the object
	 * */
	public String serializeObject(Object o) {
		String serializedObject = this.gson.toJson(o);

		return serializedObject;
	}

	/**
	 * Converts any JSON string into a Catan object
	 *
	 * @param gsonString
	 *            a JSON representation of a Catan object
	 * @return the JSON representation of the object
	 * */
	public Object deserializeObject(String gsonString) {
		Object deserializedObject = this.gson.fromJson(gsonString, Object.class);

		return deserializedObject;
	}
}
