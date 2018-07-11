package io.github.ambauma.audioplayer;

import java.io.File;
import java.util.List;

public class FilesSortedEvent {
  private List<File> files;
  private int duration;

  public FilesSortedEvent() {
  }

  public FilesSortedEvent(List<File> files, int duration) {
    this.files = files;
    this.duration = duration;
  }

  public List<File> getFiles() {
    return files;
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
