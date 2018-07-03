package io.github.ambauma.audioplayer;

import io.github.ambauma.events.EventRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private EventRouter eventRouter;

    @Override
    public void run(String... args) throws Exception {
        FileLoadEvent fileLoadEvent = new FileLoadEvent();
        fileLoadEvent.setAudioFolder(args[0]);
        fileLoadEvent.setDuration(Integer.parseInt(args[1]));
        eventRouter.fire(fileLoadEvent);
    }
}
