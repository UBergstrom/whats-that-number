package lark.fun.numbers;

/**
* This class accepts digits in character form and uses them to build up a
* character and String representation of a number.  Digits are added and
* removed from the one's column, with all other digits shifting to accommodate
* the changes.
* @version 1.0 2012.10.18
* @author U.C. Bergstrom
*/
class DigitHandler {

// The current implementation of this class allows
// for a maximum value of one sexillion - 1 (10^21 - 1).
public static final int MAX_NUM_DIGITS = 21;

// Arrays of strings to hold the various values needed
// to build up verbal representations of numbers
private static final String[] ONESIDS = {"one", "two", "three", "four",
			"five", "six", "seven", "eight", "nine"};
private static final String[] TEENSIDS = {"ten", "eleven", "twelve", "thirteen",
			"fourteen", "fifteen", "sixteen", "seventeen", 
			"eighteen", "nineteen"};
private static final String[] TENSIDS = {"twenty", "thirty", "forty", "fifty",
				"sixty", "seventy", "eighty", "ninety"};
private static final String[] TRIPLETIDS = {"", "thousand", "million", "billion",
				"trillion", "quadrillion", "quintillion"};

// Class variables
private int currnumdigits;
private char[] digits;

/**
* Constructor: allows for MAX_NUM_DIGITS digits.
*/
DigitHandler() {
	digits = new char[MAX_NUM_DIGITS];
	currnumdigits = 0;
} // DigitHandler() constructor

/**
* @return the current number of digits stored in the DigitHandler
*	instance.
*/
int currNumDigits() {
	return currnumdigits;
} // currNumDigits()

/**
* @return the maximum number of digits that this DigitHandler
*	can store.
*/
int maxNumDigits() {
	return digits.length;
} // maxNumDigits()

/**
* This method adds the input value to the lowest place value column
* (the ones column) and shifts all other digits of the DigitHandler
* instance one place value higher. WARNING: does not check that the
* input character is a decimal digit; data must be cleaned before it
* is input.
* @param A digit, '0' through '9', to add to the DigitHandler.
* @return True if the method call caused the set of digits in the
* 	DigitHandler to be changed, false otherwise.
*/
boolean addDigit(char newdigit){
	if ( (currnumdigits == digits.length) || 
	((currnumdigits == 0) && (newdigit == '0')) ) return false;

	digits[currnumdigits] = newdigit;
	currnumdigits++;
	return true;
} // addDigit(char)

/**
* This method deletes the digit in the lowest place value column
* (the ones column) and shifts all other digits one place value
* lower.
* @return True if the method call caused the set of digits in the
*	DigitHandler to be changed, false otherwise.
*/
boolean deleteDigit() {
	if (currnumdigits == 0) return false;
	currnumdigits--;
	digits[currnumdigits] = '0';
	return true;
} // deleteDigit()

/**
* This method deletes all digits currently stored in the
* DigitHandler instance.
* @return True if the method call caused the set of digits in the
*	DigitHandler to be changed, false otherwise.
*/
boolean clearAll() {
	if (currnumdigits == 0) return false;
	currnumdigits = 0;
	return true;
} // clearAll()

/**
* Formats the DigitHandler instance's digits very lightly, by 
* inserting commas.
* @return the input number in numeric form, with commas.
*/
String toNumericString() {
	if (currnumdigits == 0) return "0";

	// Determine how many commas will be needed.  Then build the
	// leading digits first.  If no commas are needed, we will
	// be done at that point.
	int numcommas = (currnumdigits - 1) / 3;
	int leadingdigits = currnumdigits - numcommas * 3;
	String result = String.copyValueOf(digits, 0, leadingdigits);
	if (numcommas == 0) return result;
		
	// Build the rest, inserting commas as needed.
	StringBuffer resbuf = new StringBuffer(result);
	int offset = leadingdigits;
	for (; numcommas > 0; numcommas--, offset += 3) {
		resbuf.append(',');
		resbuf.append(digits, offset, 3);
	} // for(numcommas)

	return resbuf.toString();
} // toString()

/**
* Converts the DigitHandler instance's digits into a verbal, 
* long-form number, e.g., '12345' becomes "twelve thousand, and 
* three-hundred and forty-five".
* @return the input number in verbal form.
*/
String toVerbalString() {
	if (currnumdigits == 0) return "zero";

	// N.B.: this program builds the string starting from the digit
	// furthest from the decimal.

	// Determine the number of triplets. Then build the 
	// leading digits first.
	int numtriplets = 1 + ((currnumdigits - 1) / 3);
	int leadingdigits = currnumdigits - ((numtriplets - 1) * 3);
	String result = "";

	switch (leadingdigits) {
		case 1:
			result = tripletToString('0', '0', digits[0]);
			break;
		case 2:
			result = tripletToString('0', digits[0], digits[1]);
			break;
		case 3:
			result = tripletToString(digits[0], digits[1], digits[2]);
			break;
	} // switch

	// If there is only one triplet, we are done.
	if (numtriplets == 1) return result;

	// Build rest, triplet by triplet, inserting commas and 
	// triplet identifiers as needed.
	StringBuffer resbuf = new StringBuffer(result);
	resbuf.append(" ");
	resbuf.append(TRIPLETIDS[numtriplets-1]);
	numtriplets--;
	String triplet;
	int offset = leadingdigits;
		
	for (; numtriplets > 0; numtriplets--, offset += 3) {
		triplet = tripletToString(digits[offset], digits[offset+1],
						digits[offset+2]);		
		if (triplet.length() != 0) {
			// In this case, the number continues past the 
			// previous value, so append a comma and space before 
			// the triplet.
			resbuf.append(", ");
			if (numtriplets == 1) {
				// this is the last triplet!
				resbuf.append("and ");
			}
			resbuf.append(triplet);
			
			// Now append the triplet identifier, e.g,
			// "million" or "quadrillion". NB: This is the 
			// empty string for the final triplet (the triplet
			// nearest the decimal point).
			resbuf.append(" ");
			resbuf.append(TRIPLETIDS[numtriplets-1]);
		}
	} // for(numtriplets)
	return resbuf.toString();
} // toVerbalString()

/**
* Converts the three input digits into a verbal form, e.g., '6', '7', '8'
* is converted to "six hundred and seventy-eight".  WARNING: does not 
* check that the input character is a decimal digit; data must be cleaned 
* before it is input.
* @param The hundreds digit, as a char.
* @param The tens digit, as a char.
* @param The ones digit, as a char.
* @return the input triplet of hundreds-tens-ones in verbal form.
*/
static String tripletToString(char hundreds, char tens, char ones) {
	// NB: the numeric values corresponding to the char-typed digits
	// will be found at digits - 48.
	StringBuffer resbuffer = new StringBuffer("");
	int hs = hundreds - 48;
	int ts = tens - 48;
	int os = ones - 48;

	if (hs != 0) {
		resbuffer.append(ONESIDS[hs - 1]);
		resbuffer.append(" hundred");
		if (ts != 0 || os != 0) {
			resbuffer.append(" and ");
		} else {
			return resbuffer.toString();
		}
	}

	if (ts == 0) {
		if (os != 0) {
			resbuffer.append(ONESIDS[os - 1]);
		}
	} else if (ts == 1) {
		resbuffer.append(TEENSIDS[os]);
	} else {
		resbuffer.append(TENSIDS[ts - 2]);
		if (os != 0) {
			resbuffer.append("-");
			resbuffer.append(ONESIDS[os - 1]);
		}
	}
	return resbuffer.toString();
} // tripletToString(char, char, char)

} // class DigitHandler