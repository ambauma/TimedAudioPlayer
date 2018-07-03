package io.github.ambauma.audioplayer;

import io.github.ambauma.events.Event;

public class FileLoadEvent implements Event {
    private String audioFolder;
    private int duration;

    public FileLoadEvent() { }

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
