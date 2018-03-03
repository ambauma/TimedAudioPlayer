package io.github.ambauma.audioplayer;

import java.io.File;
import java.nio.file.Files;
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
@PrepareForTest({SaveAndCloseTask.class})
public class SaveAndCloseTaskTest {
  private SaveAndCloseTask saveAndCloseTask;
  private AudioPlayer mockAudioPlayer;
  private SaveManager mockSaveManager;
  private File mockFile;

  private Object[] mocks;

  @Before
  public void setup() {
    mockAudioPlayer = PowerMock.createMock(AudioPlayer.class);
    mockSaveManager = PowerMock.createMock(SaveManager.class);
    mockFile = PowerMock.createMock(File.class);
    PowerMock.mockStatic(Files.class);

    saveAndCloseTask = new SaveAndCloseTask(mockAudioPlayer, mockSaveManager);

    mocks = new Object[] { mockAudioPlayer, mockSaveManager, mockFile};
  }

  @Test
  public void testRun() {
    List<File> files = new ArrayList<>();
    files.add(mockFile);
    EasyMock.expect(mockAudioPlayer.getFiles()).andReturn(files);
    EasyMock.expect(mockAudioPlayer.getCurrentFile()).andReturn(0);
    EasyMock.expect(mockFile.getAbsolutePath()).andReturn("/some/file/somewhere.mp3");
    EasyMock.expect(mockAudioPlayer.getCurrentPosition()).andReturn(0L);
    mockSaveManager.save(EasyMock.eq(new SavePoint("/some/file/somewhere.mp3", 0L)));
    EasyMock.expectLastCall();
    mockAudioPlayer.setShouldStop(true);
    EasyMock.expectLastCall();
    PowerMock.replay(mocks);
    saveAndCloseTask.run();
    PowerMock.verify(mocks);
  }
}
