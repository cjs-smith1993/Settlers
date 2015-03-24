package shared.definitions;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public enum PlayerNumber {
	@SerializedName("-1")
	BANK(-1),
	@SerializedName("0")
	ONE(0),
	@SerializedName("1")
	TWO(1),
	@SerializedName("2")
	THREE(2),
	@SerializedName("3")
	FOUR(3);

	private final int value;
	private static HashMap<Integer, PlayerNumber> map = new HashMap<Integer, PlayerNumber>();
	static {
		for (PlayerNumber number : PlayerNumber.values()) {
			map.put(number.value, number);
		}
	}

	private PlayerNumber(int value) {
		this.value = value;
	}

	public int getInteger() {
		return this.value;
	}

	public static PlayerNumber getPlayerNumber(int number) {
		return map.get(number);
	}
}