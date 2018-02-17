package io.github.ambauma.audioplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        final String fileName = args[0];
        final int duration = Integer.parseInt(args[1]);


        final AudioPlayer audioPlayer = new AudioPlayer(fileName, duration);
    }
}
