package br.com.readfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

public interface ReaderExcel {

  void open(String filePath) throws FileNotFoundException, IOException;
  Stream<RowSheet> getRowSheet();
  void close();
}
