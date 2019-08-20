package iomatix.spigot.rpgleveledmobs.userInterface;

public class MenuException extends Exception {
	private String logMessage;

	public MenuException(final String message) {
		this.logMessage = message;
	}
}
