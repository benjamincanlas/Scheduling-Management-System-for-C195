package helper;


/**
 * Specifically used for detecting empty spaces in text fields using length. DO NOT USE in Login password text field!
 */
public class WhiteSpaceChecker {
    public static boolean validate(String text) {
        if(text.trim().length() == 0)
            return true;
        return false;

    }

}
