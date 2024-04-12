package fr.ninauve.renaud.judo.jsonsax.parser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonArrayParser implements JsonTokenParser {
  private final JsonTokenParser parentParser;
  private boolean firstToken = true;
  private final String currentField;

  public JsonArrayParser(JsonTokenParser parentParser, String currentField) {
    this.parentParser = parentParser;
    this.currentField = currentField;
  }

  @Override
  public JsonTokenParser parseToken(JsonToken token, JsonSaxListener listener) {
    if (firstToken) {
      firstToken(listener);
    }

    return switch (token.type()) {
      case START_OBJECT -> startObject();
      case START_ARRAY -> startArray();
      case STRING_VALUE -> stringValue(token, listener);
      case NUMBER_VALUE -> numberValue(token, listener);
      case COMMA -> comma();
      case END_ARRAY -> endArray(listener);
      default -> throw new AssertionError(
          "unexpected tokenType " + token.type() + " while parsing array");
    };
  }

  private void firstToken(JsonSaxListener listener) {
    firstToken = false;
    if (currentField == null) {
      listener.startArray();
    } else {
      listener.startArrayField(currentField);
    }
  }

  private JsonTokenParser stringValue(JsonToken token, JsonSaxListener listener) {
    listener.stringValue(token.strValue());
    return this;
  }

  private JsonTokenParser numberValue(JsonToken token, JsonSaxListener listener) {
    listener.numberValue(token.numberValue());
    return this;
  }

  private JsonTokenParser startObject() {
    return new JsonObjectParser(this, null);
  }

  private JsonTokenParser startArray() {
    return new JsonArrayParser(this, null);
  }

  private JsonTokenParser comma() {
    return this;
  }

  private JsonTokenParser endArray(JsonSaxListener listener) {
    listener.endArray();
    return parentParser;
  }
}
