package fr.ninauve.renaud.judo.jsonsax;

import fr.ninauve.renaud.judo.jsonsax.parser.JsonRootParser;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class JsonSaxParser {
  public static final char START_OBJECT = '{';
  public static final char END_OBJECT = '}';
  public static final char START_ARRAY = '[';
  public static final char END_ARRAY = ']';
  public static final char VALUES_DELIMITER = ',';
  public static final char DOUBLE_QUOTE = '\"';
  public static final char FIELD_NAME_VALUE_DELIMITER = ':';

  public void parse(String json, JsonSaxListener listener) {
    final StreamTokenizer tokenizer = createTokenizer(json);
    new JsonRootParser().parseNode(tokenizer, listener);
  }

  private StreamTokenizer createTokenizer(String json) {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(json));
    tokenizer.eolIsSignificant(false);
    tokenizer.ordinaryChar(START_OBJECT);
    tokenizer.ordinaryChar(END_OBJECT);
    tokenizer.ordinaryChar(START_ARRAY);
    tokenizer.ordinaryChar(END_ARRAY);
    tokenizer.ordinaryChar(DOUBLE_QUOTE);
    return tokenizer;
  }

  public static int nextToken(StreamTokenizer streamTokenizer) {
    try {
      return streamTokenizer.nextToken();
    } catch (IOException e) {
      throw new IllegalStateException("failed to read next token", e);
    }
  }
}
