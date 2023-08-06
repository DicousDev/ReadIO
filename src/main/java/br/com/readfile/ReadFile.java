package br.com.readfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ReadFile<T> {

  List<T> read(String filePath) throws FileNotFoundException, IOException;
}
