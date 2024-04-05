package fr.ninauve.renaud.judo.jsonsax;

public interface JsonSaxListener {

  void stringValue(final String value);

  void numberValue(final double numberValue);

  void startObject();

  void stringField(final String fieldName, final String value);

  void endObject();

  void startArray();

  void endArray();
}
