package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.START_ARRAY;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.START_OBJECT;
import static fr.ninauve.renaud.judo.jsonsax.JsonSaxParser.nextToken;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;
import java.io.StreamTokenizer;

public class JsonRootParser implements JsonNodeParser {

  public void parseNode(StreamTokenizer tokenizer, JsonSaxListener listener) {
    int tokenType;
    while ((tokenType = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      switch (tokenType) {
        case START_OBJECT -> new JsonObjectParser(null).parseNode(tokenizer, listener);
        case START_ARRAY -> new JsonArrayParser().parseNode(tokenizer, listener);
        case StreamTokenizer.TT_WORD -> listener.stringValue(tokenizer.sval);
        case StreamTokenizer.TT_NUMBER -> listener.numberValue(tokenizer.nval);
        default -> throw new AssertionError("tokenType " + tokenType + " is not currently handled");
      }
    }
  }
}
