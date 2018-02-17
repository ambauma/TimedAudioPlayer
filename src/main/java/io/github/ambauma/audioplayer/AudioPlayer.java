package io.github.ambauma.audioplayer;

import javazoom.jlgui.basicplayer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class AudioPlayer implements BasicPlayerListener {

    private static final Logger LOG = LogManager.getLogger(AudioPlayer.class);
    private static final String DATA_FILE_PATH = System.getProperty("user.home") + File.separator + ".audioPlayer";
    private BasicPlayer basicPlayer;
    private File file;

    private int currentFile = 0;
    private long currentPosition = 0;
    private boolean shouldStop = false;
    List<File> files = new ArrayList<>();

    public AudioPlayer(String folder, int duration) throws BasicPlayerException, IOException {
        long startPosition = 0;
        files = Arrays.asList(new File(folder).listFiles());
        Collections.sort(files);
        File file = files.get(currentFile);

        final Path data = FileSystems.getDefault().getPath(DATA_FILE_PATH);
        LOG.info("Data File Path: " + DATA_FILE_PATH);
        LOG.info("Data file exists: " + Files.exists(data));
        if(Files.exists(data)) {
            String contents = new String(Files.readAllBytes(data));
            LOG.info("Data file contained: " + contents);
            String absoluteFilePath = findFilePath(contents);
            for(int i = 0; i < files.size(); i++) {
                File aFile = files.get(i);
                if(aFile.getAbsolutePath().equals(absoluteFilePath)) {
                    file = aFile;
                    currentFile = i;
                    LOG.info("Set file to play to: " + aFile.getAbsoluteFile());
                }
            }
            startPosition = Long.parseLong(contents.substring(contents.indexOf("|")+1, contents.length()));
            LOG.info("Set start position to: " + startPosition);
        }

        new Timer().schedule(new SaveAndCloseTask(this, data), duration);

        basicPlayer = new BasicPlayer();
        this.file = file;
        basicPlayer.open(file);
        basicPlayer.addBasicPlayerListener(this);
        basicPlayer.seek(startPosition);
        basicPlayer.play();
    }

    String findFilePath(String contents) {
        return contents.substring(0, contents.indexOf("|"));
    }

    public void saveState() {
        //move this.
    }

    @Override
    public void opened(Object o, Map map) {
        System.out.println("opened: " + o + " map: " + map.entrySet());
        //duration = new Long((int) map.get("audio.length.bytes"));
        //if(endPosition > duration) {
         //   endPosition = duration;
        //}
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {
        //System.out.println("Progress: " + i + " map: " + map.entrySet());
        currentPosition = (long) map.get("mp3.position.byte");
        if(shouldStop) {
            System.out.println("stopping");
            try {
                basicPlayer.stop();
                System.exit(0);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
        System.out.println("BasicPlayerEvent: " + basicPlayerEvent);
        if(BasicPlayerEvent.STOPPED == basicPlayerEvent.getCode()) {
            if(!shouldStop) {
                try {
                    currentPosition = 0;
                    basicPlayer.open(file);
                    basicPlayer.play();
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }

        } else if (BasicPlayerEvent.EOM == basicPlayerEvent.getCode()) {
            if(currentFile < files.size()) {
                currentFile++;
                file = files.get(currentFile);
                LOG.info("Changed file to: " + file.getAbsolutePath());
            } else {
                currentFile = 0;
            }
        }
    }

    @Override
    public void setController(BasicController basicController) {
        //Do Nothing
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public File getFile() {
        return file;
    }
}
