package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonObjectParser.objectParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonArrayParser implements JsonTokenParser {
  private final JsonSaxListener listener;
  private final JsonTokenParser parentParser;
  private final String currentField;
  private boolean firstToken = true;

  public static JsonTokenParser arrayParser(
      JsonSaxListener listener, JsonTokenParser parentParser, String currentField) {
    return new JsonArrayParser(listener, parentParser, currentField);
  }

  private JsonArrayParser(
      JsonSaxListener listener, JsonTokenParser parentParser, String currentField) {
    this.listener = listener;
    this.parentParser = parentParser;
    this.currentField = currentField;
  }

  @Override
  public void firstToken() {
    if (!firstToken) {
      return;
    }
    firstToken = false;

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
    return objectParser(listener, this, null);
  }

  @Override
  public JsonTokenParser startArray() {
    return arrayParser(listener, this, null);
  }

  @Override
  public JsonTokenParser endArray() {
    listener.endArray();
    return parentParser;
  }
}
