package fr.ninauve.renaud.judo.jsonsax;

import static org.mockito.Mockito.inOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonSaxParserTest {
  JsonSaxParser parser;

  @Mock JsonSaxListener listener;
  InOrder inOrder;

  @BeforeEach
  void setUp() {
    parser = new JsonSaxParser();
    inOrder = inOrder(listener);
  }

  @Test
  void should_parse_empty_object() {
    parser.parse("{}", listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).endObject();
  }
}
