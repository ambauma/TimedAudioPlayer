package io.github.ambauma.audioplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.TimerTask;

import static io.github.ambauma.audioplayer.Constants.DATA_PATH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveAndCloseTask extends TimerTask {

  private static final Logger LOG = LogManager.getLogger(SaveAndCloseTask.class);
  private final AudioPlayer audioPlayer;

  public SaveAndCloseTask(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }

  @Override
  public void run() {
    LOG.traceEntry();
    File file = audioPlayer.getFiles().get(audioPlayer.getCurrentFile());
    long currentPosition = audioPlayer.getCurrentPosition();
    String content = file.getAbsolutePath() + "|" + currentPosition;
    LOG.info(String.format("Writing \"%s\" to \"\"", content, DATA_PATH));
    try {
      Files.write(DATA_PATH, content.getBytes(),
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    audioPlayer.setShouldStop(true);
    LOG.info("Stop requested.");
    LOG.traceExit();
  }
}
