"""Main module."""
import sqlite3
import argparse
import math
import os
import sys
import time
from pathlib import Path
import vlc

class Storage:
    """Manage application memory and storage."""

    def __init__(self, location=None):
        """Initialize the class."""
        if not location:
            location = os.path.join(
                os.path.expanduser('~'),
                ".local/share/timed-audio-player"
            )
        self.location = location
        Path(self.location).mkdir(parents=True, exist_ok=True)
        self.con = sqlite3.connect(os.path.join(self.location, "data.db"))
        self.current_file = None
        self.position = None
        cur = self.con.cursor()
        cur.execute("CREATE TABLE IF NOT EXISTS playdata(directory TEXT NOT NULL, current_file TEXT PRIMARY KEY, position REAL NOT NULL)")
        cur.execute("select * from playdata")
        cur.close()
    def set_position(self, current_file: str, position):
        """Store the current position of the file."""
        directory = os.path.dirname(current_file)
        cur = self.con.cursor()
        try:
            cur.execute("INSERT INTO playdata (directory, current_file, position) VALUES (?, ?, ?);", [directory, current_file, position])
        except sqlite3.IntegrityError:
            cur.execute("UPDATE playdata SET position = ? WHERE current_file = ?", [position, current_file])
        cur.close()
    def get_position(self, current_file: str) -> float:
        """Get the current position of a playback file."""
        directory = os.path.dirname(current_file)
        cur = self.con.cursor()
        cur.execute("SELECT position from playdata where current_file = ?;", [current_file])
        try:
            value = cur.fetchone()[0]
            return value
        except TypeError:
            return 0.0
    def playback_complete(self, directory):
        """Clear out storage because all files have been played."""
        print(f"DELETING ALL FOR {directory}")
        with self.con.cursor() as cur:
            cur.execute("DELETE playdata WHERE directory = ?;", [self.directory])
    def __del__(self):
        """Close the connection."""
        self.con.commit()
        self.con.close()

storage = Storage()

def main_cli():
    """The cli entrypoint."""
    parser = argparse.ArgumentParser(
        prog="timed-audio-player",
        description="An audio player that will play audio files in a directory for a specified amount of time.",
    )
    parser.add_argument("directory", help="The directory with audio files.")
    parser.add_argument("time", type=int, help="The amount of time to play the files for.")
    args = parser.parse_args()
    play(args.directory, args.time)


class Timechecker:
    """Determine if we should keep playing."""

    def __init__(self, duration: int):
        """Initialize the class."""
        self.current_time = time.time()
        self.duration = duration

    def check(self) -> bool:
        """Check if we should keep running."""
        return self.current_time + self.duration > time.time()

def play(directory: str, duration: int):
    """Play the directory."""
    files = list(os.listdir(directory))
    files.sort()
    inst = vlc.Instance()
    time_checker = Timechecker(duration)
    for file_ in files:
        if time_checker.check():
            file_ = os.path.join(directory, file_)
            play_file(file_, time_checker, inst)
        else:
            break
    if time_checker.check():
        storage.playback_complete()


def play_file(file_: str, time_checker: Timechecker, inst):
    """Play a file."""
    mp = inst.media_player_new()
    media = inst.media_new(file_)
    mp.set_media(media)
    position = storage.get_position(file_)
    mp.play()
    mp.set_position(position)
    while time_checker.check():
        position = mp.get_position()
        storage.set_position(file_, position)
        try:
            time.sleep(0.1)
        except KeyboardInterrupt as ki:
            mp.stop()
            raise ki
        print(f"Playing {os.path.basename(file_)}: {position*100:.0f}%\r", end="")
        sys.stdout.flush()
        if math.isclose(position, 0.999, rel_tol=0.001):
            mp.stop()
            break
    else:
        print("")
        print("Stopping due to time limit")
        mp.stop()
