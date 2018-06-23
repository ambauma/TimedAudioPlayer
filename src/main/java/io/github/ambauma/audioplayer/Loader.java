package io.github.ambauma.audioplayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Loader implements CommandLineRunner {

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
