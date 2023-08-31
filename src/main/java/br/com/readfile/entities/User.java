package br.com.readfile.entities;

public class User {

  private long id;
  private String title;
  private String name;

  private User() {

  }

  public void show() {
    StringBuilder s = new StringBuilder("id: ");
    s.append(id);
    s.append(" / ");
    s.append("title: ");
    s.append(title);
    s.append(" / ");
    s.append("name: ");
    s.append(name);
    System.out.println(s);
  }
}
