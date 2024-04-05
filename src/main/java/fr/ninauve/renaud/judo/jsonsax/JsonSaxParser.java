package fr.ninauve.renaud.judo.jsonsax;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class JsonSaxParser {
  private static final char START_OBJECT = '{';
  private static final char END_OBJECT = '}';

  public void parse(String json, JsonSaxListener listener) {
    final StreamTokenizer tokenizer = createTokenizer(json);
    int tokenType;
    while ((tokenType = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      if (tokenType == START_OBJECT) {
        listener.startObject();
      }
      if (tokenType == END_OBJECT) {
        listener.endObject();
      }
      if (tokenType == StreamTokenizer.TT_WORD) {
        listener.stringValue(tokenizer.sval);
      }
    }
  }

  private StreamTokenizer createTokenizer(String json) {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(json));
    tokenizer.eolIsSignificant(false);
    tokenizer.ordinaryChar('{');
    tokenizer.ordinaryChar('}');
    tokenizer.ordinaryChar('[');
    tokenizer.ordinaryChar(']');
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
