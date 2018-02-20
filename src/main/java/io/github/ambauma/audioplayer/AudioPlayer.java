package io.github.ambauma.audioplayer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AudioPlayer implements BasicPlayerListener {

  private static final Logger LOG = LogManager.getLogger(AudioPlayer.class);
  private final List<File> files;
  private final BasicPlayer basicPlayer = new BasicPlayer();
  private int currentFile = 0;
  private long currentPosition = 0;
  private boolean shouldStop = false;
  private SaveManager saveManager;



  /**
   * Starts the audio.
   * @param audioFolder  The folder to start playing in
   * @param duration  The length of time in milliseconds to play
   * @throws BasicPlayerException possible exception
   * @throws IOException  possible exception
   */
  public AudioPlayer(String audioFolder, int duration) throws BasicPlayerException, IOException {
    files = Arrays.asList(new File(audioFolder).listFiles());
    Collections.sort(files);

    saveManager = new SaveManager();
    this.currentPosition = saveManager.getPosition();
    this.currentFile = findFileIndex(files, saveManager.getAbsoluteFilePath());
    LOG.info("Set start position to: " + currentPosition);

    new Timer().schedule(new SaveAndCloseTask(this, saveManager), duration);

    basicPlayer.open(files.get(this.currentFile));
    basicPlayer.addBasicPlayerListener(this);
    basicPlayer.seek(currentPosition);
    basicPlayer.play();
  }

  int findFileIndex(List<File> files, String filePathToFind) throws IOException {
    for (int i = 0; i < files.size(); i++) {
      File afile = files.get(i);
      if (afile.getAbsolutePath().equals(filePathToFind)) {
        LOG.info("Set file to play to: " + afile.getAbsolutePath());
        return i;
      }
    }
    return 0;
  }

  @Override
  public void opened(Object o, Map map) {
    LOG.info(String.format("Opened %s %s", o, map.entrySet()));
  }

  @Override
  public void progress(int i, long l, byte[] bytes, Map map) {
    currentPosition = (long) map.get("mp3.position.byte");
    if (shouldStop) {
      LOG.info("Stopping");
      try {
        basicPlayer.stop();
        System.exit(0);
      } catch (BasicPlayerException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
    LOG.info("BasicPlayerEvent: " + basicPlayerEvent);
    if (BasicPlayerEvent.STOPPED == basicPlayerEvent.getCode()) {
      if (!shouldStop) {
        try {
          currentPosition = 0;
          basicPlayer.open(files.get(currentFile));
          basicPlayer.play();
        } catch (BasicPlayerException e) {
          throw new RuntimeException(e);
        }
      }

    } else if (BasicPlayerEvent.EOM == basicPlayerEvent.getCode()) {
      if (currentFile < files.size()) {
        currentFile++;
        LOG.info("Changed file to: " + files.get(currentFile).getAbsolutePath());
      } else {
        currentFile = 0;
      }
    }
  }

  @Override
  public void setController(BasicController basicController) {
  }

  public long getCurrentPosition() {
    return currentPosition;
  }

  public void setShouldStop(boolean shouldStop) {
    this.shouldStop = shouldStop;
  }

  public List<File> getFiles() {
    return files;
  }

  public int getCurrentFile() {
    return currentFile;
  }
}
