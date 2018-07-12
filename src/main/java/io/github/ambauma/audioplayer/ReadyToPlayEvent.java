package io.github.ambauma.audioplayer;

import java.io.File;

public class ReadyToPlayEvent {
  private File currentFile;
  private Long position;

  public ReadyToPlayEvent() {

  }

  public ReadyToPlayEvent(File currentFile, Long position) {
    this.currentFile = currentFile;
    this.position = position;
  }

  public File getCurrentFile() {
    return currentFile;
  }

  public void setCurrentFile(File currentFile) {
    this.currentFile = currentFile;
  }

  public Long getPosition() {
    return position;
  }

  public void setPosition(Long position) {
    this.position = position;
  }
}
