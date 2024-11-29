
import java.util.Arrays;
import java.util.Scanner;

// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th avenue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

public class CityMap {
  // Checks for string consisting of all digits
  // An easier solution would use String method matches()
  private static boolean allDigits(String s) {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return true;
  }

  // Get all parts of address string
  // An easier solution would use String method split()
  // Other solutions are possible - you may replace this code if you wish
  private static String[] getParts(String address) {
    String parts[] = new String[3];

    if (address == null || address.length() == 0) {
      parts = new String[0];
      return parts;
    }
    int numParts = 0;
    Scanner sc = new Scanner(address);
    while (sc.hasNext()) {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length + 1);

      parts[numParts] = sc.next();
      numParts++;
    }
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  // Checks for a valid address
  public static boolean validAddress(String address) {
    // Fill in the code
    // Make use of the helper methods above if you wish
    // There are quite a few error conditions to check for
    // e.g. number of parts != 3
    String[] arrayli = getParts(address);
    if (arrayli.length != 3) {// to check if the string is of length 3, and if more than return false
      return false;
    }
    if (arrayli[1].length() > 3) {// is the length of the 1 index greater than 3 then return false
      return false;
    }
    if (arrayli[0].length() > 2) {// is the length of array at 0 index iss greater than 2 then return false
      return false;
    }
    if (!allDigits(arrayli[0].substring(0, 1))) {
      return false;// to check if the first index has all digits in it
    }
    if (!arrayli[1].endsWith("st") && !arrayli[1].endsWith("nd") && !arrayli[1].endsWith("rd")
        && !arrayli[1].endsWith("th")) {// to check if the last two characters are st or nd or rd or th
      return false;
    }
    if (!arrayli[2].equalsIgnoreCase("street") && !arrayli[2].equalsIgnoreCase("avenue")) {// to check if the last one
                                                                                           // is street or avenue
      return false;
    } else {
      return true;
    }
  }

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4]
  // where 3 is the avenue and 4 the street
  // See comments at the top for a more detailed explanation
  public static int[] getCityBlock(String address) {

    String pa[] = getParts(address);
    int j = Character.getNumericValue((pa[0].charAt(0)));
    int k = Character.getNumericValue((pa[1].charAt(0)));// to get the character
    String str = pa[2];
    if (str.equalsIgnoreCase("avenue")) {
      int blk[] = { k, j };
      return blk;
    }

    else if (str.equalsIgnoreCase("street")) {
      int blk[] = { j, k };
      return blk;

    } else {
      return null;
    }
    // Fill in the code

  }

  // Calculates the distance in city blocks between the 'from' address and 'to'
  // address
  // Hint: be careful not to generate negative distances

  // This skeleton version generates a random distance
  // If you do not want to attempt this method, you may use this default code
  public static int getDistance(String from, String to) {
    // Fill in the code or use this default code below. If you use
    // the default code then you are not eligible for any marks for this part

    // Math.random() generates random number from 0.0 to 0.999
    // Hence, Math.random()*17 will be from 0.0 to 16.999
    int l[] = getCityBlock(from);
    int o[] = getCityBlock(to);
    int i = (Math.abs(l[0] - o[0]) + Math.abs(l[1] - o[1]));
    return i;
    // cast the double to whole number

  }
}
