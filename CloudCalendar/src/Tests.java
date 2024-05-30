/**
 * @author POO team 2023/24
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

/**
 * The Tests class specifies a set of tests implemented using the JUnit tool. 
 * These tests use Mooshak test files as input and generate the expected result of 
 * running these tests as output. The class is implemented for testing the 
 * Paintball project.
 * To use this class you need to include the JUnit 4 library in your 
 * runtime environment.
 */
public class Tests {
	/**
	 * Use the following lines to specify the tests you are going to carry out. 
	 * For example, the expected result for the 
	 * test 01_in_help.txt is 01_out_help.txt. 
	 * You don't have to do anything else in the rest of the class. 
	 * Just set up this sequence of tests!
	 */
	
	/**
	 * Tests commands help, exit.
	 */
	@Test public void test01() { test("01_in_help.txt", "01_out_help.txt"); }
	
	/**
	 * Tests commands register, exit.
	 */
	@Test public void test02() { test("02_in_register.txt", "02_out_register.txt"); }
	
	/**
	 * Tests commands create, register, exit.
	 */
	@Test public void test03() { test("03_in_create.txt", "03_out_create.txt"); }
	
	/**
	 * Tests commands invite, create, register, exit.
	 */
	@Test public void test04() { test("04_in_invite.txt", "04_out_invite.txt"); }
	
	/**
	 * Tests commands event, invite, create, register, exit.
	 */
	@Test public void test05() { test("05_in_event.txt", "05_out_event.txt"); }
	
	/**
	 * Tests commands response, event, invite, create, register, exit.
	 */
	@Test public void test06() { test("06_in_response.txt", "06_out_response.txt"); }
	
	/**
	 * Tests commands topics, create, register, exit..
	 */
	@Test public void test07() { test("07_in_topics.txt", "07_out_topics.txt"); }
	
	private static final File BASE = new File("tests");

	private PrintStream consoleStream;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setup() {
		consoleStream = System.out;
		System.setOut(new PrintStream(outContent));
	}

	public void test(String input, String output) {
		test(new File(BASE, input), new File(BASE, output));
	}

	public void test(File input, File output) {
		consoleStream.println("Testing!");
		consoleStream.println("Input: " + input.getAbsolutePath());
		consoleStream.println("Output: " + output.getAbsolutePath());

		String fullInput = "", fullOutput = "";
		try {
			fullInput = new String(Files.readAllBytes(input.toPath()));
			fullOutput = new String(Files.readAllBytes(output.toPath()));
			consoleStream.println("INPUT ============");
			consoleStream.println(new String(fullInput));
			consoleStream.println("OUTPUT ESPERADO =============");
			consoleStream.println(new String(fullOutput));
			consoleStream.println("OUTPUT =============");
		} catch(Exception e) {
			e.printStackTrace();
			fail("Erro a ler o ficheiro");
		}

		try {
			Locale.setDefault(Locale.US);
			System.setIn(new FileInputStream(input));
			Class<?> mainClass = Class.forName("Main");
			mainClass.getMethod("main", String[].class).invoke(null, new Object[] { new String[0] });
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro no programa");
		} finally {
			byte[] outPrintBytes = outContent.toByteArray();
			consoleStream.println(new String(outPrintBytes));

			assertEquals(removeCarriages(fullOutput), removeCarriages(new String(outContent.toByteArray())));
		}
	}

	private static String removeCarriages(String s) {
		return s.replaceAll("\r\n", "\n");
	}

}