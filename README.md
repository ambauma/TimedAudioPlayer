# TimedAudioPlayer
An audio player that will play audio files in a directory for a specified amount of time.

Has been tested on Ubuntu (OpenJDK8) and Windows (Oracle JDK8)

## Dependencies
Windows:
 * Java JRE 8+
Ubuntu:
 * Java JRE 8+
 * ubuntu-restricted-extras
 * icedtea-8-plugin

## Usage
```java
java -jar audioplayer-0.1.0-SNAPSHOT-jar-with-dependencies.jar "/home/ambauma/Music/Audiobooks/AudiobookSeries" 60000
```