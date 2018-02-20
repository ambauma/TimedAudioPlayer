package io.github.ambauma.audioplayer;

import java.io.IOException;
import java.nio.file.Files;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class, SaveManager.class})
public class SaveManagerTest {
  private SaveManager saveManager;

  private Object[] mocks;

  @Before
  public void setup() {
    saveManager = new SaveManager();
    mocks = new Object[] {Files.class, SaveManager.class};
  }

  @Test
  public void testGetAbsoluteFilePathFileDoesNotExist() {
    PowerMock.mockStatic(Files.class);
    EasyMock.expect(Files.exists(Constants.DATA_PATH)).andReturn(false);

    PowerMock.replay(mocks);
    saveManager.getAbsoluteFilePath();
    PowerMock.verify(mocks);
  }

  @Test(expected = RuntimeException.class)
  public void testGetAbsoluteFilePathIOException() throws IOException {
    PowerMock.mockStatic(Files.class);
    EasyMock.expect(Files.exists(Constants.DATA_PATH)).andReturn(true);
    EasyMock.expect(Files.readAllBytes(Constants.DATA_PATH)).andThrow(new IOException("access probably"));

    PowerMock.replay(mocks);
    try {
      saveManager.getAbsoluteFilePath();
    } catch (RuntimeException e) {
      PowerMock.verify(mocks);
      throw e;
    }
  }

  @Test
  public void testGetAbsoluteFilePath() throws IOException {
    PowerMock.mockStatic(Files.class);
    EasyMock.expect(Files.exists(Constants.DATA_PATH)).andReturn(true);
    EasyMock.expect(Files.readAllBytes(Constants.DATA_PATH)).andReturn("/some/file/somewhere.mp3|6000".getBytes());

    PowerMock.replay(mocks);
    saveManager.getAbsoluteFilePath();
    PowerMock.verify(mocks);
  }

}
