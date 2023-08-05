package br.com.readfile;

import java.util.List;

public interface ReadFileAbstract<T> {

  List<T> read(String location);
}
