package io.nexusai.demo;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
final class DocumentParser {
 static String parse(String name,byte[] bytes)throws IOException {
  String lower=name.toLowerCase(Locale.ROOT);String text;
  if(lower.endsWith(".pdf")){try(var doc=Loader.loadPDF(bytes)){if(doc.getNumberOfPages()>200)throw new IllegalArgumentException("PDF page limit exceeded");text=new PDFTextStripper().getText(doc);}}
  else if(lower.endsWith(".docx")){try(var doc=new XWPFDocument(new ByteArrayInputStream(bytes))){var out=new StringBuilder();for(var p:doc.getParagraphs())out.append(p.getText()).append('\n');for(var t:doc.getTables())for(var row:t.getRows()){for(var cell:row.getTableCells())out.append(cell.getText()).append(" | ");out.append('\n');}text=out.toString();}}
  else if(lower.endsWith(".xlsx")){try(var wb=new XSSFWorkbook(new ByteArrayInputStream(bytes))){var out=new StringBuilder();var format=new DataFormatter();for(var sheet:wb){out.append(sheet.getSheetName()).append('\n');for(var row:sheet){for(var cell:row)out.append(format.formatCellValue(cell)).append(" | ");out.append('\n');if(out.length()>100000)throw new IllegalArgumentException("Document text limit exceeded");}}text=out.toString();}}
  else if(lower.endsWith(".txt")||lower.endsWith(".csv"))text=new String(bytes,StandardCharsets.UTF_8);
  else throw new IllegalArgumentException("Unsupported file type");
  if(text.isBlank()||text.length()>100000)throw new IllegalArgumentException("Empty document or text limit exceeded");return text;
 }
 static List<String> split(String text){var chunks=new ArrayList<String>();for(int start=0;start<text.length();start+=1000){int end=Math.min(start+1200,text.length());chunks.add(text.substring(start,end));if(end==text.length())break;}return chunks;}
}
