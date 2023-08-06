package br.com.readfile;

public class Log {

  public static <T> void error(T message) {
    System.err.println(message);
  }

  public static <T> void info(T message) {
    System.out.println(message);
  }
}
