package fr.ninauve.renaud.judo.jsonsax;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class JsonSaxParser {
  private static final char START_OBJECT = '{';
  private static final char END_OBJECT = '}';
  private static final char START_ARRAY = '[';
  private static final char END_ARRAY = ']';
  private static final char VALUES_DELIMITER = ',';

  public void parse(String json, JsonSaxListener listener) {
    final StreamTokenizer tokenizer = createTokenizer(json);
    int tokenType;
    while ((tokenType = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      if (tokenType == VALUES_DELIMITER) {
        continue;
      }
      switch (tokenType) {
        case START_OBJECT -> listener.startObject();
        case END_OBJECT -> listener.endObject();
        case START_ARRAY -> listener.startArray();
        case END_ARRAY -> listener.endArray();
        case StreamTokenizer.TT_WORD -> listener.stringValue(tokenizer.sval);
        case StreamTokenizer.TT_NUMBER -> listener.numberValue(tokenizer.nval);
        default -> throw new AssertionError("tokenType " + tokenType + " is not currently handled");
      }
    }
  }

  private StreamTokenizer createTokenizer(String json) {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(json));
    tokenizer.eolIsSignificant(false);
    tokenizer.ordinaryChar(START_OBJECT);
    tokenizer.ordinaryChar(END_OBJECT);
    tokenizer.ordinaryChar(START_ARRAY);
    tokenizer.ordinaryChar(END_ARRAY);
    return tokenizer;
  }

  private int nextToken(StreamTokenizer streamTokenizer) {
    try {
      return streamTokenizer.nextToken();
    } catch (IOException e) {
      throw new IllegalStateException("failed to read next token", e);
    }
  }
}
