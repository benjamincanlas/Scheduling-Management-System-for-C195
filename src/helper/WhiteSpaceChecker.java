package helper;

public class WhiteSpaceChecker {
    public static boolean validate(String text) {
        if(text.trim().length() == 0)
            return true;
        return false;

    }

}
