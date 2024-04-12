package fr.ninauve.renaud.judo.jsonsax.parser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonObjectParser implements JsonTokenParser {
  private final JsonTokenParser parentParser;
  private final String parentField;
  private boolean firstToken = true;
  private String currentField;

  public JsonObjectParser(JsonTokenParser parentParser, String parentField) {
    this.parentParser = parentParser;
    this.parentField = parentField;
  }

  @Override
  public JsonTokenParser parseToken(JsonToken token, JsonSaxListener listener) {
    if (firstToken) {
      firstToken(listener);
    }

    return switch (token.type()) {
      case COMMA, COLON -> this;
      case STRING_VALUE -> stringValue(token, listener);
      case NUMBER_VALUE -> numberValue(token, listener);
      case START_OBJECT -> startObject();
      case START_ARRAY -> startArray();
      case END_OBJECT -> endObject(listener);
      default -> throw new AssertionError(
          "unexpected tokenType " + token.type() + " while parsing object");
    };
  }

  private void firstToken(JsonSaxListener listener) {
    firstToken = false;
    if (parentField == null) {
      listener.startObject();
    } else {
      listener.startObjectField(parentField);
    }
  }

  private JsonTokenParser stringValue(JsonToken token, JsonSaxListener listener) {
    if (currentField == null) {
      currentField = token.strValue();
    } else {
      listener.stringField(currentField, token.strValue());
      currentField = null;
    }
    return this;
  }

  private JsonTokenParser numberValue(JsonToken token, JsonSaxListener listener) {
    if (currentField == null) {
      throw new IllegalArgumentException(
          "expecting a field name but got number " + token.numberValue());
    } else {
      listener.numberField(currentField, token.numberValue());
      currentField = null;
    }
    return this;
  }

  private JsonTokenParser startObject() {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got start object");
    } else {
      final JsonObjectParser nextParser = new JsonObjectParser(this, currentField);
      currentField = null;
      return nextParser;
    }
  }

  private JsonTokenParser startArray() {
    if (currentField == null) {
      throw new IllegalArgumentException("expecting a field name but got start array");
    } else {
      final JsonTokenParser nextParser = new JsonArrayParser(this, currentField);
      currentField = null;
      return nextParser;
    }
  }

  private JsonTokenParser endObject(JsonSaxListener listener) {
    listener.endObject();
    return parentParser;
  }
}
