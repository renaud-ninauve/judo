package fr.ninauve.renaud.judo.jsonsax;

public interface JsonSaxListener {

  void stringValue(final String value);

  void startObject();

  void endObject();
}
