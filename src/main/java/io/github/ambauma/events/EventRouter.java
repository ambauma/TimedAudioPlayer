package io.github.ambauma.events;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventRouter {

  private static final String REGISTER_MESSAGE = "Registered handler %s";
  private static final String FIRE_MESSAGE = "Handler %s has returned from fired event %s";
  private static final Logger LOG = LoggerFactory.getLogger(EventRouter.class);
  private Set<EventHandler> eventHandlers = new HashSet<>();
  private ExecutorService executorService = Executors.newCachedThreadPool();

  public EventRouter() {

  }

  /**
   * This method stores event handlers.
   *
   * @param handlers One or more event handlers to be notified when events occur.
   */
  public void registerHandlers(EventHandler... handlers) {
    for (EventHandler eventHandler : handlers) {
      eventHandlers.add(eventHandler);
      LOG.info(String.format(REGISTER_MESSAGE, eventHandler));
    }
  }

  /**
   * This method searches for event handlers that have registered to handle the passed event.  The
   * event will be passed to every event handler that has registered for that event.
   *
   * @param event The event used to search for event handlers.
   */
  public <T extends Object> void fire(T event) {
    eventHandlers.parallelStream().forEach(eventHandler -> {
      eventHandler.handle(event);
      LOG.info(String.format(FIRE_MESSAGE, eventHandler, event));
    });
  }

}
