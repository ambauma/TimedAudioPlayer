package io.github.ambauma.audioplayer;

import java.io.File;
import java.util.TimerTask;

import io.github.ambauma.events.EventRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveAndCloseTask extends TimerTask {

  private static final Logger LOG = LogManager.getLogger(SaveAndCloseTask.class);

  @Autowired
  private EventRouter eventRouter;

  public SaveAndCloseTask() { }

  @Override
  public void run() {
    LOG.info("Duration exceeded.");
    getEventRouter().fire(new ShutdownNowEvent());
  }

  public EventRouter getEventRouter() {
    return eventRouter;
  }

  public void setEventRouter(EventRouter eventRouter) {
    this.eventRouter = eventRouter;
  }
}
