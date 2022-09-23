package com.zoomcode.arabic2roman.R2NConv;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

enum RomanNumeral {
    I(1), IV(4), V(5), IX(9), X(10),
    XL(40), L(50), XC(90), C(100),
    CD(400), D(500), CM(900), M(1000);

    private int value;

    RomanNumeral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                .collect(Collectors.toList());
    }
}

@RestController
public class R2NConv {
    @RequestMapping(value="/conv/r2n", method= RequestMethod.GET)
    public String R2N(@RequestParam String num) {
        String romanNumeral = num.toUpperCase();

        int result = 0;
        int i = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            return num + " cannot be converted to a arabic numeral";
            //throw new IllegalArgumentException(num + " cannot be converted to a Roman Numeral");
        }

        return String.valueOf(result);
    }

    @RequestMapping(value="/conv/n2r", method= RequestMethod.GET)
    public String N2R(@RequestParam String num) {

        if (num.isEmpty()) {
            return "Invalid parameter";
            //throw new IllegalArgumentException("Invalid parameter");
        }

        int number = Integer.parseInt(num);
        int i = 0;

        if ((number <= 0) || (number > 4000)) {
            return number + " is not in range (0,4000)";
            //throw new IllegalArgumentException(number + " is not in range (0,4000)");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}
