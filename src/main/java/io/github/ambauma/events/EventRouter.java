package io.github.ambauma.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EventRouter {

  private static final String REGISTER_MESSAGE = "Registered event %s with handler %s";
  private static final String FIRE_MESSAGE = "Fired event %s and with handler %s";
  private static final Logger LOG = LogManager.getLogger(EventRouter.class);
  private Map<String, Set<EventHandler>> eventHandlersByEvent;

  public EventRouter() {
    eventHandlersByEvent = new HashMap<>();
  }

  /**
   * This method stores relationships between events and event handlers.
   * @param event An event
   * @param eventHandler  The handler to fire if the event occurs.
   */
  public void registerHandler(Event event, EventHandler eventHandler) {
    Set<EventHandler> eventHandlersForEvent = eventHandlersByEvent.get(event.getClass().getName());
    if (eventHandlersForEvent == null) {
      eventHandlersForEvent = new HashSet<>();
      eventHandlersByEvent.put(event.getClass().getName(), eventHandlersForEvent);
    }
    eventHandlersForEvent.add(eventHandler);
    LOG.info(String.format(REGISTER_MESSAGE, event.getClass().getName(), eventHandler));
  }

  /**
   * This method searches for event handlers that have registered to handle the passed event.  The
   * event will be passed to every event handler that has registered for that event.
   *
   * @param event The event used to search for event handlers.
   */
  public void fire(Event event) {
    Set<EventHandler> eventHandlers = eventHandlersByEvent.get(event.getClass().getName());
    eventHandlers = eventHandlers == null ? new HashSet<>() : eventHandlers;
    for (EventHandler eventHandler : eventHandlers) {
      eventHandler.handle(event);
      LOG.info(String.format(FIRE_MESSAGE, event.getClass().getName(), eventHandler));
    }
  }

}
