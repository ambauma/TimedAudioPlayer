package io.github.ambauma.audioplayer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashSet;
import java.util.List;

import io.github.ambauma.events.EventHandler;
import io.github.ambauma.events.EventRouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SavePointHandler implements EventHandler {
  private static final Logger LOG = LoggerFactory.getLogger(SavePointHandler.class);

  @Autowired
  private EventRouter eventRouter;

  @Autowired
  private SaveManager saveManager;

  @PostConstruct
  public void init() {
    getEventRouter().registerHandlers(this);
  }

  @Override
  public <T> void handle(T event) {
    if(FilesSortedEvent.class.isInstance(event)) {
      subHandle((FilesSortedEvent) event);
    }
  }

  void subHandle(FilesSortedEvent filesSortedEvent) {
    LOG.info(String.format("%s handling event %s", this, filesSortedEvent));
    SavePoint savePoint = getSaveManager().load();
    int filePosition = 0;
    if(savePoint.getAbsoluteFilePath() != null) {
      filePosition = findFileIndex(filesSortedEvent.getFiles(), savePoint.getAbsoluteFilePath());
    }
    File currentFile = filesSortedEvent.getFiles().get(filePosition);
    LOG.info(String.format("For file %s set start position to %d.", currentFile, savePoint.getPosition()));

    ReadyToPlayEvent readyToPlayEvent = new ReadyToPlayEvent();
    readyToPlayEvent.setCurrentFile(currentFile);
    readyToPlayEvent.setPosition(savePoint.getPosition());
    getEventRouter().fire(readyToPlayEvent);
  }

  int findFileIndex(List<File> files, String filePathToFind) {
    for (int i = 0; i < files.size(); i++) {
      File afile = files.get(i);
      if (afile.getAbsolutePath().equals(filePathToFind)) {
        LOG.info("Set file to play to: " + afile.getAbsolutePath());
        return i;
      }
    }
    return 0;
  }

  public EventRouter getEventRouter() {
    return eventRouter;
  }

  public void setEventRouter(EventRouter eventRouter) {
    this.eventRouter = eventRouter;
  }

  public SaveManager getSaveManager() {
    return saveManager;
  }

  public void setSaveManager(SaveManager saveManager) {
    this.saveManager = saveManager;
  }

}
