package org.louie.api.youtube;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public class Item {

  @Key
  String title;

  @Key
  DateTime updated;
  
}
