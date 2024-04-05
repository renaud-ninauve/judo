package fr.ninauve.renaud.judo.jsonsax;

public interface JsonSaxListener {

  void stringValue(final String value);

  void numberValue(final double numberValue);

  void startObject();

  void endObject();

  void startArray();

  void endArray();
}
