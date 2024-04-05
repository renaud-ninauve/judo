package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.DOUBLE_QUOTE;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.END_ARRAY;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.START_ARRAY;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.START_OBJECT;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.VALUES_DELIMITER;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.nextToken;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;
import java.io.StreamTokenizer;

public class JsonArrayParser implements JsonNodeParser {
  private final String currentField;

  public JsonArrayParser(String currentField) {
    this.currentField = currentField;
  }

  public void parseNode(StreamTokenizer tokenizer, JsonSaxListener listener) {
    if (currentField == null) {
      listener.startArray();
    } else {
      listener.startArrayField(currentField);
    }

    int tokenType;
    while ((tokenType = nextToken(tokenizer)) != END_ARRAY) {
      if (tokenType == VALUES_DELIMITER || tokenType == DOUBLE_QUOTE) {
        continue;
      }

      switch (tokenType) {
        case START_OBJECT -> new JsonObjectParser(null).parseNode(tokenizer, listener);
        case START_ARRAY -> new JsonArrayParser(null).parseNode(tokenizer, listener);
        case StreamTokenizer.TT_WORD -> listener.stringValue(tokenizer.sval);
        case StreamTokenizer.TT_NUMBER -> listener.numberValue(tokenizer.nval);
        default -> throw new AssertionError(
            "unexpected tokenType " + tokenType + " while parsing array");
      }
    }
    listener.endArray();
  }
}
