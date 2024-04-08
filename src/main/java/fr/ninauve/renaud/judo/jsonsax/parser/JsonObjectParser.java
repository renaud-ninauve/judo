package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.DOUBLE_QUOTE;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.END_OBJECT;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.FIELD_NAME_VALUE_DELIMITER;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.START_ARRAY;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.START_OBJECT;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.VALUES_DELIMITER;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.nextToken;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;
import java.io.StreamTokenizer;

public class JsonObjectParser implements JsonNodeParser {
  private final String parentField;
  private String currentField;

  public JsonObjectParser(String parentField) {
    this.parentField = parentField;
  }

  public void parseNode(StreamTokenizer tokenizer, JsonSaxListener listener) {
    if (parentField == null) {
      listener.startObject();
    } else {
      listener.startObjectField(parentField);
    }

    int tokenType;
    while ((tokenType = nextToken(tokenizer)) != END_OBJECT) {
      if (tokenType == VALUES_DELIMITER
          || tokenType == FIELD_NAME_VALUE_DELIMITER) {
        continue;
      }

      switch (tokenType) {
        case StreamTokenizer.TT_WORD, DOUBLE_QUOTE -> {
          if (currentField == null) {
            currentField = tokenizer.sval;
          } else {
            listener.stringField(currentField, tokenizer.sval);
            currentField = null;
          }
        }
        case StreamTokenizer.TT_NUMBER -> {
          if (currentField == null) {
            throw new IllegalArgumentException(
                "expecting a field name but got number " + tokenizer.nval);
          } else {
            listener.numberField(currentField, tokenizer.nval);
            currentField = null;
          }
        }
        case START_OBJECT -> {
          if (currentField == null) {
            throw new IllegalArgumentException(
                "expecting a field name but got number " + tokenizer.nval);
          } else {
            new JsonObjectParser(currentField).parseNode(tokenizer, listener);
            currentField = null;
          }
        }
        case START_ARRAY -> {
          if (currentField == null) {
            throw new IllegalArgumentException("expecting a field name but got a start array");
          } else {
            new JsonArrayParser(currentField).parseNode(tokenizer, listener);
            currentField = null;
          }
        }
        default -> throw new AssertionError(
            "unexpected tokenType " + tokenType + " while parsing object");
      }
    }
    listener.endObject();
  }
}
