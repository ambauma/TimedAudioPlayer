package io.github.ambauma.audioplayer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.ambauma.events.EventHandler;
import io.github.ambauma.events.EventRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SortFilesEventHandler implements EventHandler {

  private static final Logger LOG = LogManager.getLogger(SortFilesEventHandler.class);

  @Autowired
  private EventRouter eventRouter;

  @PostConstruct
  public void init() {
    getEventRouter().registerHandlers(this);
  }


  @Override
  public <T> void handle(T event) {
    if(SortFilesEvent.class.isInstance(event)) {
      SortFilesEvent sortFilesEvent = (SortFilesEvent) event;
      LOG.info(String.format("%s handling event %s", this, sortFilesEvent));

      //Sort files
      List<File> files = Arrays.asList(new File(sortFilesEvent.getAudioFolder()).listFiles());
      Collections.sort(files);
      LOG.info("eventRouter:  " + getEventRouter());
      LOG.info("SortFileEventDuration: " + sortFilesEvent.getDuration());
      getEventRouter().fire(new FilesSortedEvent(files, sortFilesEvent.getDuration()));
    }
  }

  public EventRouter getEventRouter() {
    return eventRouter;
  }

  public void setEventRouter(EventRouter eventRouter) {
    this.eventRouter = eventRouter;
  }

}