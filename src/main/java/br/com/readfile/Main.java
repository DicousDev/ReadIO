package br.com.readfile;import java.util.List;public class Main {  public static void main(String[] args) {    ReadExcelAbstract reader = new FastExcelReaderImp();    WorkMap workMap = new WorkMap();    workMap.put("id", "id");    workMap.put("titulo", "title");    workMap.put("author", "name");    ReadFileAbstract readFile = new ReadFileImp<User>(User.class, workMap, reader);    String filePath = "src/main/resources/Example.xlsx";    List<User> users = readFile.read(filePath);    for(User user : users) {      user.show();    }  }}