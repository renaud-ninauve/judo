package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonArrayParser.startArrayParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonObjectParser implements JsonTokenParser {
  private final JsonSaxListener listener;
  private final JsonTokenParser parentParser;
  private final String parentField;
  private String currentField;

  public static JsonTokenParser startObjectParser(
      JsonSaxListener listener, JsonTokenParser parentParser, String currentField) {
    final JsonObjectParser parser = new JsonObjectParser(listener, parentParser, currentField);
    parser.start();
    return parser;
  }

  private JsonObjectParser(
      JsonSaxListener listener, JsonTokenParser parentParser, String parentField) {
    this.listener = listener;
    this.parentParser = parentParser;
    this.parentField = parentField;
  }

  private void start() {
    if (parentField == null) {
      listener.startObject();
    } else {
      listener.startObjectField(parentField);
    }
  }

  @Override
  public JsonTokenParser stringValue(String value) {
    if (currentField == null) {
      currentField = value;
    } else {
      listener.stringField(currentField, value);
      currentField = null;
    }
    return this;
  }

  @Override
  public JsonTokenParser numberValue(double value) {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got number " + value);
    } else {
      listener.numberField(currentField, value);
      currentField = null;
    }
    return this;
  }

  @Override
  public JsonTokenParser startObject() {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got start object");
    } else {
      final JsonTokenParser nextParser = startObjectParser(listener, this, currentField);
      currentField = null;
      return nextParser;
    }
  }

  @Override
  public JsonTokenParser startArray() {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got start array");
    } else {
      final JsonTokenParser nextParser = startArrayParser(listener, this, currentField);
      currentField = null;
      return nextParser;
    }
  }

  @Override
  public JsonTokenParser endObject() {
    listener.endObject();
    return parentParser;
  }
}
