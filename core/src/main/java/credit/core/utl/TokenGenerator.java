package credit.core.utl;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    public static String generateToken() {
        return generateToken(16);
    }

    public static String generateToken(int charLength) {
        return numberToId(generateDamm(charLength));
    }

    public static String numberToId(final String s) {
        return dashEvery(s, "-", 4);
    }

    public static String stringToId(final String s) {
        if (s == null) {
            return null;
        }
        return numberToId(s.replaceAll("\\D+", "")
                                  .trim());
    }

    private static String generateDamm(final int length) {
        String number = RandomStringUtils.randomNumeric(length - 1);
        return number + DammAlgorithm.checkSum(number);
    }

    private static String dashEvery(String s, String delim, int every) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i % every == 0 && i != 0) {
                sb.append(delim);
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }
}
