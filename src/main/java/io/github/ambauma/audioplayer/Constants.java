package io.github.ambauma.audioplayer;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

final class Constants {
  static final String DATA_FILE_PATH = System.getProperty("user.home")
          + File.separator
          + ".audioPlayer";
  static final Path DATA_PATH = FileSystems.getDefault().getPath(DATA_FILE_PATH);
}
