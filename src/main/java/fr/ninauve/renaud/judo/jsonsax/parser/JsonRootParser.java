package fr.ninauve.renaud.judo.jsonsax.parser;

import static fr.ninauve.renaud.judo.jsonsax.parser.JsonArrayParser.arrayParser;
import static fr.ninauve.renaud.judo.jsonsax.parser.JsonObjectParser.objectParser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;

public class JsonRootParser implements JsonTokenParser {

  public static JsonTokenParser rootParser() {
    return new JsonRootParser();
  }

  private JsonRootParser() {}

  @Override
  public JsonTokenParser startObject(JsonSaxListener listener) {
    return objectParser(this, null);
  }

  @Override
  public JsonTokenParser startArray(JsonSaxListener listener) {
    return arrayParser(this, null);
  }

  @Override
  public JsonTokenParser stringValue(JsonSaxListener listener, String value) {
    listener.stringValue(value);
    return this;
  }

  @Override
  public JsonTokenParser numberValue(JsonSaxListener listener, double value) {
    listener.numberValue(value);
    return this;
  }
}
