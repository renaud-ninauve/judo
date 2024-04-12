package fr.ninauve.renaud.judo.jsonsax.parser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonRootParser implements JsonTokenParser {

  @Override
  public JsonTokenParser parseToken(JsonToken token, JsonSaxListener listener) {
    return switch (token.type()) {
      case START_OBJECT -> new JsonObjectParser(this, null);
      case START_ARRAY -> new JsonArrayParser(this, null);
      case STRING_VALUE -> parseStringValue(token, listener);
      case NUMBER_VALUE -> parseNumberValue(token, listener);
      default -> throw new AssertionError("unexpected tokenType " + token.type());
    };
  }

  private JsonTokenParser parseStringValue(JsonToken token, JsonSaxListener listener) {
    listener.stringValue(token.strValue());
    return this;
  }

  private JsonTokenParser parseNumberValue(JsonToken token, JsonSaxListener listener) {
    listener.numberValue(token.numberValue());
    return this;
  }
}
