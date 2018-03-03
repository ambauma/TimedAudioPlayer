package io.github.ambauma.audioplayer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SavePoint {
  private String absoluteFilePath;
  private long position;

  public SavePoint() { }

  public SavePoint(String absoluteFilePath, Long position) {
    super();
    this.absoluteFilePath = absoluteFilePath;
    this.position = position;
  }

  public String getAbsoluteFilePath() {
    return absoluteFilePath;
  }

  public void setAbsoluteFilePath(String absoluteFilePath) {
    this.absoluteFilePath = absoluteFilePath;
  }

  public long getPosition() {
    return position;
  }

  public void setPosition(long position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
