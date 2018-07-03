package io.github.ambauma.audioplayer;

import io.github.ambauma.events.EventRouter;

public final class Main {

  /**
   * Main entrypoint of the application.
   * @param args  The args to process.  Expects a folder path and a duration in milliseconds
   * @throws Exception  possible exception
   */
  public static void main(String[] args) throws Exception {
    EventRouter eventRouter = new EventRouter();

    final String folderName = args[0];

    final int duration = Integer.parseInt(args[1]);
    new AudioPlayer(folderName, duration);
  }
}
