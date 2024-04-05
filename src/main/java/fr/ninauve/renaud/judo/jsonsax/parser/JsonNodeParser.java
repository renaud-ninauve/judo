package fr.ninauve.renaud.judo.jsonsax.parser;

import fr.ninauve.renaud.judo.jsonsax.JsonSaxListener;
import java.io.StreamTokenizer;

public interface JsonNodeParser {

  void parseNode(StreamTokenizer tokenizer, JsonSaxListener listener);
}
