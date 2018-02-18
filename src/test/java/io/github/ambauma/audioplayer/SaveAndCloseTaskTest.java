package io.github.ambauma.audioplayer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
@PrepareForTest({Files.class, SaveAndCloseTask.class})
public class SaveAndCloseTaskTest {
  private SaveAndCloseTask saveAndCloseTask;
  private AudioPlayer mockAudioPlayer;
  private File mockFile1;

  private Object[] mocks;

  @Before
  public void setup() {
    mockAudioPlayer = PowerMock.createMock(AudioPlayer.class);
    mockFile1 = PowerMock.createMock(File.class);
    PowerMock.mockStatic(Files.class);

    saveAndCloseTask = new SaveAndCloseTask(mockAudioPlayer);

    mocks = new Object[] { mockAudioPlayer, mockFile1, Files.class };
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRunWithNoFiles() {
    EasyMock.expect(mockAudioPlayer.getFiles()).andReturn(new ArrayList<>());
    EasyMock.expect(mockAudioPlayer.getCurrentFile()).andReturn(0);
    try {
      PowerMock.replay(mocks);
      saveAndCloseTask.run();
    } catch (IndexOutOfBoundsException e) {
      PowerMock.verify(mocks);
      throw e;
    }
  }

  @Test
  public void testConstants() {
    assertEquals("/home/ambauma/.audioPlayer", Constants.DATA_PATH_STRING);
    assertEquals(FileSystems.getDefault().getPath("/home/ambauma/.audioPlayer"),
            Constants.DATA_PATH);
  }

  @Test
  public void testRunWithOneFile() throws IOException {
    List<File> files = new ArrayList<>();
    files.add(mockFile1);
    EasyMock.expect(mockAudioPlayer.getFiles()).andReturn(files);
    EasyMock.expect(mockAudioPlayer.getCurrentFile()).andReturn(0);
    EasyMock.expect(mockAudioPlayer.getCurrentPosition()).andReturn(0L);
    EasyMock.expect(mockFile1.getAbsolutePath()).andReturn("/some/path/file1.mp3");
    EasyMock.expect(Files.write(
            EasyMock.and(EasyMock.isA(Path.class), EasyMock.eq(Constants.DATA_PATH)),
            EasyMock.aryEq("/some/path/file1.mp3|0".getBytes()),
            EasyMock.eq(StandardOpenOption.CREATE),
            EasyMock.eq(StandardOpenOption.TRUNCATE_EXISTING))).andReturn(
                    FileSystems.getDefault().getPath("~/.audioPlayer"));
    mockAudioPlayer.setShouldStop(true);
    EasyMock.expectLastCall();
    PowerMock.replay(mocks);
    saveAndCloseTask.run();
    PowerMock.verify(mocks);
  }
}
