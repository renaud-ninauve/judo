package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonArrayParser.arrayParser;
import static fr.ninauve.renaud.judo.jsonsax.parser.JsonObjectParser.objectParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonRootParser implements JsonTokenParser {
  private final JsonSaxListener listener;

  public static JsonTokenParser rootParser(JsonSaxListener listener) {
    return new JsonRootParser(listener);
  }

  private JsonRootParser(JsonSaxListener listener) {
    this.listener = listener;
  }

  @Override
  public JsonTokenParser startObject() {
    return objectParser(listener, this, null);
  }

  @Override
  public JsonTokenParser startArray() {
    return arrayParser(listener, this, null);
  }

  @Override
  public JsonTokenParser stringValue(String value) {
    listener.stringValue(value);
    return this;
  }

  @Override
  public JsonTokenParser numberValue(double value) {
    listener.numberValue(value);
    return this;
  }
}
