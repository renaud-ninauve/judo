package fr.ninauve.renaud.judo.jsonsax.parser;

public interface JsonTokenParser {

  default JsonTokenParser startObject() {
    throw new IllegalArgumentException("unexpected startObject");
  }

  default JsonTokenParser endObject() {
    throw new IllegalArgumentException("unexpected endObject");
  }

  default JsonTokenParser startArray() {
    throw new IllegalArgumentException("unexpected startArray");
  }

  default JsonTokenParser endArray() {
    throw new IllegalArgumentException("unexpected endArray");
  }

  default JsonTokenParser stringValue(String value) {
    throw new IllegalArgumentException("unexpected stringValue");
  }

  default JsonTokenParser numberValue(double value) {
    throw new IllegalArgumentException("unexpected numberValue");
  }
}
