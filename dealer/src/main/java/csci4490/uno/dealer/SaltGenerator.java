package csci4490.uno.dealer;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

public class SaltGenerator {

    /* @formatter:off */
    private static final char[] SALT_ALPHABET = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*',
            '(', ')', '~', '`', '+', '-', '=', '_', '{', '[',
            '}', ']', '|', '\\', ':', ';', '\"', '\'', '<',
            ',', '>', '.', '?', '/'
    };
    /* @formatter:on */

    @SuppressWarnings("SameParameterValue")
    public static @NotNull String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(SALT_ALPHABET.length);
            salt.append(SALT_ALPHABET[index]);
        }

        return salt.toString();
    }

}
