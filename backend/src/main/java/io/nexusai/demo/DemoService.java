package io.nexusai.demo;
import java.util.*;
import org.springframework.stereotype.Service;
@Service public class DemoService {
 public List<String> execute(String input,String query){
  if(input==null||query==null||input.length()>50000||query.length()>2000)throw new IllegalArgumentException("Invalid input size");
  return Arrays.stream(input.split("\\R")).filter(s->s.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))).limit(10).toList();
 }
}
