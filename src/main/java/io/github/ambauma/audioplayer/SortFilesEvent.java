package io.github.ambauma.audioplayer;

public class SortFilesEvent {

  private String audioFolder;
  private int duration;

  public SortFilesEvent() {
  }

  public String getAudioFolder() {
    return audioFolder;
  }

  public void setAudioFolder(String audioFolder) {
    this.audioFolder = audioFolder;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
