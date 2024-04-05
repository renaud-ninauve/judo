package fr.ninauve.renaud.judo.jsonsax;

public interface JsonSaxListener {

  void stringValue(final String value);

  void numberValue(final double value);

  void startObject();

  void stringField(final String fieldName, final String value);

  void numberField(final String fieldName, final double value);

  void endObject();

  void startArray();

  void endArray();
}
