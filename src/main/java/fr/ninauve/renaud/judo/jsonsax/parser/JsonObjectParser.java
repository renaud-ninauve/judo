package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonArrayParser.arrayParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonObjectParser implements JsonTokenParser {
  private final JsonTokenParser parentParser;
  private final String parentField;
  private String currentField;
  private boolean firstToken = true;

  public static JsonTokenParser objectParser(JsonTokenParser parentParser, String currentField) {
    return new JsonObjectParser(parentParser, currentField);
  }

  private JsonObjectParser(JsonTokenParser parentParser, String parentField) {
    this.parentParser = parentParser;
    this.parentField = parentField;
  }

  @Override
  public void firstToken(JsonSaxListener listener) {
    if (!firstToken) {
      return;
    }
    firstToken = false;

    if (parentField == null) {
      listener.startObject();
    } else {
      listener.startObjectField(parentField);
    }
  }

  @Override
  public JsonTokenParser stringValue(JsonSaxListener listener, String value) {
    if (currentField == null) {
      currentField = value;
    } else {
      listener.stringField(currentField, value);
      currentField = null;
    }
    return this;
  }

  @Override
  public JsonTokenParser numberValue(JsonSaxListener listener, double value) {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got number " + value);
    } else {
      listener.numberField(currentField, value);
      currentField = null;
    }
    return this;
  }

  @Override
  public JsonTokenParser startObject(JsonSaxListener listener) {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got start object");
    } else {
      final JsonTokenParser nextParser = objectParser(this, currentField);
      currentField = null;
      return nextParser;
    }
  }

  @Override
  public JsonTokenParser startArray(JsonSaxListener listener) {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got start array");
    } else {
      final JsonTokenParser nextParser = arrayParser(this, currentField);
      currentField = null;
      return nextParser;
    }
  }

  @Override
  public JsonTokenParser endObject(JsonSaxListener listener) {
    listener.endObject();
    return parentParser;
  }
}
