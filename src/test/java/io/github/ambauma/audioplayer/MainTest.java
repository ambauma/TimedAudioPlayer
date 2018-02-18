package io.github.ambauma.audioplayer;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Main.class})
public class MainTest {

    private AudioPlayer mockAudioPlayer;
    private Object[] mocks;

    @Before
    public void before() {
        mockAudioPlayer = PowerMock.createMock(AudioPlayer.class);
        mocks = new Object[] { mockAudioPlayer, AudioPlayer.class };
    }

    @Test
    public void testFindFilePath() throws Exception {
        PowerMock.expectNew(AudioPlayer.class, "/some/folder", 6000).andReturn(mockAudioPlayer);
        PowerMock.replay(mocks);
        Main.main(new String[] {"/some/folder", "6000"});
        PowerMock.verify(mocks);
    }
}
