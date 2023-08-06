package br.com.readfile.abstracts;

import br.com.readfile.entities.RowSheet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

public interface ReaderExcel {

  void open(String filePath) throws FileNotFoundException, IOException;
  Stream<RowSheet> getRowSheet();
  void close();
}
