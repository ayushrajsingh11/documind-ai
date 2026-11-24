package io.nexusai.demo;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DemoServiceTest {
 private final DemoService service=new DemoService();
 @Test void sampleWorkflow(){assertEquals(List.of("refund policy"),service.execute("refund policy\ninvoice","refund"));}
 @Test void rejectsNull(){assertThrows(IllegalArgumentException.class,()->service.execute(null,"test"));}
}
