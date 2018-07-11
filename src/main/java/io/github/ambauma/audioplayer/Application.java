package io.github.ambauma.audioplayer;

import io.github.ambauma.events.EventRouter;
import javazoom.jlgui.basicplayer.BasicPlayer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "io.github.ambauma")
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public BasicPlayer basicPlayer() {
    return new BasicPlayer();
  }

  @Bean EventRouter eventRouter() { return new EventRouter(); }
}