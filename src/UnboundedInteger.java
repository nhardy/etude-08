import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.*;

/**
 * UnboundedInteger class
 * @author Kimberley Louw, Nathan Hardy
 */
public class UnboundedInteger {
    private boolean isNegative = false;
    private LinkedList<Boolean> bits = new LinkedList<Boolean>();
    private final Pattern stringPattern = Pattern.compile("^(-?)(\\d+)$");
    public UnboundedInteger(String input) {
        Matcher m = stringPattern.matcher(input);

        if (!m.find()) throw new IllegalArgumentException("String: ''" + input + "'' does not match the pattern for UnboundedInteger.");

        isNegative = m.group(1).length() == 1;
        String digits = m.group(2);
    }

    public UnboundedInteger(boolean isNegative, LinkedList<Boolean> bits) {
        this.isNegative = isNegative;
        this.bits = bits;
    }

    public UnboundedInteger add(UnboundedInteger other) {
        UnboundedInteger result;
        return result;
    }

    public UnboundedInteger substract(UnboundedInteger other) {
        UnboundedInteger result;
        return result;
    }

    public UnboundedInteger multiply(UnboundedInteger other) {
        UnboundedInteger result;
        return result;
    }

    public UnboundedInteger divide(UnboundedInteger other) {
        UnboundedInteger result;
        return result;
    }

    public UnboundedInteger gcd(UnboundedInteger other) {
        UnboundedInteger result;
        return result;
    }

    public boolean greaterThan(UnboundedInteger other) {
        boolean result;
        return result;
    }

    public boolean lessThan(UnboundedInteger other) {
        boolean result;
        return result;
    }

    public boolean equals(UnboundedInteger other) {
        boolean result;
        return result;
    }

    public String toString() {
        return "";
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
            String num1 = numbers[0];
            String operand = numbers[1];
            String num2 = numbers[2];
            String result;
            switch(operand) {
                case "+":
                    result = num1.add(num2); 
                    break;
                case "-":
                    result = num1.subtract(num2);
                    break;
                case "*":
                    result = num1.multiply(num2);
                    break;
                case "/":
                    result = num1.divide(num2);
                    break;
                case "gcd":
                    result = num1.gcd(num2);
                    break;
                case ">":
                    result = num1.greaterThan(num2);
                    break;
                case "<":
                    result = num1.lessThan(num2);
                    break;
                case "=":
                    result = num1.equals(num2);
                    break;
                default:
                    System.out.println("# Syntax error");
            }
            if(result != "") {
                System.out.println("# " + result);
            }
        }
        scanner.close();
     }
}
