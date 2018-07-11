package io.github.ambauma.audioplayer;

import io.github.ambauma.events.EventRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private EventRouter eventRouter;

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Initializing...");
        SortFilesEvent sortFilesEvent = new SortFilesEvent();
        sortFilesEvent.setAudioFolder(args[0]);
        sortFilesEvent.setDuration(Integer.parseInt(args[1]));
        getEventRouter().fire(sortFilesEvent);
        LOG.info("...complete.");
    }

    public EventRouter getEventRouter() {
        return eventRouter;
    }

    public void setEventRouter(EventRouter eventRouter) {
        this.eventRouter = eventRouter;
    }
}
