package io.github.ambauma.audioplayer;

import static io.github.ambauma.audioplayer.Constants.DATA_PATH;
import static io.github.ambauma.audioplayer.Constants.DATA_PATH_STRING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveManager {

  private static final Logger LOG = LogManager.getLogger(SaveManager.class);
  private String absoluteFilePath;
  private Long position;

  public SaveManager() {
    LOG.info("Data File Path: " + DATA_PATH_STRING);
    LOG.info("Data file exists: " + Files.exists(DATA_PATH));
  }

  private void load() {
    if(Files.exists(DATA_PATH)){
      try {
        String contents = new String(Files.readAllBytes(DATA_PATH));
        this.absoluteFilePath = contents.substring(0, contents.indexOf("|"));
        this.position = Long.parseLong(
                contents.substring(contents.indexOf("|") + 1, contents.length()));
        LOG.info(String.format("Read \"%s\" from \"%s\"", contents, DATA_PATH));
      } catch (IOException e) {
        throw new RuntimeException();
      }
    }
  }

  int findFileIndex(List<File> files, String filePathToFind) throws IOException {
    for (int i = 0; i < files.size(); i++) {
      File afile = files.get(i);
      if (afile.getAbsolutePath().equals(filePathToFind)) {
        LOG.info("Set file to play to: " + afile.getAbsoluteFile());
        return i;
      }
    }
    return 0;
  }

  private void save() {
    String content = absoluteFilePath + "|" + position;
    LOG.info(String.format("Writing \"%s\" to \"%s\"", content, DATA_PATH));
    try {
      Files.write(DATA_PATH, content.getBytes(),
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getAbsoluteFilePath() {
    if(this.absoluteFilePath == null) {
      load();
    }
    return absoluteFilePath;
  }

  public void setAbsoluteFilePath(String absoluteFilePath) {
    save();
    this.absoluteFilePath = absoluteFilePath;
  }

  public long getPosition() {
    if(position == null) {
      load();
    }
    return position;
  }

  public void setPosition(long position) {
    save();
    this.position = position;
  }
}
