package fr.ninauve.renaud.judo.jsonsax;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonRootParser.rootParser;

import fr.ninauve.renaud.judo.jsonsax.parser.JsonTokenParser;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import lombok.SneakyThrows;

public class JsonSaxParser {
  private static final int START_OBJECT = '{';
  private static final int END_OBJECT = '}';
  private static final int START_ARRAY = '[';
  private static final int END_ARRAY = ']';
  private static final int COMMA = ',';
  private static final int COLON = ':';
  private static final int DOUBLE_QUOTE = '"';

  @SneakyThrows
  public void parse(String json, JsonSaxListener listener) {
    final StreamTokenizer tokenizer = createTokenizer(json);

    JsonTokenParser currentParser = rootParser(listener);
    int tokenValue;
    while ((tokenValue = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      currentParser =
          switch (tokenValue) {
            case START_OBJECT -> currentParser.startObject();
            case END_OBJECT -> currentParser.endObject();
            case START_ARRAY -> currentParser.startArray();
            case END_ARRAY -> currentParser.endArray();
            case StreamTokenizer.TT_NUMBER -> currentParser.numberValue(tokenizer.nval);
            case StreamTokenizer.TT_WORD, DOUBLE_QUOTE -> currentParser.stringValue(tokenizer.sval);
            default -> throw new IllegalArgumentException("unexpected token " + tokenValue);
          };
    }
  }

  private StreamTokenizer createTokenizer(String json) {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(json));
    tokenizer.eolIsSignificant(false);
    tokenizer.ordinaryChar(START_OBJECT);
    tokenizer.ordinaryChar(END_OBJECT);
    tokenizer.ordinaryChar(START_ARRAY);
    tokenizer.ordinaryChar(END_ARRAY);
    tokenizer.whitespaceChars(COLON, COLON);
    tokenizer.whitespaceChars(COMMA, COMMA);
    return tokenizer;
  }

  private static int nextToken(StreamTokenizer streamTokenizer) {
    try {
      return streamTokenizer.nextToken();
    } catch (IOException e) {
      throw new IllegalStateException("failed to read next token", e);
    }
  }

}
