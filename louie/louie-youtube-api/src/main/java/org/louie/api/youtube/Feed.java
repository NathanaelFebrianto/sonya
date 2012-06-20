package org.louie.api.youtube;

import java.util.List;

import com.google.api.client.util.Key;

public class Feed<T extends Item> {

  @Key
  List<T> items;

  @Key
  int totalItems;
}