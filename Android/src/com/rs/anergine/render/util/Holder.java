package com.rs.anergine.render.util;

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
