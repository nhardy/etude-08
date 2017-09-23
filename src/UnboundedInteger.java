import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
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

    public UnboundedInteger truncatedHalf(UnboundedInteger other) {
        UnboundedInteger half = new UnboundedInteger("0");
        half = divide(other);
        return half;
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
                break;
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
        return newMagnitude;
    }

    public UnboundedInteger subtract(UnboundedInteger other) {
        if (sign == 0) {
            return new UnboundedInteger(-1, other.magnitude);
        } else if (other.sign == 0) {
            return new UnboundedInteger(1, magnitude);
        } else if (sign != other.sign) {
            // TODO: Check if this works as intended if other.sign is negative
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

    public UnboundedInteger multiply(UnboundedInteger other) {
        if (equals(ZERO) || other.equals(ZERO)) {
            return ZERO;
        } else if (greaterThan(ZERO) && other.greaterThan(ZERO)) {
            UnboundedInteger count = other;
            UnboundedInteger result = new UnboundedInteger(sign, magnitude);
            while (count.greaterThan(ONE)) {
                result = result.add(this);
                count = count.subtract(ONE);
            }
            return result;
        }
        // } else if (lessThan(ZERO) && other.lessThan(ZERO)) {
        //     UnboundedInteger count = new UnboundedInteger(1, other.magnitude);
        //     List<Integer> resultMag = new LinkedList<Integer>();
        //     resultMag = magnitude;
        //     while (count.greaterThan(ONE)) {
        //         resultMag = add(resultMag, other.magnitude);
        //         count = count.subtract(ONE);
        //     }
        //     UnboundedInteger result = new UnboundedInteger(1, resultMag);
        //     return result;
        // } else {
        //     UnboundedInteger count = other;
        //     List<Integer> resultMag = new LinkedList<Integer>();
        //     resultMag = other.magnitude;
        //     while (count.greaterThan(ONE)) {
        //         resultMag = add(magnitude, other.magnitude);
        //         count = count.subtract(ONE);
        //     }
        //     UnboundedInteger result = new UnboundedInteger(-1, resultMag);
        //     return result;
        // }
        return ZERO;
    }

    public UnboundedInteger divide(UnboundedInteger other) {
        if (equals(ZERO) || other.equals(ZERO)) {
            return ZERO;
        } else if (greaterThan(ZERO) && other.greaterThan(ZERO)) {
            UnboundedInteger count = ZERO;
            UnboundedInteger remainder = ZERO;
            UnboundedInteger result = new UnboundedInteger(sign, magnitude);
            while (result.greaterThan(ZERO)) {
                result = result.subtract(other);
                if (result.lessThan(ZERO)) {
                    remainder = result;
                    System.out.println(remainder);
                }
                count = count.add(ONE);
            }
            return count;
        }
        return ZERO;
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
            if(numbers.length != 3) {
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
