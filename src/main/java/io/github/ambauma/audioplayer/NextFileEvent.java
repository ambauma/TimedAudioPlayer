package io.github.ambauma.audioplayer;

import java.io.File;

public class NextFileEvent {
  private File currentFile;

  public File getCurrentFile() {
    return currentFile;
  }

  public void setCurrentFile(File currentFile) {
    this.currentFile = currentFile;
  }
}
