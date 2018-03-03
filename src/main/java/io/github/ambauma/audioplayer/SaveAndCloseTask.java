package io.github.ambauma.audioplayer;

import java.io.File;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveAndCloseTask extends TimerTask {

  private static final Logger LOG = LogManager.getLogger(SaveAndCloseTask.class);
  private final AudioPlayer audioPlayer;
  private final SaveManager saveManager;

  public SaveAndCloseTask(AudioPlayer audioPlayer, SaveManager saveManager) {
    this.audioPlayer = audioPlayer;
    this.saveManager = saveManager;
  }

  @Override
  public void run() {
    LOG.traceEntry();
    File file = audioPlayer.getFiles().get(audioPlayer.getCurrentFile());
    long currentPosition = audioPlayer.getCurrentPosition();
    saveManager.save(new SavePoint(file.getAbsolutePath(), currentPosition));
    audioPlayer.setShouldStop(true);
    LOG.info("Stop requested.");
    LOG.traceExit();
  }
}
