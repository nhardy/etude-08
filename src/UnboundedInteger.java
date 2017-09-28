/**
 * @author Kimberley Louw, Nathan Hardy
 */

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

/**
 * UnboundedInteger class
 */
public class UnboundedInteger {
    /**
     * UnboundedInteger constant for {@code 0}
     */
    private static UnboundedInteger ZERO = new UnboundedInteger("0");

    /**
     * Sign for the UnboundedInteger. {@code -1} for negative, {@code 0} for {@code 0}, and {@code 1} for postive numbers.
     */
    private int sign;
    /**
     * List of magnitude components. Currently, these are single decimal digit integers.
     */
    private List<Integer> magnitude = new LinkedList<Integer>();

    /**
     * Creates an UnboundedInteger from a String representation
     * @param number String representation
     */
    public UnboundedInteger(String number) {
        // If there is a negative sign at the first
        // character, we have a negative number. Otherwise,
        // the number is positive (except for 0).
        int negIndex = number.lastIndexOf('-');
        sign = negIndex == 0 ? -1 : 1;

        // If the first character after the negative sign (or
        // the first character if there is none) is a zero,
        // set the sign to 0 and let the magnitude consist of
        // a single zero digit.
        if (number.charAt(negIndex + 1) == '0') {
            sign = 0;
            magnitude.add(0);
            return;
        }

        // For each digit in the input, starting from least
        // significant, add that digit to the magnitude List
        for (int i = number.length() - 1; i > negIndex; i--) {
            magnitude.add(Integer.parseInt(number.substring(i, i + 1)));
        }
    }

    /**
     * Creates an UnboundedInteger from a {@code sign} and {@code magnitude}
     * @param sign Sign variable
     * @param magnitude Magnitude component List
     */
    private UnboundedInteger(int sign, List<Integer> magnitude) {
        this.sign = sign;
        this.magnitude = magnitude;
    }

    /**
     * Compares the magnitude of the current UnboundedInteger with that of {@code other}
     * @param other Other UnboundedInteger for comparison
     * @return Integer consistent with a comparator
     */
    private int compareMagnitude(UnboundedInteger other) {
        // If the size of the magnitude list is greater than that of other
        if (magnitude.size() > other.magnitude.size()) return 1;
        // If the size of the magnitude list is less than that of other
        if (magnitude.size() < other.magnitude.size()) return -1;
        // If same length, loop through each digit from most significant to least
        for (int i = magnitude.size() - 1; i >= 0; i--) {
            // If the digit is greater than that of other
            if (magnitude.get(i) > other.magnitude.get(i)) return 1;
            // If the digit is lesser than that of other
            if (magnitude.get(i) < other.magnitude.get(i)) return -1;
        }
        // The magnitudes are identical
        return 0;
    }

    /**
     * Returns the absolute value of an UnboundedInteger, without using the internal variables
     * @param number The UnboundedInteger on which to operate
     * @return Absolute value of {@code number}
     */
    private static UnboundedInteger abs(UnboundedInteger number) {
        // Note: This would be more efficient by setting the
        // internal sign variable to its own absolute value
        // but requirements for this etude mandated that the
        // consumers of this method not modify internal variables

        // If {@code this} >= 0, return as-is
        if (number.equals(ZERO) || number.greaterThan(ZERO)) return number;
        // Otherwise, subtract the current number from zero
        return ZERO.subtract(number);
    }

    /**
     * Returns the sign number of an UnboundedInteger, without using the internal sign
     * @param number The UnboundedInteger on which to operate
     * @return int representation of {@code number}'s sign
     */
    private static int sign(UnboundedInteger number) {
        if (number.equals(ZERO)) return 0;
        if (number.greaterThan(ZERO)) return 1;
        return -1;
    }

    /**
     * Truncated halving operation required by program specification
     * @return Half of the current UnboundedInteger
     */
    public UnboundedInteger truncatedHalf() {
        return divide(new UnboundedInteger("2")).getQuotient();
    }

    /**
     * Returns a new magnitude List from the addition of two other magnitude Lists
     * @param magnitude1 first number
     * @param magnitude2 second number
     * @return List of magnitude components
     */
    private static List<Integer> add(List<Integer> magnitude1, List<Integer> magnitude2) {
        List<Integer> newMagnitude = new LinkedList<Integer>();
        // The position indicates the digits being added
        int position = 0;
        // max equals the highest number of digits between the two numbers
        int max = Math.max(magnitude1.size(), magnitude2.size());
        int carry = 0;
        // loops over each digit in the numbers
        while (position < max || carry > 0) {
            int initialDigit;
            int otherDigit;
            // initialises the initialDigit and otherDigit to the digit
            // at position or 0 if one number has less digits than the other
            if (position >= magnitude1.size()) {
                initialDigit = 0;
            } else {
                initialDigit = magnitude1.get(position);
            }
            if (position >= magnitude2.size()) {
                otherDigit = 0;
            } else {
                otherDigit = magnitude2.get(position);
            }
            // add the digits and any carry values from the previous added digits
            int add = initialDigit + otherDigit + carry;
            // reset carry to 0
            carry = 0;
            // if the added digits is greater than 9 then add to carry
            if (add > 9) {
                carry = 1;
                add = add % 10; // returns the ones digit as tens is added to carry
            }
            newMagnitude.add(add); // adds the digit to newMagnitude
            position += 1; // increase the position of the digits
        }
        return newMagnitude; // returns the result of the added magnitudes
    }

    /**
     * Returns a new UnboundedInteger, with the {@code other} added to {@code this}.
     * @param other Other UnboundedInteger
     * @return Result
     */
    public UnboundedInteger add(UnboundedInteger other) {
        // Test to see if one number is zero, if so return the other number
        if (sign == 0) return new UnboundedInteger(1, other.magnitude);
        if (other.sign == 0) return new UnboundedInteger(1, magnitude);
        // if both signs are the same add the absolute values and return the original sign.
        if (sign == other.sign) return new UnboundedInteger(sign, add(magnitude, other.magnitude));

        List<Integer> newMagnitude = new LinkedList<Integer>();
        // if the signs are different compare the absolute values
        // then subtract the smaller value from the larger value
        int cmp = compareMagnitude(other);
        if (cmp == 0) {
            // if the absolute values are the same return zero
            return ZERO;
        } else if (cmp > 0) {
            newMagnitude = subtract(magnitude, other.magnitude);
        } else {
            newMagnitude = subtract(other.magnitude, magnitude);
        }
        // return new UnboundedInteger with the new absolute value and the sign of the larger magnitude
        return new UnboundedInteger(cmp == sign ? 1 : -1, newMagnitude);
    }

    /**
     * Returns a new magnitude List from the subtraction of one magnitude List from another
     * @param magnitude1 first number
     * @param magnitude2 second number
     * @return List of magnitude components
     */
    private static List<Integer> subtract(List<Integer> magnitude1, List<Integer> magnitude2) {
        List<Integer> newMagnitude = new LinkedList<Integer>();
        // The position indicates the digits being subtracted
        int position = 0;
        // max equals the highest number of digits between the two numbers
        int max = Math.max(magnitude1.size(), magnitude2.size());
        int borrow = 0;
        // loops over each digit in the numbers
        while (position < max || borrow > 0) {
            int initialDigit;
            int otherDigit;
            // initialises the initialDigit and otherDigit to the digit
            // at position or 0 if one number has less digits than the other
            if (position >= magnitude1.size()) {
                initialDigit = 0;
            } else {
                initialDigit = magnitude1.get(position);
            }
            if (position >= magnitude2.size()) {
                otherDigit = 0;
            } else {
                otherDigit = magnitude2.get(position);
            }
            // checks if previous digits needed to borrow from next digits
            if (borrow == 1) {
                initialDigit -= 1; // minus borrowed digit off
                borrow = 0; // resets borrow
            }
            int subtract;
            // if initialDigit is greater than otherDigit then subtract
            if (initialDigit > otherDigit) {
                subtract = initialDigit - otherDigit;
            // if initialDigit is less than otherDigit need to borrow from next digit
            } else if (initialDigit < otherDigit) {
                borrow = 1;
                initialDigit += 10; // add borrowed 10 to initial digit
                subtract = initialDigit - otherDigit;
                newMagnitude.add(subtract); // add digit to newMagnitude
                position += 1; // increase position
                continue;
            } else {
                subtract = 0; // if both digits are the same then return 0
            }

            newMagnitude.add(subtract); // add digit to newMagnitude
            position += 1; // increase position
        }
        // removes any leading zeros from subtracting a number with less digits
        // from a number with more digits
        while (newMagnitude.get(newMagnitude.size() - 1) == 0 && newMagnitude.size() > 1) {
            newMagnitude.remove(newMagnitude.size() - 1);
        }
        return newMagnitude; // returns the result of the added magnitudes
    }

    /**
     * Returns a new UnboundedInteger, with the {@code other} subtracted from {@code this}.
     * @param other Other UnboundedInteger
     * @return Result
     */
    public UnboundedInteger subtract(UnboundedInteger other) {
        // Test to see if one number is zero and other number is positive
        // if so return the other number as a negative
        if (sign == 0 && other.sign == 1) return new UnboundedInteger(-1, other.magnitude);
        // Test to see if one number is zero and other number is negative
        // if so return the other number as a positive
        if (sign == 0 && other.sign == -1)  return new UnboundedInteger(1, other.magnitude);
        // Test to see if other number is zero, if so return original number
        if (other.sign == 0) return new UnboundedInteger(1, magnitude);
        // Test to see if both signs are different
        // if so add absolute values and return original sign
        if (sign != other.sign) return new UnboundedInteger(sign, add(magnitude, other.magnitude));

        List<Integer> newMagnitude = new LinkedList<Integer>();
        // if the signs are the same compare the absolute values
        // then subtract the smaller value from the larger value
        int cmp = compareMagnitude(other);
        if (cmp == 0) {
            // if the absolute values are the same return zero
            newMagnitude.add(0);
            return new UnboundedInteger(0, newMagnitude);
        } else if (cmp > 0) {
            newMagnitude = subtract(magnitude, other.magnitude);
        } else {
            newMagnitude = subtract(other.magnitude, magnitude);
        }
        // return new UnboundedInteger with the new absolute value and the sign of the larger magnitude
        return new UnboundedInteger(cmp == sign ? 1 : -1, newMagnitude);
    }

    /**
     * Returns the current UnboundedInteger multiplied by 10. Used for place-value shifting.
     * @return Result
     */
    private UnboundedInteger multiplyBy10() {
        UnboundedInteger result = ZERO;
        // Adds {@code this} 10 times
        for (int i = 0; i < 10; i++) {
            result = result.add(this);
        }
        return result;
    }

    /**
     * Returns a new UnboundedInteger which is the result of {@code this} multiplied by {@code other}.
     * @param other Other UnboundedInteger
     * @return Result
     */
    public UnboundedInteger multiply(UnboundedInteger other) {
        // If either number is zero, the result will be zero
        if (equals(ZERO) || other.equals(ZERO)) return ZERO;

        // The resulting sign will be the product of the two signs
        int sign = sign(this) * sign(other);

        // Grab the absolute value of each number
        UnboundedInteger absThis = abs(this);
        UnboundedInteger absOther = abs(other);

        // Sort the two numbers so that we can iterate over the smaller number for speed
        boolean thisGtOther = absThis.greaterThan(absOther);
        UnboundedInteger greater = thisGtOther ? absThis : absOther;
        UnboundedInteger lesser = thisGtOther ? absOther : absThis;

        UnboundedInteger absResult = ZERO;

        // We aren't allowed to access the underlying magnitude List
        // for this method, so we convert the number to a string and
        // operate on each digit accordingly.
        char[] lesserDigitChars = lesser.toString().toCharArray();
        ArrayList<Character> lesserDigits = new ArrayList<Character>();
        for (int i = lesserDigitChars.length - 1; i >= 0; i--) {
            lesserDigits.add(lesserDigitChars[i]);
        }

        // Multiply using shift and add
        for (int i = 0; i < lesserDigits.size(); i++) {
            int digitAtPlace = Integer.parseInt("" + lesserDigits.get(i));
            UnboundedInteger partialSum = ZERO;
            for (int j = 0; j < digitAtPlace; j++) {
                partialSum = partialSum.add(greater);
            }

            // Shift result by 10^i
            for (int j = 0; j < i; j++) {
                partialSum = partialSum.multiplyBy10();
            }

            // Add
            absResult = absResult.add(partialSum);
        }

        // If the new sign is positive, we can just return the result
        if (sign == 1) return absResult;

        // If the new sign is negative, make the result negative
        // by subtracting the absolute result from zero
        return ZERO.subtract(absResult);
    }

    /**
     * Returns the quotient and remainder for {@code this} divided by {@code other}.
     * @param other Other UnboundedInteger
     * @return QuotientAndRemainder object containing the quotient and remainder
     */
    public QuotientAndRemainder divide(UnboundedInteger other) {
        // If the dividend is zero, throw an Exception
        if (other.equals(ZERO)) throw new ArithmeticException("Divide by zero");

        // Zero divided by any nonzero integer is zero remainder zero
        if (equals(ZERO)) return new QuotientAndRemainder(ZERO, ZERO);

        // The resulting sign will be the product of the sign of the dividend and divisor
        int sign = sign(this) * sign(other);

        // Grab the absolute value of each number
        UnboundedInteger absDividend = abs(this);
        UnboundedInteger absDivisor = abs(other);

        // We aren't allowed to access the underlying magnitude List
        // for this method, so we convert the number to a string and
        // operate on each digit accordingly.
        char[] dividendCharArray = absDividend.toString().toCharArray();
        ArrayList<Character> dividendChars = new ArrayList<Character>();
        for (int i = 0; i < dividendCharArray.length; i++) {
            dividendChars.add(dividendCharArray[i]);
        }

        String quotientDigits = "";
        String remainderDigits = "";

        // Last successful division occurred at this position in the dividendChars list
        int successful = 0;

        // Keep advancing one digit at a time through the dividend
        for (int i = 0; i < dividendChars.size(); i++) {
            // Carry down the digits of the previous remainder
            String partialDividendString = remainderDigits;
            // Clear the remainder digits
            remainderDigits = "";

            // Build up the string of digits for the part of the dividend that
            // we're interested in - from the last successful division index
            // to the current char in the dividend (index i)
            for (int j = successful; j < i + 1; j++) {
                partialDividendString += dividendChars.get(j);
            }
            // Turn the String into a new UnboundedInteger
            UnboundedInteger partialDividend = new UnboundedInteger(partialDividendString);
            // Count the number of times our divisor fits into the partial dividend
            int count = 0;

            // While our divisor is less than or equal to the partial dividend
            while (absDivisor.lessThan(partialDividend) || absDivisor.equals(partialDividend)) {
                // Advance the successful division marker one space from the current char of the divisor
                successful = i + 1;
                // Subtract the divisor from the partial dividend
                partialDividend = partialDividend.subtract(absDivisor);

                // If the partial dividend reaches zero, we divided evenly, so the remainder to carry down is empty
                if (partialDividend.equals(ZERO)) {
                    remainderDigits = "";
                } else {
                    // Otherwise, copy the remaining digits to be carried down
                    remainderDigits = partialDividend.toString();
                }

                // Add one to the division count
                count++;
            }

            // If the partial dividend could not be divided, or we've already started recording digits
            if (count != 0 || quotientDigits.length() != 0) {
                // Add the digits to the quotient
                quotientDigits += count;
            }
        }

        // Convert the quotient string to an UnboundedInteger
        UnboundedInteger quotientMagnitude = quotientDigits.isEmpty() ? ZERO : new UnboundedInteger(quotientDigits);
        // Convert the remainder string to an UnboundedInteger
        UnboundedInteger remainder = remainderDigits.isEmpty() ? ZERO : new UnboundedInteger(remainderDigits);

        // If the sign of the result should be negative, subtract the magnitude from zero
        if (sign != 1) {
            quotientMagnitude = ZERO.subtract(quotientMagnitude);
        }

        // Store the result in the QuotientAndRemainder container class
        return new QuotientAndRemainder(quotientMagnitude, remainder);
    }

    /**
     * Returns the greatest common divisor of {@code this} and {@code other}. Uses Euclid's algorithm.
     * @param other Other UnboundedInteger
     * @return Greatest Common Divisor
     */
    public UnboundedInteger gcd(UnboundedInteger other) {
        // If the numbers are equal, they are their own GCD
        if (equals(other)) return this;

        // Sort the numbers
        boolean thisGtOther = this.greaterThan(other);
        UnboundedInteger divisor = thisGtOther ? other : this;
        if (divisor.equals(ZERO)) return divisor;
        UnboundedInteger dividend = thisGtOther ? this : other;

        do {
            QuotientAndRemainder qr = dividend.divide(divisor);
            dividend = divisor;
            divisor = qr.getRemainder();
        } while (!divisor.equals(ZERO));

        return dividend;
    }

    /**
     * Returns whether or not {@code this} is greater than {@code other}
     * @param other Other UnboundedInteger
     * @return Boolean
     */
    public boolean greaterThan(UnboundedInteger other) {
        if (sign > other.sign) return true;
        if (sign < other.sign) return false;
        return compareMagnitude(other) > 0;
    }

    /**
     * Returns whether or not {@code this} is less than {@code other}
     * @param other Other UnboundedInteger
     * @return Boolean
     */
    public boolean lessThan(UnboundedInteger other) {
        if (sign < other.sign) return true;
        if (sign > other.sign) return false;
        return compareMagnitude(other) < 0;
    }

    /**
     * Returns whether or not {@code this} is equal to {@code other}
     * @param other Other UnboundedInteger
     * @return Boolean
     */
    public boolean equals(UnboundedInteger other) {
        return sign == other.sign && magnitude.equals(other.magnitude);
    }

    /**
     * Returns a string representation of the current UnboundedInteger
     * @return String representation
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int digit : magnitude) {
            stringBuilder.insert(0, Integer.toString(digit));
        }

        if (sign == -1) stringBuilder.insert(0, "-");

        return stringBuilder.toString();
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        // All input statements contain an operand, followed
        // by an operator, followed by another operand
        Pattern INPUT_FORMAT = Pattern.compile("^(-?\\d+) ([\\+\\-\\/\\*<>=]|gcd) (-?\\d+)$");

        Scanner scanner = new Scanner(System.in);
        // loops of scaned in input
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);

            // If line is blank, or starts with a '#',
            // continue to the next line of input
            if (line.equals("") || line.charAt(0) == '#') continue;

            Matcher matcher = INPUT_FORMAT.matcher(line);
            if (!matcher.find()) {
                System.out.println("# Syntax error");
                continue;
            }

            // assign numbers to num1 and num2 and operator to operand
            UnboundedInteger num1 = new UnboundedInteger(matcher.group(1));
            String operand = matcher.group(2);
            UnboundedInteger num2 = new UnboundedInteger(matcher.group(3));
            String result = "";
            try {
                // switch statement for each operator to call specific operator method
                switch (operand) {
                    // calls add method to add the numbers
                    case "+":
                        result = num1.add(num2).toString();
                        break;
                    // calls subtract method to subtract the numbers
                    case "-":
                        result = num1.subtract(num2).toString();
                        break;
                    // calls multiply method to mulitply the numbers
                    case "*":
                        result = num1.multiply(num2).toString();
                        break;
                    // calls divide method to divide the numbers with remainder
                    case "/":
                        QuotientAndRemainder tmp = num1.divide(num2);
                        result = tmp.getQuotient() + " " + tmp.getRemainder();
                        break;
                    // calls gcd method to find the greatest common divisor
                    case "gcd":
                        result = num1.gcd(num2).toString();
                        break;
                    // calls greaterThan method and returns true or false
                    case ">":
                        result = "" + num1.greaterThan(num2);
                        break;
                    // calls lessThan method and returns true or false
                    case "<":
                        result = "" + num1.lessThan(num2);
                        break;
                    // calls equals method and returns true or false
                    case "=":
                        result = "" + num1.equals(num2);
                        break;
                }
                System.out.println("# " + result);
            } catch (ArithmeticException e) {
                // Catches division by 0
                System.out.println("# Syntax error");
            }
        }
        scanner.close();
    }

    /**
     * Container class for Quotient and Remainder
     */
    private class QuotientAndRemainder {
        private UnboundedInteger quotient;
        private UnboundedInteger remainder;

        /**
         * @param quotient Quotient
         * @param remainder Remainder
         */
        public QuotientAndRemainder(UnboundedInteger quotient, UnboundedInteger remainder) {
            this.quotient = quotient;
            this.remainder = remainder;
        }

        /**
         * Returns the Quotient
         * @return quotient component
         */
        public UnboundedInteger getQuotient() {
            return quotient;
        }

        /**
         * Returns the Remainder
         * @return remainder component
         */
        public UnboundedInteger getRemainder() {
            return remainder;
        }
    }
}
