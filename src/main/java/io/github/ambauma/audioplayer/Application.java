package io.github.ambauma.audioplayer;

import io.github.ambauma.events.EventRouter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public void run(String... args) {

    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

    }

    @Bean
    public EventRouter eventRouter() {
        EventRouter eventRouter = new EventRouter();
        eventRouter.registerHandler(new FileLoadEvent(), new FileLoadEventHandler());
        return eventRouter;
    }

}