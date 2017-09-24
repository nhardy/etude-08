import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

/**
 * UnboundedInteger class
 * @author Kimberley Louw, Nathan Hardy
 */
public class UnboundedInteger {
    /**
     * UnboundedInteger constant for {@code 0}
     */
    private static UnboundedInteger ZERO = new UnboundedInteger("0");
    /**
     * UnboundedInteger constant for {@code 1}
     */
    private static UnboundedInteger ONE = new UnboundedInteger("1");

    /**
     * Sign for the UnboundedInteger. {@code -1} for negative, {@code 0} for {@code 0}, and {@code 1} for postive numbers.
     */
    private int sign;
    /**
     * List of magnitude components. Currently, these are single decimal digit integers.
     */
    private List<Integer> magnitude = new LinkedList<Integer>();

    /**
     * Creates an UnboundedInteger from a String representaion.
     * @param number String representation
     */
    public UnboundedInteger(String number) {
        int negIndex = number.lastIndexOf('-');
        sign = negIndex == 0 ? -1 : 1;

        if (number.charAt(negIndex + 1) == '0') {
            sign = 0;
            magnitude.add(0);
            return;
        }

        for (int i = number.length() - 1; i > negIndex; i--) {
            magnitude.add(Integer.parseInt(number.substring(i, i + 1)));
        }
    }

    /**
     * Creates an UnboundedInteger from a {@code sign} and {@code magnitude}
     * @param sign Sign variable
     * @param magnitude Magnitude component List
     */
    public UnboundedInteger(int sign, List<Integer> magnitude) {
        this.sign = sign;
        this.magnitude = magnitude;
    }

    /**
     * Compares the magnitude of the current UnboundedInteger with that of {@code other}
     * @param other Other UnboundedInteger for comparison
     * @return Integer consistent with a comparator
     */
    private int compareMagnitude(UnboundedInteger other) {
        if (magnitude.size() > other.magnitude.size()) return 1;
        if (magnitude.size() < other.magnitude.size()) return -1;
        for (int i = magnitude.size() - 1; i >= 0; i--) {
            if (magnitude.get(i) > other.magnitude.get(i)) return 1;
            if (magnitude.get(i) < other.magnitude.get(i)) return -1;
        }
        return 0;
    }

    /**
     * Returns the absolute value of an UnboundedInteger
     * @param number The UnboundedInteger on which to operate
     * @return Absolute value of {@code number}
     */
    private static UnboundedInteger abs(UnboundedInteger number) {
        if (number.equals(ZERO) || number.greaterThan(ZERO)) return number;
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
        int position = 0;
        int max = Math.max(magnitude1.size(), magnitude2.size());
        int carry = 0;
        while (position < max || carry > 0) {
            int initialDigit;
            int otherDigit;
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
            int add = initialDigit + otherDigit + carry;
            carry = 0;
            if (add > 9) {
                carry = 1;
                add = add % 10;
            }
            newMagnitude.add(add);
            position += 1;
        }
        return newMagnitude;
    }

    /**
     * Returns a new UnboundedInteger, with the {@code other} added to {@code this}.
     * @param other Other UnboundedInteger
     * @return Result
     */
    public UnboundedInteger add(UnboundedInteger other) {
        if (sign == 0) return new UnboundedInteger(1, other.magnitude);
        if (other.sign == 0) return new UnboundedInteger(1, magnitude);
        if (sign == other.sign) return new UnboundedInteger(sign, add(magnitude, other.magnitude));

        List<Integer> newMagnitude = new LinkedList<Integer>();
        int cmp = compareMagnitude(other);
        if (cmp == 0) {
            newMagnitude.add(0);
            return new UnboundedInteger(0, newMagnitude);
        } else if (cmp > 0) {
            newMagnitude = subtract(magnitude, other.magnitude);
        } else {
            newMagnitude = subtract(other.magnitude, magnitude);
        }
        return new UnboundedInteger(cmp == sign ? 1 : -1, newMagnitude);
    }

    private static List<Integer> subtract(List<Integer> magnitude1, List<Integer> magnitude2) {
        List<Integer> newMagnitude = new LinkedList<Integer>();
        int position = 0;
        int max = Math.max(magnitude1.size(), magnitude2.size());
        int borrow = 0;
        while (position < max || borrow > 0) {
            int initialDigit;
            int otherDigit;
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

            if (borrow == 1) {
                initialDigit -= 1;
                borrow = 0;
            }
            int subtract;
            if (initialDigit > otherDigit) {
                subtract = initialDigit - otherDigit;
            } else if (initialDigit < otherDigit) {
                borrow = 1;
                initialDigit += 10;
                subtract = initialDigit - otherDigit;
                newMagnitude.add(subtract);
                position += 1;
                continue;
            } else {
                subtract = 0;
            }

            newMagnitude.add(subtract);
            position += 1;
        }
        while (newMagnitude.get(newMagnitude.size()-1) == 0 && newMagnitude.size() > 1) {
            newMagnitude.remove(newMagnitude.size()-1);
        }
        return newMagnitude;
    }

    /**
     * Returns a new UnboundedInteger, with the {@code other} subtracted from {@code this}.
     * @param other Other UnboundedInteger
     * @return Result
     */
    public UnboundedInteger subtract(UnboundedInteger other) {
        if (sign == 0 && other.sign == 1) {
            return new UnboundedInteger(-1, other.magnitude);
        } else if (sign == 0 && other.sign == -1) {
            return new UnboundedInteger(1, other.magnitude);
        }else if (other.sign == 0) {
            return new UnboundedInteger(1, magnitude);
        } else if (sign != other.sign) {
            return new UnboundedInteger(sign, add(magnitude, other.magnitude));
        }
        List<Integer> newMagnitude = new LinkedList<Integer>();
        int cmp = compareMagnitude(other);
        if (cmp == 0) {
            newMagnitude.add(0);
            return new UnboundedInteger(0, newMagnitude);
        } else if (cmp > 0) {
            newMagnitude = subtract(magnitude, other.magnitude);
        } else {
            newMagnitude = subtract(other.magnitude, magnitude);
        }
        return new UnboundedInteger(cmp == sign ? 1 : -1, newMagnitude);
    }

    /**
     * Returns the current UnboundedInteger multiplied by 10. Used for place-value shifting.
     * @return Result
     */
    private UnboundedInteger multiplyBy10() {
        UnboundedInteger result = ZERO;
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
        if (equals(ZERO) || other.equals(ZERO)) return ZERO;

        int sign = sign(this) * sign(other);
        UnboundedInteger absThis = abs(this);
        UnboundedInteger absOther = abs(other);

        boolean thisGtOther = absThis.greaterThan(absOther);
        UnboundedInteger greater = thisGtOther ? absThis : absOther;
        UnboundedInteger lesser = thisGtOther ? absOther : absThis;
        UnboundedInteger absResult = ZERO;

        // We aren't allowed to access the underlying magnitude ArrayList
        // for this method, so we convert the number to a string and
        // operate on each digit accordingly.
        char[] lesserDigitChars = lesser.toString().toCharArray();
        ArrayList<Character> lesserDigits = new ArrayList<Character>();
        for (int i = lesserDigitChars.length - 1; i >= 0; i--) {
            lesserDigits.add(lesserDigitChars[i]);
        }

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

        if (sign == 1) return absResult;

        return ZERO.subtract(absResult);
    }

    /**
     * Returns the quotient and remainder for {@code this} divided by {@code other}.
     * @param other Other UnboundedInteger
     * @return QuotientAndRemainder object containing the quotient and remainder
     */
    public QuotientAndRemainder divide(UnboundedInteger other) {
        if (other.equals(ZERO)) throw new ArithmeticException("Divide by zero");
        QuotientAndRemainder zero = new QuotientAndRemainder(ZERO, ZERO);
        if (equals(ZERO)) return zero;

        int sign = sign(this) * sign(other);
        UnboundedInteger absDividend = abs(this);
        UnboundedInteger absDivisor = abs(other);

        char[] dividendCharArray = absDividend.toString().toCharArray();
        ArrayList<Character> dividendChars = new ArrayList<Character>();
        for (int i = 0; i < dividendCharArray.length; i++) {
            dividendChars.add(dividendCharArray[i]);
        }

        String quotientDigits = "";
        String remainderDigits = "";

        int successful = 0;

        for (int i = 0; i < dividendChars.size(); i++) {
            String partialDividendString = remainderDigits;
            remainderDigits = "";
            for (int j = successful; j < i + 1; j++) {
                partialDividendString += dividendChars.get(j);
            }
            UnboundedInteger partialDividend = new UnboundedInteger(partialDividendString);
            UnboundedInteger count = ZERO;

            while (absDivisor.lessThan(partialDividend) || absDivisor.equals(partialDividend)) {
                successful = i + 1;
                partialDividend = partialDividend.subtract(absDivisor);

                if (partialDividend.equals(ZERO)) {
                    remainderDigits = "";
                } else {
                    remainderDigits = partialDividend.toString();
                }

                count = count.add(ONE);
            }

            if (!count.equals(ZERO) || quotientDigits.length() != 0) {
                quotientDigits += count.toString();
            }
        }

        UnboundedInteger quotientMagnitude = quotientDigits.isEmpty() ? ZERO : new UnboundedInteger(quotientDigits);
        UnboundedInteger remainder = remainderDigits.isEmpty() ? ZERO : new UnboundedInteger(remainderDigits);

        if (sign != 1) {
            quotientMagnitude = ZERO.subtract(quotientMagnitude);
        }

        QuotientAndRemainder result = new QuotientAndRemainder(quotientMagnitude, remainder);

        return result;
    }

    /**
     * Returns the greatest common divisor of {@code this} and {@code other}. Uses Euclid's algorithm.
     * @param other Other UnboundedInteger
     * @return Greatest Common Divisor
     */
    public UnboundedInteger gcd(UnboundedInteger other) {
        if (equals(other)) return this;

        boolean thisGtOther = this.greaterThan(other);
        UnboundedInteger lower = thisGtOther ? other : this;
        if (lower.equals(ZERO)) return lower;
        UnboundedInteger higher = thisGtOther ? this : other;

        do {
            QuotientAndRemainder qr = higher.divide(lower);
            higher = lower;
            lower = qr.getRemainder();
        } while (!lower.equals(ZERO));

        return higher;
    }

    /**
     * Returns whether or not {@code this} is greater than {@code other}
     */
    public boolean greaterThan(UnboundedInteger other) {
        if (sign > other.sign) return true;
        if (sign < other.sign) return false;
        return compareMagnitude(other) > 0;
    }

    public boolean lessThan(UnboundedInteger other) {
        if (sign < other.sign) return true;
        if (sign > other.sign) return false;
        return compareMagnitude(other) < 0;
    }

    public boolean equals(UnboundedInteger other) {
        return sign == other.sign && magnitude.equals(other.magnitude);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int digit : magnitude) {
            stringBuilder.insert(0, Integer.toString(digit));
        }

        if (sign == -1) stringBuilder.insert(0, "-");

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.charAt(0) == '#') {
                System.out.println(line);
                continue;
            }
            String[] numbers = line.split("\\s+");
            if(numbers.length != 3) {
                System.out.println(line);
                System.out.println("# Syntax error");
                continue;
            }
            UnboundedInteger num1 = new UnboundedInteger(numbers[0]);
            String operand = numbers[1];
            UnboundedInteger num2 = new UnboundedInteger(numbers[2]);
            String result = "";
            switch(operand) {
                case "+":
                    result = num1.add(num2).toString();
                    break;
                case "-":
                    result = num1.subtract(num2).toString();
                    break;
                case "*":
                    result = num1.multiply(num2).toString();
                    break;
                case "/":
                    QuotientAndRemainder tmp = num1.divide(num2);
                    result = tmp.getQuotient() + " r " + tmp.getRemainder();
                    break;
                case "gcd":
                    result = num1.gcd(num2).toString();
                    break;
                case ">":
                    result = "" + num1.greaterThan(num2);
                    break;
                case "<":
                    result = "" + num1.lessThan(num2);
                    break;
                case "=":
                    result = "" + num1.equals(num2);
                    break;
                default:
                    System.out.println(line);
                    System.out.println("# Syntax error");
            }
            if (!result.equals("")) {
                System.out.println(line);
                System.out.println("# " + result);
            }
        }
        scanner.close();
    }

    private class QuotientAndRemainder {
        private UnboundedInteger quotient;
        private UnboundedInteger remainder;

        public QuotientAndRemainder(UnboundedInteger quotient, UnboundedInteger remainder) {
            this.quotient = quotient;
            this.remainder = remainder;
        }

        public UnboundedInteger getQuotient() {
            return quotient;
        }

        public UnboundedInteger getRemainder() {
            return remainder;
        }
    }
}
