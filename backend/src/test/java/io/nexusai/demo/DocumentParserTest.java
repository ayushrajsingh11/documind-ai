package io.nexusai.demo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DocumentParserTest {
 @Test void parsesText()throws Exception{assertEquals("hello",DocumentParser.parse("a.txt","hello".getBytes()));}
 @Test void rejectsUnknown(){assertThrows(IllegalArgumentException.class,()->DocumentParser.parse("a.exe",new byte[]{1}));}
 @Test void overlapsChunks(){var text="a".repeat(2500);var chunks=DocumentParser.split(text);assertEquals(3,chunks.size());assertEquals(1200,chunks.getFirst().length());assertEquals(500,chunks.getLast().length());}
 @Test void serializesVectors(){assertEquals("[1.0,2.0]",DocumentService.vector(new float[]{1,2}));}
}
