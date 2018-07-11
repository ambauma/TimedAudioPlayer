package io.github.ambauma.events;

public interface EventHandler {

  <T extends Object> void handle(T event);

}
