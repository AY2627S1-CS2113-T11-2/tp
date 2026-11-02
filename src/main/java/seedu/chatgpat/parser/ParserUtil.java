package seedu.chatgpat.parser;

import seedu.chatgpat.exception.InvalidFormatException;

/**
 * Converts the numbers that the user types in commands.
 * A value that is not a valid number is reported with the command's usage message,
 * so that every command reacts to a mistyped number in the same way.
 */
public final class ParserUtil {
    private ParserUtil() {
        // Utility class with only static methods; it is not meant to be instantiated.
    }

    /**
     * Converts the given text to a whole number.
     *
     * @param value The text typed by the user.
     * @param usage The usage message to show if the text is not a whole number.
     * @return The whole number.
     * @throws InvalidFormatException If the text is not a whole number, or is too large to be stored.
     */
    public static int parseInteger(String value, String usage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(usage);
        }
    }

    /**
     * Converts the given text to a decimal number.
     * Special values such as {@code NaN} and {@code Infinity} are rejected, because they are not real numbers.
     *
     * @param value The text typed by the user.
     * @param usage The usage message to show if the text is not a decimal number.
     * @return The decimal number.
     * @throws InvalidFormatException If the text is not a finite decimal number.
     */
    public static double parseDecimal(String value, String usage) {
        double number;
        try {
            number = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(usage);
        }
        if (!Double.isFinite(number)) {
            throw new InvalidFormatException(usage);
        }
        return number;
    }
}
