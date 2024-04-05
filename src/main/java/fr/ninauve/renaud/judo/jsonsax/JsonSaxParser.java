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
  private static final char DOUBLE_QUOTE = '\"';
  private static final char FIELD_NAME_VALUE_DELIMITER = ':';

  private boolean expectingFieldName;
  private boolean expectingFieldValue;
  private String currentField;

  public void parse(String json, JsonSaxListener listener) {
    final StreamTokenizer tokenizer = createTokenizer(json);
    int tokenType;
    while ((tokenType = nextToken(tokenizer)) != StreamTokenizer.TT_EOF) {
      if (tokenType == VALUES_DELIMITER
          || tokenType == DOUBLE_QUOTE
          || tokenType == FIELD_NAME_VALUE_DELIMITER) {
        continue;
      }
      switch (tokenType) {
        case START_OBJECT -> {
          listener.startObject();
          expectingFieldName = true;
        }
        case END_OBJECT -> {
          listener.endObject();
          expectingFieldName = false;
        }
        case START_ARRAY -> listener.startArray();
        case END_ARRAY -> listener.endArray();
        case StreamTokenizer.TT_WORD -> {
          if (expectingFieldName) {
            currentField = tokenizer.sval;
            expectingFieldName = false;
            expectingFieldValue = true;
          } else if (expectingFieldValue) {
            listener.stringField(currentField, tokenizer.sval);
            expectingFieldName = true;
            expectingFieldValue = false;
            currentField = null;
          } else {
            listener.stringValue(tokenizer.sval);
          }
        }
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
    tokenizer.ordinaryChar(DOUBLE_QUOTE);
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
