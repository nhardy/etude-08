import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.*;

/**
 * UnboundedInteger class
 * @author Kimberley Louw, Nathan Hardy
 */
public class UnboundedInteger {
    private boolean isNegative = false;
    private LinkedList<Integer> data = new LinkedList<Integer>();
    private final Pattern stringPattern = Pattern.compile("^(-?)(\\d+)$");
    public UnboundedInteger(String input) {
        Matcher m = stringPattern.matcher(input);

        if (!m.find()) throw new IllegalArgumentException("String: ''" + input + "'' does not match the pattern for UnboundedInteger.");

        isNegative = m.group(1).length() == 1;

        ArrayList<Character> digits = new ArrayList<Character>();
        for (char digit : m.group(2).toCharArray()) {
            digits.add(digit);
        }
        Collections.reverse(digits);

        int place = 1;
        for (char digit : digits) {
            int part = Integer.parseInt("" + digit);
            place++;
        }
    }

    public UnboundedInteger(boolean isNegative, LinkedList<Integer> data) {
        this.isNegative = isNegative;
        this.data = data;
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
        }
        scanner.close();
     }
}
