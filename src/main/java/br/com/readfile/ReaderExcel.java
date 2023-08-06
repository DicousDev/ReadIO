package br.com.readfile;

import java.util.stream.Stream;

public interface ReaderExcel {

  void open(String filePath);
  Stream<RowSheet> getRowSheet();
  void close();
}
