package com.rs.anergine.Util;

public class Holder<T> {
  private T value;

  public Holder(T i) {
    value = i;
  }

  public Holder() {

  }


  public void set(T i) {
    value = i;
  }

  public T get() {
    return value;
  }
}
