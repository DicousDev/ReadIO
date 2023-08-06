package br.com.readfile;

public class Log {

  public static <T> void error(T message) {
    System.err.println(message);
  }
}
