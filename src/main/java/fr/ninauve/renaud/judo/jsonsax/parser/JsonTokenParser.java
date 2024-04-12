package fr.ninauve.renaud.judo.jsonsax.parser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public interface JsonTokenParser {

  enum JsonTokenType {
    STRING_VALUE,
    NUMBER_VALUE,
    START_OBJECT,
    END_OBJECT,
    START_ARRAY,
    END_ARRAY,
    COMMA,
    COLON
  }

  record JsonToken(JsonTokenType type, String strValue, Double numberValue) {}

  JsonTokenParser parseToken(JsonToken token, JsonSaxListener listener);
}
