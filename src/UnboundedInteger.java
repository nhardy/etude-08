import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

/**
 * UnboundedInteger class
 * @author Kimberley Louw, Nathan Hardy
 */
public class UnboundedInteger {
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

    public UnboundedInteger add(UnboundedInteger other) {
        int newSign = 1;
        List<Integer> newMagnitude = new LinkedList<Integer>();
        int position = 0;
        int max = Math.max(magnitude.size(), other.magnitude.size());
        int carry = 0;
        while (position < max || carry > 0) {
            int initialDigit = magnitude.get(position);
            int otherDigit = other.magnitude.get(position);
            int add = initialDigit + otherDigit;
            if (add > 9) {
                carry = 1;
                add = add % 10;
            }
            newMagnitude.set(position, add);
        }

        return new UnboundedInteger(newSign, newMagnitude);
    }

    public UnboundedInteger subtract(UnboundedInteger other) {
        return new UnboundedInteger("0");
    }

    public UnboundedInteger multiply(UnboundedInteger other) {
        return new UnboundedInteger("0");
    }

    public UnboundedInteger divide(UnboundedInteger other) {
        return new UnboundedInteger("0");
    }

    public UnboundedInteger gcd(UnboundedInteger other) {
        return new UnboundedInteger("0");
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
            if(numbers.length < 3) {
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
                    result = num1.divide(num2).toString();
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
                    System.out.println("# Syntax error");
            }
            if (!result.equals("")) {
                System.out.println(line);
                System.out.println("# " + result);
            }
        }
        scanner.close();
    }
}
