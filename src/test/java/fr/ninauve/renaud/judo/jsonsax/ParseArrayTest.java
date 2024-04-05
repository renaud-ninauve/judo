package fr.ninauve.renaud.judo.jsonsax;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParseArrayTest {
  JsonSaxParser parser;

  @Mock JsonSaxListener listener;
  InOrder inOrder;

  @BeforeEach
  void setUp() {
    parser = new JsonSaxParser();
    inOrder = inOrder(listener);
  }

  @Test
  void should_parse_empty_array() {
    parser.parse("[]", listener);

    inOrder.verify(listener).startArray();
    inOrder.verify(listener).endArray();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_number_array() {
    parser.parse("[1, 2.3]", listener);

    inOrder.verify(listener).startArray();
    inOrder.verify(listener).numberValue(1);
    inOrder.verify(listener).numberValue(2.3);
    inOrder.verify(listener).endArray();
    verifyNoMoreInteractions(listener);
  }
}
