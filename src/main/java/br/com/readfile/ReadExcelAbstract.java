package br.com.readfile;

import java.util.List;

public interface ReadExcelAbstract {

  void open(String filePath);
  List<RowSheet> getRowSheet();
  void close();
}
