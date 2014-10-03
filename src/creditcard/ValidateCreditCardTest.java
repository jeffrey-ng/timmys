package creditcard;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateCreditCardTest {

// -------------Testing ParseDirtyLong method---------------
	
	@Test
   	public void testParseDirtyLong1() throws Exception {

        //TestCase1
        String test_case_1 = "12345";
        assertEquals(ValidateCreditCard.parseDirtyLong(test_case_1), 12345L);
   	}
	
   	@Test
   	public void testParseDirtyLong2() throws Exception {

        //TestCase2
        String test_case_2 = "";
        assertEquals(ValidateCreditCard.parseDirtyLong(test_case_2), 0L);
   	}
   	
   	@Test
   	public void testParseDirtyLong3() throws Exception {

        //TestCase3
        String test_case_3 = null;
        expectNullPointerExceptionDirtyLong(test_case_3);
   	}
   	
   	@Test
   	public void testParseDirtyLong4() throws Exception {

        //TestCase4
        String test_case_4 = "-9223372036854775809"; //-2^(63)-1
        expectNumberFormatExceptionDirtyLong(test_case_4);
        
        // fails because number is smaller than MIN_LONG
   	}
   	
   	@Test
   	public void testParseDirtyLong5() throws Exception {

        //TestCase5
        String test_case_5 = "9223372036854775808"; //2^(63)+1
        expectNumberFormatExceptionDirtyLong(test_case_5);
        
        // fails because number is greater than MAX_LONG
   	}
   	
   	@Test
   	public void testParseDirtyLong6() throws Exception {

        //TestCase6
        String test_case_6 = "-12345";
        assertEquals(ValidateCreditCard.parseDirtyLong(test_case_6), -12345L);
   	}
   	
// -------------Testing IsValid method---------------

    @Test
    public void testIsValid1() throws Exception {

        //TestCase1
        long test_case_1 = 4111111111111111L;
        assertTrue(ValidateCreditCard.isValid(test_case_1));
    }
    
    @Test
    public void testIsValid2() throws Exception {

        //TestCase2
        long test_case_2 = 1L;
        assertFalse(ValidateCreditCard.isValid(test_case_2));
    }
    
//    @Test
//    public void testIsValid3() throws Exception {
//
//        //TestCase3
//        long test_case_3 = 12345678901234567890L;
//        assertFalse(ValidateCreditCard.isValid(test_case_3));
//    }
    
//    @Test
//    public void testIsValid4() throws Exception {
//
//        //TestCase4
//        String test_case_4 = "asdfasdfasdfasdf";
//        assertFalse(ValidateCreditCard.isValid(test_case_4));
//    }
    
    @Test
    public void testIsValid5() throws Exception {

        //TestCase5
        long test_case_5 = 0000000000000000L;
        assertFalse(ValidateCreditCard.isValid(test_case_5));
    }
    
    @Test
    public void testIsValid6() throws Exception {

        //TestCase6
        long test_case_6 = 4111111111111112L;
        assertFalse(ValidateCreditCard.isValid(test_case_6));
    }

    private void expectNumberFormatExceptionDirtyLong(String s)
    {
        try {
            ValidateCreditCard.parseDirtyLong(s);
            fail("NumberFormatException should have been thrown");
        } catch (NumberFormatException e) {
            //This should always be executed after the call to parseDirtyLong().
            assertTrue(true);
        }
    }

    private void expectNullPointerExceptionDirtyLong(String s)
    {
        try {
            ValidateCreditCard.parseDirtyLong(s);
            fail("NumberFormatException should have been thrown");

        } catch (NullPointerException e) {
            //This should always be executed after the call to parseDirtyLong().
            assertTrue(true);
        }
    }

}