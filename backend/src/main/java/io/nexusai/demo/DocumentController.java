package io.nexusai.demo;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController @RequestMapping("/api")
class DocumentController {
 private final DocumentService service;
 DocumentController(DocumentService service){this.service=service;}
 record Question(String question,UUID documentId){}
 @GetMapping("/documents") List<DocumentService.Document> list(Principal p){return service.list(p.getName());}
 @PostMapping("/documents") DocumentService.Document upload(Principal p,@RequestParam("file") MultipartFile file)throws IOException{
  String name=Optional.ofNullable(file.getOriginalFilename()).orElse("unknown").replaceAll("[\\p{Cntrl}/\\\\]","_");
  if(name.length()>200||file.isEmpty())throw new IllegalArgumentException("Invalid file");
  return service.ingest(p.getName(),name,DocumentParser.parse(name,file.getBytes()));
 }
 @DeleteMapping("/documents/{id}") ResponseEntity<Void> delete(Principal p,@PathVariable UUID id){service.delete(p.getName(),id);return ResponseEntity.noContent().build();}
 @PostMapping("/chat") DocumentService.Answer ask(Principal p,@RequestBody Question q){return service.ask(p.getName(),q.question(),q.documentId());}
 @ExceptionHandler({IllegalArgumentException.class,IOException.class}) ResponseEntity<Map<String,String>> invalid(Exception e){return ResponseEntity.badRequest().body(Map.of("error","Invalid document or request"));}
}
