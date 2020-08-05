package converter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int sourceRadix;
        int targetRadix;

        if (scan.hasNextInt()) {
            sourceRadix = scan.nextInt();
            if (sourceRadix < 1 || sourceRadix > 36) {
                System.out.println("Error : SourceRadix MUST be between 1 and 36)");
                return;
            }
        } else {
            System.out.println("Error : expected an Integer for SourceRadix");
            return;
        }

        String sourceNumber = scan.next();

        if (scan.hasNextInt()) {
            targetRadix = scan.nextInt();
            if (targetRadix < 1 || targetRadix > 36) {
                System.out.println("Error : TargetRadix MUST be between 1 and 36)");
                return;
            }
        }  else {
            System.out.println("Error : expected an Integer for TargetRadix");
            return;
        }

        String targetNumber = convertSourceToTarget(sourceRadix, sourceNumber, targetRadix);

        System.out.println(targetNumber);
    }

    private static String convertSourceToTarget(int sourceRadix, String sourceNumber, int targetRadix) {
        // Process Integer portion of sourceNumber
        String sourceInteger = sourceNumber.split("\\.")[0];
        String targetInteger = convertInteger(sourceRadix, sourceInteger, targetRadix);

        // Process Fractional portion of SourceNumber (if it exists)
        if (sourceNumber.split("\\.").length > 1) {
            String sourceFractional = "." + sourceNumber.split("\\.")[1];
            String targetFractional = convertFractional(sourceRadix, sourceFractional, targetRadix);

            return targetInteger + "." + targetFractional;
        } else {
            return targetInteger;
        }
    }

    private static String convertInteger(int sourceRadix, String sourceInteger, int targetRadix) {
        int sourceNumberDecimal;

        // Convert Source to Decimal first (if not already)
        if (sourceRadix == 1) {
            sourceNumberDecimal = sourceInteger.length();
        }
        else if (sourceRadix != 10) { //Convert to decimal first
            sourceNumberDecimal = Integer.parseInt(sourceInteger, sourceRadix);
        } else { //Source Number is already in decimal
            sourceNumberDecimal = Integer.parseInt(sourceInteger);
        }

        StringBuilder targetNumber = new StringBuilder();
        // Convert Decimal Source Number to Target Base
        if (targetRadix == 1) {
            targetNumber.append("1".repeat(Math.max(0, sourceNumberDecimal)));
        } else if (targetRadix != 10) { //Convert from decimal to target base
            targetNumber = new StringBuilder(Integer.toString(sourceNumberDecimal, targetRadix));
        } else { //Target is decimal
            targetNumber = new StringBuilder(Integer.toString(sourceNumberDecimal));
        }

        return targetNumber.toString();
    }

    private static String convertFractional(int sourceRadix, String sourceFractional, int targetRadix) {
        String sourceFractionDec;

        // Convert Source to Decimal first (if not already)
        if (sourceRadix != 10) { //Convert to decimal first
            sourceFractionDec = convertFractionToDecimal(sourceRadix, sourceFractional);
        } else { // Source Number is already in decimal
            sourceFractionDec = sourceFractional;
        }

        String targetFractional;
        // Convert Decimal Value to Target Base
        if (targetRadix != 10) { // Convert from decimal to target base
            targetFractional = convertFractionToTarget(sourceFractionDec, targetRadix);
        } else { // Target is decimal
            targetFractional = sourceFractionDec;
        }

        return targetFractional;
    }

    private static String convertFractionToDecimal(int sourceRadix, String sourceFractional) {
        int decimalDigit;
        float decimalValue = 0;

        for (int i = 1; i < sourceFractional.length(); i++) { // Skip first element as = '.'
                decimalDigit = Character.digit(sourceFractional.charAt(i), sourceRadix);
            decimalValue += decimalDigit / Math.pow(sourceRadix, i);
        }

        return Float.toString(decimalValue);
    }

    private static String convertFractionToTarget(String sourceFractionDec, int targetRadix) {
        float result;
        float fraction = Float.parseFloat(sourceFractionDec);

        int integer;
        StringBuilder targetFraction = new StringBuilder();

        for (int i = 0; i < 5; i++) { // Limit to 5 fractional digits
            result = fraction * targetRadix;
            integer = (int)Math.floor(result);
            fraction = result - integer;

            if (integer >= 10) { //Convert to proper alphanumeric
                targetFraction.append(Integer.toString(integer, targetRadix));
            } else {
                targetFraction.append(integer);
            }
        }

        return targetFraction.toString();
    }
}
