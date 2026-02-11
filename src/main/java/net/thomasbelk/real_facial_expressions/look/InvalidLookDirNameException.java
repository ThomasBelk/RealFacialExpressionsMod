package net.thomasbelk.real_facial_expressions.look;

public class InvalidLookDirNameException extends Exception {
    public InvalidLookDirNameException(String invalidStr) {
        super("The given look direction name: " + invalidStr + "is not a valid look direction name."
                + "Valid lock directions include:" + LookDir.getAllNamesAsOneString());
    }
}
