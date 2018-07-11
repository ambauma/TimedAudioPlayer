package io.github.ambauma.audioplayer;

import java.io.File;

public class ReadyToPlayEvent {
  private File currentFile;
  private Long duration;

  public ReadyToPlayEvent() {

  }

  public ReadyToPlayEvent(File currentFile, Long duration) {
    this.currentFile = currentFile;
    this.duration = duration;
  }

  public File getCurrentFile() {
    return currentFile;
  }

  public void setCurrentFile(File currentFile) {
    this.currentFile = currentFile;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }
}
