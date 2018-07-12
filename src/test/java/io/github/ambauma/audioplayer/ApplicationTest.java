package io.github.ambauma.audioplayer;

import org.junit.Test;

public class ApplicationTest {

  @Test
  public void testRun() throws Exception {
    Application.main (new String[] {"src/test/resources/alphabet/", "100000"});
  }
}
