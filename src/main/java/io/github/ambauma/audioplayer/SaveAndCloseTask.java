package io.github.ambauma.audioplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveAndCloseTask extends TimerTask {

  private static final Logger LOG = LogManager.getLogger(SaveAndCloseTask.class);
  private final AudioPlayer audioPlayer;
  private Path data;

  public SaveAndCloseTask(AudioPlayer audioPlayer, Path data) {
    this.audioPlayer = audioPlayer;
    this.data = data;
  }

  @Override
  public void run() {
    LOG.traceEntry();
    File file = audioPlayer.getFile();
    long currentPosition = audioPlayer.getCurrentPosition();
    String content = file.getAbsolutePath() + "|" + currentPosition;
    LOG.info("Writing data: " + content);
    try {
      Files.write(data, content.getBytes(),
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
    audioPlayer.setShouldStop(true);
    LOG.info("Stop requested.");
    LOG.traceExit();
  }
}
