import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;

import static org.junit.Assert.*;

/*
Each requirement must have a test verifying it functions properly.
Each requirement should have an error test to verify it does not crash when receiving invalid input.
Hint: For the requirements that verify data persists between runs, you can check the file contents in the test case.
 */

public class furnitureMarketplaceTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase{
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void testCreateAccount() throws IOException{
            String input = "2\nusername\nemailemail\npassword\n1\n7\n3\n";
            String expected = "emailemail,username,password,buyer,x,x";

            receiveInput(input);
            FurnitureMarketplace.main(new String[0]);

            BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
            String stuOut =  bfr.readLine();
            bfr.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.print("");
            pw.close();
            assertEquals(stuOut, expected);
        }



    }
}

