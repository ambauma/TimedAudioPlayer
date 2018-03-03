package io.github.ambauma.audioplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

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
    PowerMock.mockStatic(Files.class);
    saveManager = new SaveManager();
    mocks = new Object[] {Files.class, SaveManager.class};
  }

  @Test
  public void testLoadNoFile() {
    PowerMock.mockStatic(Files.class);
    EasyMock.expect(Files.exists(Constants.DATA_PATH)).andReturn(false);

    PowerMock.replay(mocks);
    SavePoint savePoint = saveManager.load();
    PowerMock.verify(mocks);
    assertNotNull(savePoint);
    assertNull(savePoint.getAbsoluteFilePath());
    assertEquals(0L, savePoint.getPosition());
  }

  @Test(expected = RuntimeException.class)
  public void testLoadIOException() throws IOException {
    PowerMock.mockStatic(Files.class);
    EasyMock.expect(Files.exists(Constants.DATA_PATH)).andReturn(true);
    EasyMock.expect(Files.readAllBytes(Constants.DATA_PATH)).andThrow(new IOException("access probably"));

    PowerMock.replay(mocks);
    try {
      saveManager.load();
    } catch (RuntimeException e) {
      PowerMock.verify(mocks);
      throw e;
    }
  }

  @Test
  public void testLoad() throws IOException {
    PowerMock.mockStatic(Files.class);
    EasyMock.expect(Files.exists(Constants.DATA_PATH)).andReturn(true);
    EasyMock.expect(Files.readAllBytes(Constants.DATA_PATH)).andReturn("/some/file/somewhere.mp3|6000".getBytes());

    PowerMock.replay(mocks);
    SavePoint savePoint = saveManager.load();
    PowerMock.verify(mocks);
    assertNotNull(savePoint);
    assertEquals("/some/file/somewhere.mp3", savePoint.getAbsoluteFilePath());
    assertEquals(6000L, savePoint.getPosition());
  }

  @Test
  public void testSave() throws IOException {
    String path = "/some/file/some/where.mp3";
    PowerMock.mockStatic(Files.class);
    byte[] one = path.getBytes();
    byte[] two = "|".getBytes();
    byte[] three = "200".getBytes();
    byte[] combined = new byte[one.length + two.length + three.length];
    System.arraycopy(one, 0, combined, 0, one.length);
    System.arraycopy(two, 0, combined, one.length, two.length);
    System.arraycopy(three, 0, combined, one.length + two.length, three.length);

    EasyMock.expect(Files.write(EasyMock.eq(Constants.DATA_PATH), EasyMock.aryEq(combined),
            EasyMock.eq(StandardOpenOption.CREATE),
            EasyMock.eq(StandardOpenOption.TRUNCATE_EXISTING))).andReturn(Constants.DATA_PATH);
    SavePoint savePoint = new SavePoint();
    savePoint.setAbsoluteFilePath(path);
    savePoint.setPosition(200L);
    PowerMock.replay(mocks);
    saveManager.save(savePoint);
    PowerMock.verify(mocks);
  }

}
