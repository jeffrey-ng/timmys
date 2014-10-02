/*
 * [Vendor.java]
 *
 * Summary: enumeration of credit card vendors.
 *
 * Copyright: (c) 1998-2014 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.7+
 *
 * Created with: JetBrains IntelliJ IDEA IDE http://www.jetbrains.com/idea/
 *
 * Version History:
 *  1.8 2008-03-06 convert to jdk 1.5. add build number to title. simplify with enum.
 */
package creditcard;

/**
 * enumeration of credit card vendors.
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.8 2008-03-06 convert to jdk 1.5. add build number to title. simplify with enum.
 * @since 1998
 */
public enum Vendor
    {
        /**
         * enum for insufficient digits
         */
        NOT_ENOUGH_DIGITS( true, "Error: not enough digits", false ),
        /**
         * enum for too many digits
         */
        TOO_MANY_DIGITS( true, "Error: too many digits", false ),
        /**
         * enum for unknown vendor
         */
        UNKNOWN_VENDOR( true, "Error: unknown credit card company", false ),
        DUMMY( true, "dummy", false ),
        /**
         * enum for Amex
         */
        AMEX( false, "Amex", true ),
        /**
         * enum for Diner's club
         */
        DINERS( false, "Diners/Carte Blanche", true ), // includes Carte Blanche
        /**
         * enum for Discover Card
         */
        DISCOVER( false, "Discover", true ),
        /**
         * enum for Enroute
         */
        ENROUTE( false, "enRoute", false ),
        /**
         * enum for JCB
         */
        JCB( false, "JCB", true ),
        /**
         * enum for Mastercard
         */
        MASTERCARD( false, "MasterCard", true ),
        /**
         * enum for Visa
         */
        VISA( false, "Visa", true );

    /**
     * human name for vendor
     */
    private final String name;

    /**
     * true if vendor uses a check digit
     */
    private final boolean hasCheckDigit;

    /**
     * true this enum represents an error condition
     */
    private final boolean isError;

    /**
     * constructor
     *
     * @param name human name for vendor
     */
    Vendor( boolean isError, String name, boolean hasCheckDigit )
        {
        this.isError = isError;
        this.name = name;
        this.hasCheckDigit = hasCheckDigit;
        }

    /**
     * get human name
     *
     * @return human name of vendor
     */
    String getName()
        {
        return name;
        }

    /**
     * does this vendor use a checkdigit?
     *
     * @return true if vendor uses a check digit.
     */
    boolean hasCheckDigit()
        {
        return hasCheckDigit;
        }

    /**
     * does this enum represent an error condition?
     *
     * @return true enum represents an error condition.
     */
    boolean isError()
        {
        return isError;
        }
    }
