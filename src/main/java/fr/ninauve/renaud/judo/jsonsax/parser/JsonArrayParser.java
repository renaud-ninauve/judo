package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonObjectParser.startObjectParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonArrayParser implements JsonTokenParser {
  private final JsonSaxListener listener;
  private final JsonTokenParser parentParser;
  private final String currentField;

  public static JsonTokenParser startArrayParser(
      JsonSaxListener listener, JsonTokenParser parentParser, String currentField) {
    final JsonArrayParser parser = new JsonArrayParser(listener, parentParser, currentField);
    parser.start();
    return parser;
  }

  private JsonArrayParser(
      JsonSaxListener listener, JsonTokenParser parentParser, String currentField) {
    this.listener = listener;
    this.parentParser = parentParser;
    this.currentField = currentField;
  }

  private void start() {
    if (currentField == null) {
      listener.startArray();
    } else {
      listener.startArrayField(currentField);
    }
  }

  @Override
  public JsonTokenParser stringValue(String value) {
    listener.stringValue(value);
    return this;
  }

  @Override
  public JsonTokenParser numberValue(double value) {
    listener.numberValue(value);
    return this;
  }

  @Override
  public JsonTokenParser startObject() {
    return startObjectParser(listener, this, null);
  }

  @Override
  public JsonTokenParser startArray() {
    return startArrayParser(listener, this, null);
  }

  @Override
  public JsonTokenParser endArray() {
    listener.endArray();
    return parentParser;
  }
}
