package io.github.ambauma.audioplayer;


import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import io.github.ambauma.events.EventHandler;
import io.github.ambauma.events.EventRouter;
import javazoom.jlgui.basicplayer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AudioPlayerEventHandler implements EventHandler, BasicPlayerListener {

  private static final Logger LOG = LoggerFactory.getLogger(AudioPlayerEventHandler.class);

  @Autowired
  private EventRouter eventRouter;

  private File file;
  private long currentPosition;
  private BasicPlayer basicPlayer = new BasicPlayer();
  private boolean shouldStop;

  @PostConstruct
  public void init() {
    getEventRouter().registerHandlers(this);
  }

  @Override
  public <T> void handle(T event) {
    if(ShutdownNowEvent.class.isInstance(event)) {
      SavePoint savePoint = new SavePoint();
      savePoint.setAbsoluteFilePath(file.getAbsolutePath());
      savePoint.setPosition(currentPosition);
      getEventRouter().fire(savePoint);
      shouldStop = true;
    } else if(ReadyToPlayEvent.class.isInstance(event)) {
      ReadyToPlayEvent readyToPlayEvent = (ReadyToPlayEvent) event;
      file = readyToPlayEvent.getCurrentFile();
      currentPosition = readyToPlayEvent.getPosition();
      try {
        basicPlayer.open(file);
        basicPlayer.addBasicPlayerListener(this);
        basicPlayer.seek(currentPosition);
        basicPlayer.play();
      } catch (BasicPlayerException bpe) {
        throw new RuntimeException(bpe);
      }
    }
  }

  @Override
  public void opened(Object stream, Map properties) {
    LOG.info("opened: " + stream + " map: " + properties.entrySet());
  }

  @Override
  public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
    currentPosition = (long) properties.get("mp3.position.byte");
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
  public void stateUpdated(BasicPlayerEvent event) {
    System.out.println("BasicPlayerEvent: " + event);
    if(BasicPlayerEvent.STOPPED == event.getCode()) {
      if(!shouldStop) {
        try {
          currentPosition = 0;
          basicPlayer.open(file);
          basicPlayer.play();
        } catch (BasicPlayerException e) {
          e.printStackTrace();
        }
      }

    } else if (BasicPlayerEvent.EOM == event.getCode()) {
      NextFileEvent nextFileEvent = new NextFileEvent();
      getEventRouter().fire(nextFileEvent);
    }
  }

  @Override
  public void setController(BasicController controller) {
    //do nothing
  }

  public EventRouter getEventRouter() {
    return eventRouter;
  }

  public void setEventRouter(EventRouter eventRouter) {
    this.eventRouter = eventRouter;
  }
}
