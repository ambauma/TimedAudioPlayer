package io.github.ambauma.audioplayer;


import java.io.File;
import java.nio.file.Files;

import javazoom.jlgui.basicplayer.BasicPlayer;
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
@PrepareForTest({Files.class, AudioPlayer.class, File.class, BasicPlayer.class, SaveManager.class})
public class AudioPlayerTest {

  private File mockFile1;
  private File mockFile2;
  private File mockFile3;
  private BasicPlayer mockBasicPlayer;
  private SaveManager mockSaveManager;
  private Object[] mocks;

  @Before
  public void setup() throws Exception {
    mockFile1 = PowerMock.createMock(File.class);
    mockFile2 = PowerMock.createMock(File.class);
    mockFile3 = PowerMock.createMock(File.class);
    mockSaveManager = PowerMock.createMock(SaveManager.class);
    mockBasicPlayer = PowerMock.createMock(BasicPlayer.class);

    mocks = new Object[] {mockFile1, mockFile2, mockFile3, mockBasicPlayer, mockSaveManager, Files.class, AudioPlayer.class, File.class, BasicPlayer.class, SaveManager.class};
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testConstructorWithEmptyDirectory() throws Exception {
    PowerMock.expectNew(File.class, "/some/folder").andReturn(mockFile1);
    EasyMock.expect(mockFile1.listFiles()).andReturn(new File[]{});
    PowerMock.replay(mocks);
      try {
        new AudioPlayer("/some/folder", 6000);
      } catch (ArrayIndexOutOfBoundsException e) {
        PowerMock.verify(mocks);
        throw e;
      }
  }

  @Test
  public void testConstructor() throws Exception {
    PowerMock.expectNew(BasicPlayer.class).andReturn(mockBasicPlayer);
    PowerMock.expectNew(File.class, "/some/folder").andReturn(mockFile1);
    PowerMock.expectNew(SaveManager.class).andReturn(mockSaveManager);

    EasyMock.expect(mockFile1.listFiles()).andReturn(new File[]{mockFile2, mockFile3});
    EasyMock.expect(mockSaveManager.getAbsoluteFilePath()).andReturn("/some/folder/2_file.mp3");
    EasyMock.expect(mockSaveManager.getPosition()).andReturn(6000L);
    EasyMock.expect(mockFile2.compareTo(EasyMock.isA(File.class))).andReturn(-1).anyTimes();
    EasyMock.expect(mockFile3.compareTo(EasyMock.isA(File.class))).andReturn(1).anyTimes();
    EasyMock.expect(mockFile2.getAbsolutePath()).andReturn("/some/folder/2_file.mp3").times(2);
    //EasyMock.expect(mockFile3.getAbsolutePath()).andReturn("/some/folder/3_file.mp3");

    mockBasicPlayer.open(mockFile2);
    EasyMock.expectLastCall();
    mockBasicPlayer.addBasicPlayerListener(EasyMock.isA(AudioPlayer.class));
    EasyMock.expectLastCall();
    EasyMock.expect(mockBasicPlayer.seek(6000L)).andReturn(6000L);
    mockBasicPlayer.play();
    EasyMock.expectLastCall();

    PowerMock.replay(mocks);
    new AudioPlayer("/some/folder", 6000);
    PowerMock.verify(mocks);
  }
}
