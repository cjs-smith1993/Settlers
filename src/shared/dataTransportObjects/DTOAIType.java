package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.AIType;

public class DTOAIType {
	public AIType AIType;

	public DTOAIType(AIType type) {
		if (type == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.AIType = type;
	}
}
