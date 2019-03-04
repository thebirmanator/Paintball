package net.darkscorner.paintball;

public enum PowerUpEffect {

	JUMP(0), SPEED(1), VOLLEY(2);
	
	private final int effect;
	
	PowerUpEffect(int effect) {
		this.effect = effect;
	}
	
	public int getEffect() {
		return effect;
	}
	
	public PowerUpEffect fromString(String string) {
		switch (string) {
		case "JUMP":
			return JUMP;
		case "SPEED":
			return SPEED;
		case "VOLLEY":
			return VOLLEY;
		default:
			return null;
		}
	}
}
