package fr.ninauve.renaud.judo.jsonsax;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParseObjectTest {
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
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_object_with_strings() {
    parser.parse(
        """
        {
          "name": "toto",
          "nickname": "tata"
        }
        """,
        listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).stringField("name", "toto");
    inOrder.verify(listener).stringField("nickname", "tata");
    inOrder.verify(listener).endObject();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_object_with_special_chars() {
    parser.parse(
        """
        {
          "name:[]{},'1.2": "toto:[]{},'1.2"
        }
        """, listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).stringField("name:[]{},'1.2", "toto:[]{},'1.2");
    inOrder.verify(listener).endObject();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_object_with_escaped_chars() {
    parser.parse(
        """
        {
          "\\"name\\"": "\\"toto\\""
        }
        """, listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).stringField("\"name\"", "\"toto\"");
    inOrder.verify(listener).endObject();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_object_with_numbers() {
    parser.parse(
        """
        {
          "a": 1.2,
          "b": 3.4
        }
        """, listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).numberField("a", 1.2);
    inOrder.verify(listener).numberField("b", 3.4);
    inOrder.verify(listener).endObject();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_object_with_objects() {
    parser.parse(
        """
        {
          "a": {},
          "b": {}
        }
        """, listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).startObjectField("a");
    inOrder.verify(listener).endObject();
    inOrder.verify(listener).startObjectField("b");
    inOrder.verify(listener, times(2)).endObject();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void should_parse_object_with_arrays() {
    parser.parse(
        """
        {
          "a": [],
          "b": []
        }
        """, listener);

    inOrder.verify(listener).startObject();
    inOrder.verify(listener).startArrayField("a");
    inOrder.verify(listener).endArray();
    inOrder.verify(listener).startArrayField("b");
    inOrder.verify(listener).endArray();
    inOrder.verify(listener).endObject();
    verifyNoMoreInteractions(listener);
  }
}
