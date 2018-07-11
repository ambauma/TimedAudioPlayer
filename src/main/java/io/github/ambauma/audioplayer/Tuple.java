package io.github.ambauma.audioplayer;

public class Tuple<S,T> {
  public final S left;
  public final T right;

  public Tuple(S left, T right) {
    this.left = left;
    this.right = right;
  }
}
