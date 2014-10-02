/*
 * [ValidateCreditCard.java]
 *
 * Summary: Handles calculations to validate credit card numbers and determine which credit card company they belong to.
 *
 * Adapted from material provided by:
 * Copyright: (c) 1999-2014 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          http://mindprod.com/contact/nonmil.html
 *
*/
package creditcard;

import static creditcard.Vendor.AMEX;
import static creditcard.Vendor.DINERS;
import static creditcard.Vendor.DISCOVER;
import static creditcard.Vendor.ENROUTE;
import static creditcard.Vendor.JCB;
import static creditcard.Vendor.MASTERCARD;
import static creditcard.Vendor.NOT_ENOUGH_DIGITS;
import static creditcard.Vendor.TOO_MANY_DIGITS;
import static creditcard.Vendor.UNKNOWN_VENDOR;
import static creditcard.Vendor.VISA;

/**
 * Handles calculations to validate credit card numbers and determine which credit card company they belong to.
 * <p/>
 * 1. if a credit card number is valid,
 * 2. which credit card vendor handles that number.
 * <p/>
 * It validates the prefix and the checkdigit. It does
 * not* contact the credit card company to ensure that number
 * has actually been issued and that the account is in good
 * standing.
 * <p/>
 * It will also tell you which of the following credit card
 * companies issued the card: Amex, Diners Club, Carte Blanche,
 * Discover, enRoute, JCB, MasterCard or Visa.
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.3 1999-08-19 ignore dashes in numbers
 * @since 1999-08-17
 */
public final class ValidateCreditCard
    {
    /**
     * Used to speed up findMatchingRange by caching the last hit.
     */
    private static int cachedLastFind;

    /**
     * ranges of credit card number that belong to each company. buildRanges initialises.
     */
    private static LCR[] ranges;

    static
        {
        // now that all enum constants defined
        buildRanges();
        }

    /**
     * Determine if the credit card number is valid, i.e. has good prefix and checkdigit. Does _not_ ask the credit card
     * company if this card has been issued or is in good standing.
     *
     * @param creditCardNumber number on card.
     *
     * @return true if card number is good.
     */
    public static boolean isValid( long creditCardNumber )
        {
        Vendor vendor = matchVendor( creditCardNumber );
        if ( vendor.isError() )
            {
            return false;
            }
        else
            {
            // we have a match
            if ( vendor.hasCheckDigit() )
                {
                // there is a checkdigit to be validated
                long number = creditCardNumber;
                int checksum = 0;
                // work right to left
                for ( int place = 0; place < 16; place++ )
                    {
                    int digit = ( int ) ( number % 10 );
                    number /= 10;
                    if ( ( place & 1 ) == 0 )
                        {
                        // even position (0-based from right), just add digit
                        checksum += digit;
                        }
                    else
                        {// odd position (0-based from right), must double
                        // and add
                        checksum += z( digit );
                        }
                    if ( number == 0 )
                        {
                        break;
                        }
                    } // end for
                // good checksum should be 0 mod 10
                return ( checksum % 10 ) == 0;
                }
            else
                {
                return true;// no checksum needed
                }
            } // end if have match
        } // end isValid

    /**
     * Finds a matching range in the ranges array for a given creditCardNumber.
     *
     * @param creditCardNumber number on card.
     *
     * @return index of matching range, or NOT_ENOUGH_DIGITS or UNKNOWN_VENDOR on failure.
     */
    public static Vendor matchVendor( long creditCardNumber )
        {
        if ( creditCardNumber < 1000000000000L )
            {
            return NOT_ENOUGH_DIGITS;
            }
        if ( creditCardNumber > 9999999999999999L )
            {
            return TOO_MANY_DIGITS;
            }
        // check the cached index first, where we last found a number.
        if ( ranges[ cachedLastFind ].low <= creditCardNumber
             && creditCardNumber <= ranges[ cachedLastFind ].high )
            {
            return ranges[ cachedLastFind ].vendor;
            }
        for ( int i = 0; i < ranges.length; i++ )
            {
            if ( ranges[ i ].low <= creditCardNumber
                 && creditCardNumber <= ranges[ i ].high )
                {
                // we have a match
                cachedLastFind = i;
                return ranges[ i ].vendor;
                }
            } // end for
        return UNKNOWN_VENDOR;
        } // end matchVendor

    /**
     * convert a String to a long. The routine is very forgiving. It ignores invalid chars, lead trail, embedded spaces,
     * decimal points etc.
     *
     * @param numStr the String containing the number to be converted to long.
     *
     * @return long value of the string found, ignoring junk characters. May be negative.
     * @throws NumberFormatException if the number is too big to fit in a long.
     */
    public static long parseDirtyLong( String numStr )
        {
        numStr = numStr.trim();
        // strip commas, spaces, + etc
        // StringBuilder is better than FastCat for char by char work.
        StringBuilder b = new StringBuilder( numStr.length() );
        for ( int i = 0, n = numStr.length(); i < n; i++ )
            {
            char c = numStr.charAt( i );
            if ( '0' < c && c <= '9' )
                {   b.append( c );   }
            } // end for
        numStr = b.toString();
        if ( numStr.length() == 0 )
            {   return 0;   }
        return Long.parseLong( numStr );
        } // end parseDirtyLong

    /**
     * Convert a creditCardNumber as long to a formatted String. Currently it breaks 16-digit numbers into groups of 4.
     *
     * @param creditCardNumber number on card.
     *
     * @return String representation of the credit card number.
     */
    public static String toPrettyString( long creditCardNumber )
        {
        String plain = Long.toString( creditCardNumber );
        int length = plain.length();
        switch ( length )
            {
            case 12:
                // 12 pattern 3-3-3-3
                return plain.substring( 0, 3 )
                       + ' '
                       + plain.substring( 3, 6 )
                       + ' '
                       + plain.substring( 6, 9 )
                       + ' '
                       + plain.substring( 9, 12 );
            case 13:
                // 13 pattern 4-3-3-3
                return plain.substring( 0, 4 )
                       + ' '
                       + plain.substring( 4, 7 )
                       + ' '
                       + plain.substring( 7, 10 )
                       + ' '
                       + plain.substring( 10, 13 );
            case 14:
                // 14 pattern 2-4-4-4
                return plain.substring( 0, 2 )
                       + ' '
                       + plain.substring( 2, 6 )
                       + ' '
                       + plain.substring( 6, 10 )
                       + ' '
                       + plain.substring( 10, 14 );
            case 15:
                // 15 pattern 3-4-4-4
                return plain.substring( 0, 3 )
                       + ' '
                       + plain.substring( 3, 7 )
                       + ' '
                       + plain.substring( 7, 11 )
                       + ' '
                       + plain.substring( 11, 15 );
            case 16:
                // 16 pattern 4-4-4-4
                return plain.substring( 0, 4 )
                       + ' '
                       + plain.substring( 4, 8 )
                       + ' '
                       + plain.substring( 8, 12 )
                       + ' '
                       + plain.substring( 12, 16 );
            case 17:
                // 17 pattern 1-4-4-4-4
                return plain.substring( 0, 1 )
                       + ' '
                       + plain.substring( 1, 5 )
                       + ' '
                       + plain.substring( 5, 9 )
                       + ' '
                       + plain.substring( 9, 13 )
                       + ' '
                       + plain.substring( 13, 17 );
            default:
                // 0..11, 18+ digits long
                // plain
                return plain;
            } // end switch
        } // end toPrettyString

    /**
     * build table of which ranges of credit card number belong to which vendor
     */
    private static void buildRanges()
        {
        // careful, no lead zeros allowed
        // low high len vendor
        ranges = new LCR[] {
                new LCR( 4000000000000L, 4999999999999L/* 13 */, VISA ),
                new LCR( 30000000000000L, 30599999999999L/* 14 */, DINERS ),
                new LCR( 36000000000000L, 36999999999999L/* 14 */, DINERS ),
                new LCR( 38000000000000L, 38999999999999L/* 14 */, DINERS ),
                new LCR( 180000000000000L, 180099999999999L/* 15 */, JCB ),
                new LCR( 201400000000000L, 201499999999999L/* 15 */, ENROUTE ),
                new LCR( 213100000000000L, 213199999999999L/* 15 */, JCB ),
                new LCR( 214900000000000L, 214999999999999L/* 15 */, ENROUTE ),
                new LCR( 340000000000000L, 359999999999999L/* 15 */, AMEX ),
                new LCR( 370000000000000L, 379999999999999L/* 15 */, AMEX ),
                new LCR( 3000000000000000L, 3999999999999999L/* 16 */, JCB ),
                new LCR( 4000000000000000L, 4999999999999999L/* 16 */, VISA ),
                new LCR( 5100000000000000L, 5599999999999999L/* 16 */, MASTERCARD ),
                new LCR( 6011000000000000L, 6011999999999999L/* 16 */, DISCOVER ) };
        }

    /**
     * used in computing checksums, doubles and adds resulting digits.
     *
     * @param digit the digit to be doubled, and digit summed.
     *
     * @return // 0->0 1->2 2->4 3->6 4->8 5->1 6->3 7->5 8->7 9->9
     */
    private static int z( int digit )
        {
        if ( digit == 0 )
            {
            return 0;
            }
        else
            {
            return ( digit * 2 - 1 ) % 9 + 1;
            }
        }

    /**
     * @param args not used
     */
    public static void main( String[] args )
        {
        } // end main
    }

/**
 * Describes a single Legal Card Range
 */
final class LCR
    {
    /**
     * enumeration credit card service
     */
    public final Vendor vendor;

    /**
     * low and high bounds on range covered by this vendor
     */
    public final long high;

    /**
     * low bounds on range covered by this vendor
     */
    public final long low;

    /**
     * public constructor
     *
     * @param low    lowest credit card number in range.
     * @param high   highest credit card number in range
     * @param vendor enum constant for vendor
     */
    public LCR( long low,
                long high,
                Vendor vendor
    )
        {
        this.low = low;
        this.high = high;
        this.vendor = vendor;
        } // end public constructor
    }
