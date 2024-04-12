package fr.ninauve.renaud.judo.jsonsax.parser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public interface JsonTokenParser {

  default void firstToken(JsonSaxListener listener) {}

  default JsonTokenParser startObject(JsonSaxListener listener) {
    throw new IllegalArgumentException("unexpected startObject");
  }

  default JsonTokenParser endObject(JsonSaxListener listener) {
    throw new IllegalArgumentException("unexpected endObject");
  }

  default JsonTokenParser startArray(JsonSaxListener listener) {
    throw new IllegalArgumentException("unexpected startArray");
  }

  default JsonTokenParser endArray(JsonSaxListener listener) {
    throw new IllegalArgumentException("unexpected endArray");
  }

  default JsonTokenParser stringValue(JsonSaxListener listener, String value) {
    throw new IllegalArgumentException("unexpected stringValue");
  }

  default JsonTokenParser numberValue(JsonSaxListener listener, double value) {
    throw new IllegalArgumentException("unexpected numberValue");
  }
}
