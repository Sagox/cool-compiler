package cool;

public class ErrorReporter {

	public static void reportError(String filename, int lineNo, String errorMessage) {
		System.err.println("ERROR: " + filename + ": " + lineNo + ": "+ errorMessage);
	}

	public static void reportGenericError(String errorMessage) {
		System.err.println("ERROR: "+ errorMessage);
	}
}