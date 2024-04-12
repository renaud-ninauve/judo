package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonObjectParser.objectParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonArrayParser implements JsonTokenParser {
  private final JsonTokenParser parentParser;
  private final String currentField;
  private boolean firstToken = true;

  public static JsonTokenParser arrayParser(JsonTokenParser parentParser, String currentField) {
    return new JsonArrayParser(parentParser, currentField);
  }

  private JsonArrayParser(JsonTokenParser parentParser, String currentField) {
    this.parentParser = parentParser;
    this.currentField = currentField;
  }

  @Override
  public void firstToken(JsonSaxListener listener) {
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
  public JsonTokenParser stringValue(JsonSaxListener listener, String value) {
    listener.stringValue(value);
    return this;
  }

  @Override
  public JsonTokenParser numberValue(JsonSaxListener listener, double value) {
    listener.numberValue(value);
    return this;
  }

  @Override
  public JsonTokenParser startObject(JsonSaxListener listener) {
    return objectParser(this, null);
  }

  @Override
  public JsonTokenParser startArray(JsonSaxListener listener) {
    return arrayParser(this, null);
  }

  @Override
  public JsonTokenParser endArray(JsonSaxListener listener) {
    listener.endArray();
    return parentParser;
  }
}
