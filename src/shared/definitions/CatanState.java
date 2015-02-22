package shared.definitions;

import com.google.gson.annotations.SerializedName;

public enum CatanState {
	@SerializedName("Rolling")ROLLING,
	@SerializedName("Robbing")ROBBING,
	@SerializedName("Playing")PLAYING,
	@SerializedName("Discarding")DISCARDING,
	@SerializedName("FirstRound")FIRST_ROUND,
	@SerializedName("SecondRound")SECOND_ROUND
}
