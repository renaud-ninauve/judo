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

    JsonTokenParser currentParser = rootParser();
    int tokenValue;
    while ((tokenValue = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      final JsonTokenParser newParser =
          switch (tokenValue) {
            case START_OBJECT -> startObject(currentParser, listener);
            case END_OBJECT -> endObject(currentParser, listener);
            case START_ARRAY -> startArray(currentParser, listener);
            case END_ARRAY -> endArray(currentParser, listener);
            case StreamTokenizer.TT_NUMBER -> numberValue(currentParser, listener, tokenizer.nval);
            case StreamTokenizer.TT_WORD, DOUBLE_QUOTE -> stringValue(
                currentParser, listener, tokenizer.sval);
            default -> throw new IllegalArgumentException("unexpected token " + tokenValue);
          };
      if (newParser != currentParser) {
        newParser.firstToken(listener);
      }
      currentParser = newParser;
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

  private JsonTokenParser startObject(JsonTokenParser currentParser, JsonSaxListener listener) {
    return currentParser.startObject(listener);
  }

  private JsonTokenParser endObject(JsonTokenParser currentParser, JsonSaxListener listener) {
    return currentParser.endObject(listener);
  }

  private JsonTokenParser startArray(JsonTokenParser currentParser, JsonSaxListener listener) {
    return currentParser.startArray(listener);
  }

  private JsonTokenParser endArray(JsonTokenParser currentParser, JsonSaxListener listener) {
    return currentParser.endArray(listener);
  }

  private JsonTokenParser stringValue(
      JsonTokenParser currentParser, JsonSaxListener listener, String value) {
    return currentParser.stringValue(listener, value);
  }

  private JsonTokenParser numberValue(
      JsonTokenParser currentParser, JsonSaxListener listener, double value) {
    return currentParser.numberValue(listener, value);
  }
}
