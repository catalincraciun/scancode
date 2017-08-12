package com.ichack.scancode.model;

public interface StorageGuard {

  boolean containsData(long code);
  void add(long code, String data);
  String getData(long code);
}
