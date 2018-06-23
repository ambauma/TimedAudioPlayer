package io.github.ambauma.audioplayer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventRouter {

    private static final Logger LOG = LogManager.getLogger(EventRouter.class);
    private Map<String, Set<EventHandler>> eventHandlersByEvent;

    public EventRouter() {
        eventHandlersByEvent = new HashMap<>();
    }

    public void registerHandler(Event event, EventHandler eventHandler) {
        Set<EventHandler> eventHandlersForEvent = eventHandlersByEvent.get(event.getClass().getName());
        if(eventHandlersForEvent == null) {
            eventHandlersForEvent = new HashSet<>();
            eventHandlersByEvent.put(event.getClass().getName(), eventHandlersForEvent);
        }
        eventHandlersForEvent.add(eventHandler);
        LOG.info(String.format("Registered Event %s and Handler %s", event.getClass().getName(), eventHandler));
    }

    public void fire(Event event) {
        Set<EventHandler> eventHandlers = eventHandlersByEvent.get(event.getClass().getName());
        for(EventHandler eventHandler : eventHandlers) {
            eventHandler.handle(event);
        }
    }

}
