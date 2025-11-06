package sk.ukf.sep.util;

<<<<<<< HEAD
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PasswordUtil {
    private static final String UPPER   = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWER   = "abcdefghijkmnopqrstuvwxyz";
    private static final String DIGITS  = "23456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+";
    private static final String ALL     = UPPER + LOWER + DIGITS + SYMBOLS;
    private static final SecureRandom RAND = new SecureRandom();

    private PasswordUtil() {}

    public static String generate(int length) {
        if (length < 8) length = 8;
        List<Character> chars = new ArrayList<>();
        chars.add(pick(UPPER)); chars.add(pick(LOWER));
        chars.add(pick(DIGITS)); chars.add(pick(SYMBOLS));
        for (int i = chars.size(); i < length; i++) chars.add(pick(ALL));
        Collections.shuffle(chars, RAND);
        StringBuilder sb = new StringBuilder(length);
        for (char c : chars) sb.append(c);
        return sb.toString();
    }

    private static char pick(String s) { return s.charAt(RAND.nextInt(s.length())); }
=======
public class PasswordUtil {
>>>>>>> 29be1dc50e9b87c59bed4dc9dd1a677775042f24
}
