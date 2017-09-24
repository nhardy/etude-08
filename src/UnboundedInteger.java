import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.lang.Math;

/**
 * UnboundedInteger class
 * @author Kimberley Louw, Nathan Hardy
 */
public class UnboundedInteger {
    private static UnboundedInteger ZERO = new UnboundedInteger("0");
    private static UnboundedInteger ONE = new UnboundedInteger("1");
    private int sign;
    private List<Integer> magnitude = new LinkedList<Integer>();

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

    public UnboundedInteger(int sign, List<Integer> magnitude) {
        this.sign = sign;
        this.magnitude = magnitude;
    }

    private int compareMagnitude(UnboundedInteger other) {
        if (magnitude.size() > other.magnitude.size()) return 1;
        if (magnitude.size() < other.magnitude.size()) return -1;
        for (int i = magnitude.size() - 1; i >= 0; i--) {
            if (magnitude.get(i) > other.magnitude.get(i)) return 1;
            if (magnitude.get(i) < other.magnitude.get(i)) return -1;
        }
        return 0;
    }

    private static UnboundedInteger abs(UnboundedInteger number) {
        if (number.equals(ZERO) || number.greaterThan(ZERO)) return number;
        return ZERO.subtract(number);
    }

    private static int sign(UnboundedInteger number) {
        if (number.equals(ZERO)) return 0;
        if (number.greaterThan(ZERO)) return 1;
        return -1;
    }

    public UnboundedInteger truncatedHalf() {
        return divide(new UnboundedInteger("2")).getQuotient();
    }

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

    public UnboundedInteger add(UnboundedInteger other) {
        if (sign == 0) {
            return new UnboundedInteger(1, other.magnitude);
        } else if (other.sign == 0) {
            return new UnboundedInteger(1, magnitude);
        } else if (sign == other.sign) {
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
            if (borrow == 1) {
                subtract -= borrow;
            }
            borrow = 0;
            newMagnitude.add(subtract);
            position += 1;
        }
        while (newMagnitude.get(newMagnitude.size()-1) == 0) {
            newMagnitude.remove(newMagnitude.size()-1);
        }
        return newMagnitude;
    }

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
        System.out.println("cmp: " + cmp);
        if (cmp == 0) {
            newMagnitude.add(0);
            return new UnboundedInteger(0, newMagnitude);
        } else if (cmp > 0) {
            System.out.println("mag: " + magnitude + " otherMag: " + other.magnitude);
            newMagnitude = subtract(magnitude, other.magnitude);
            System.out.println("newMag: " + newMagnitude);
        } else {
            newMagnitude = subtract(other.magnitude, magnitude);
        }
        return new UnboundedInteger(cmp == sign ? 1 : -1, newMagnitude);
    }

    private UnboundedInteger multiplyBy10() {
        UnboundedInteger result = ZERO;
        for (int i = 0; i < 10; i++) {
            result = result.add(this);
        }
        return result;
    }

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

    public QuotientAndRemainder divide(UnboundedInteger other) {
        if (other.equals(ZERO)) throw new ArithmeticException("Divide by zero");
        QuotientAndRemainder zero = new QuotientAndRemainder(ZERO, ZERO);
        if (equals(ZERO)) return zero;

        int sign = sign(this) * sign(other);
        UnboundedInteger num1 = abs(this);
        UnboundedInteger num2 = abs(other);
        UnboundedInteger count = ZERO;
        UnboundedInteger remainder = ZERO;
        UnboundedInteger tmp = ZERO;
        System.out.println("sign: " + sign);
        System.out.println("num1: " + num1);
        System.out.println("num2: " + num2);

        while (num1.greaterThan(ZERO)) {
            System.out.println("num1: " + num1 + " - num2: " + num2);
            tmp = num1.subtract(num2);
            System.out.println("= tmp: " + tmp);
            if (tmp.lessThan(ZERO)) {
                remainder = num1.subtract(ZERO);
                System.out.println("remainder: " + remainder);
                if (remainder.lessThan(ZERO)) {
                    remainder = ZERO;
                }
                num1 = tmp;
            } else {
                num1 = tmp;
                count = count.add(ONE);
                System.out.println("num1 in loop: " + num1);
                System.out.println("count: " + count);
            }
        }

        if (sign != 1) {
            count = ZERO.subtract(count);
        }

        QuotientAndRemainder result = new QuotientAndRemainder(count, remainder);

        return result;
    }

    public UnboundedInteger gcd(UnboundedInteger other) {
        if (equals(other)) return this;

        boolean thisGtOther = this.greaterThan(other);
        UnboundedInteger lower = thisGtOther ? other : this;
        if (lower.equals(ZERO)) return lower;
        UnboundedInteger higher = thisGtOther ? this : other;

        do {
            QuotientAndRemainder qr = higher.divide(lower);
            higher = qr.getQuotient();
            lower = qr.getRemainder();
        } while (!lower.equals(ZERO));

        return higher;
    }

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
