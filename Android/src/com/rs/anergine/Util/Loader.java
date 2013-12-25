package com.rs.anergine.Util;

public interface Loader<O, I> {
  public Holder<O> load(I input);
}
