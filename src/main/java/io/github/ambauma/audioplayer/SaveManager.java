package io.github.ambauma.audioplayer;

import static io.github.ambauma.audioplayer.Constants.DATA_PATH;
import static io.github.ambauma.audioplayer.Constants.DATA_PATH_STRING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveManager {

  private static final Logger LOG = LogManager.getLogger(SaveManager.class);

  public SaveManager() {
    LOG.info("Data File Path: " + DATA_PATH_STRING);
    LOG.info("Data file exists: " + Files.exists(DATA_PATH));
  }

  public SavePoint load() {
    SavePoint savePoint = new SavePoint();
    if(Files.exists(DATA_PATH)){
      try {
        String contents = new String(Files.readAllBytes(DATA_PATH));
        savePoint.setAbsoluteFilePath(contents.substring(0, contents.indexOf("|")));
        savePoint.setPosition(Long.parseLong(
                contents.substring(contents.indexOf("|") + 1, contents.length())));
        LOG.info(String.format("Read \"%s\" from \"%s\"", contents, DATA_PATH));
      } catch (IOException e) {
        throw new RuntimeException();
      }
    }
    return savePoint;
  }

  public void save(SavePoint savePoint) {
    String content = savePoint.getAbsoluteFilePath() + "|" + savePoint.getPosition();
    LOG.info(String.format("Writing \"%s\" to \"%s\"", content, DATA_PATH));
    try {
      Files.write(DATA_PATH, content.getBytes(),
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
