package nz.ac.vuw.engr300;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AppTest {
  @Test
  void test_01() {
    App a = new App();
    assertTrue(a.getStatus());
  }
}
