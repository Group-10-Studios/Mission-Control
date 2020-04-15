/**
 * 
 */
package nz.ac.vuw.engr300.importers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Tests for JsonImporter importer class.
 * 
 * @author Nathan Duckett
 */
class JsonImporterTest {

	/**
	 * Test a basic import of the WeatherTestFile. Verify that the root components
	 * are present when imported.
	 */
	@Test
	void test_basic_import() {
		JsonElement contents = null;
		try {
			contents = JsonImporter.load("src/test/resources/WeatherTestFile.json");
		} catch (FileNotFoundException e) {
			fail("Test file was not found as expected");
		}
		
		assertNotNull(contents);
		
		JsonObject objContents = contents.getAsJsonObject();
		
		assertEquals("200", objContents.get("cod").getAsString());
		assertNotNull(objContents.get("message"));
		assertNotNull(objContents.get("cnt"));
		assertNotNull(objContents.get("list"));
		assertNotNull(objContents.get("city"));
	}
	
	
	/**
	 * Test an attempt to load a not found file. Checks the exception is thrown.
	 */
	@Test
	void test_invalid_file() {
		JsonElement contents = null;
		try {
			contents = JsonImporter.load("WeatherTestFile.json");
			fail("Exception not thrown on missing file");
		} catch (FileNotFoundException e) {
			assertNull(contents);
		}
	}
	
	/**
	 * Test the parser throws an error on invalid Json files.
	 */
	@Test
	void test_invalid_json() {
		JsonElement contents = null;
		try {
			contents = JsonImporter.load("src/test/resources/InvalidJsonFile.json");
			fail("Exception not thrown on missing file");
		} catch (JsonParseException e) {
			assertNull(contents);
		} catch (FileNotFoundException e) {
			fail("Test file was not found as expected");
		}
	}
}
