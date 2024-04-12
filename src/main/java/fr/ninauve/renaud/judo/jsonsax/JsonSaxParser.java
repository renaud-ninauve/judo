package fr.ninauve.renaud.judo.jsonsax;

import fr.ninauve.renaud.judo.jsonsax.parser.JsonRootParser;
import fr.ninauve.renaud.judo.jsonsax.parser.JsonTokenParser;
import fr.ninauve.renaud.judo.jsonsax.parser.JsonTokenParser.JsonToken;
import fr.ninauve.renaud.judo.jsonsax.parser.JsonTokenParser.JsonTokenType;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Map;
import lombok.SneakyThrows;

public class JsonSaxParser {
  private static final int START_OBJECT = '{';
  private static final int END_OBJECT = '}';
  private static final int START_ARRAY = '[';
  private static final int END_ARRAY = ']';
  private static final int COMMA = ',';
  private static final int COLON = ':';
  private static final int DOUBLE_QUOTE = '"';

  private static final Map<Integer, JsonTokenType> TOKEN_TYPE_BY_VALUE =
      Map.of(
          START_OBJECT,
          JsonTokenType.START_OBJECT,
          END_OBJECT,
          JsonTokenType.END_OBJECT,
          START_ARRAY,
          JsonTokenType.START_ARRAY,
          END_ARRAY,
          JsonTokenType.END_ARRAY,
          COMMA,
          JsonTokenType.COMMA,
          COLON,
          JsonTokenType.COLON,
          StreamTokenizer.TT_WORD,
          JsonTokenType.STRING_VALUE,
          DOUBLE_QUOTE,
          JsonTokenType.STRING_VALUE,
          StreamTokenizer.TT_NUMBER,
          JsonTokenType.NUMBER_VALUE);

  @SneakyThrows
  public void parse(String json, JsonSaxListener listener) {
    final StreamTokenizer tokenizer = createTokenizer(json);

    JsonTokenParser currentParser = new JsonRootParser();
    int tokenValue;
    while ((tokenValue = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      final JsonTokenType tokenType = TOKEN_TYPE_BY_VALUE.get(tokenValue);
      if (tokenType == null) {
        continue;
      }
      final JsonToken token = new JsonToken(tokenType, tokenizer.sval, tokenizer.nval);
      currentParser = currentParser.parseToken(token, listener);
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

  public static int nextToken(StreamTokenizer streamTokenizer) {
    try {
      return streamTokenizer.nextToken();
    } catch (IOException e) {
      throw new IllegalStateException("failed to read next token", e);
    }
  }
}
