package br.com.readfile;

import java.math.BigDecimal;

public class User {

  private BigDecimal id;
  private String title;
  private String name;

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
