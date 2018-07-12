package io.github.ambauma.audioplayer;

import javax.annotation.PostConstruct;
import java.util.Timer;

import io.github.ambauma.events.EventHandler;
import io.github.ambauma.events.EventRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShutdownLaterHandler implements EventHandler {

  @Autowired
  private EventRouter eventRouter;

  @PostConstruct
  public void init() {
    getEventRouter().registerHandlers(this);
  }

  @Override
  public <T> void handle(T event) {
    if(StartTimerEvent.class.isInstance(event)) {
      StartTimerEvent startTimerEvent = (StartTimerEvent) event;
      new Timer().schedule(new SaveAndCloseTask(), startTimerEvent.getDuration());
    }
  }

  public EventRouter getEventRouter() {
    return eventRouter;
  }

  public void setEventRouter(EventRouter eventRouter) {
    this.eventRouter = eventRouter;
  }
}
