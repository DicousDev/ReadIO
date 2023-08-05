package br.com.readfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

public final class ReadFileImp<T> implements ReadFileAbstract {

  private EntityModel<T> entityModel;
  private WorkMap workMap;

  public ReadFileImp(Class<T> clazz, WorkMap workMap) {
    entityModel = new EntityModel<T>(clazz);
    this.workMap = workMap;
  }

  @Override
  public List<T> read(String location) {
    // Excel
    List<T> instances = new ArrayList<>();
    FileInputStream file = null;
    ReadableWorkbook readableWorkbook = null;
    Stream<Row> rows = null;
    try {
      file = new FileInputStream(location);
      readableWorkbook = new ReadableWorkbook(file);
      Sheet sheet = readableWorkbook.getFirstSheet();
      rows = sheet.openStream();
      //int rowsBodyTotal = sheet.read().size();

      Map<Integer, String> map = new HashMap<>();
      rows.forEach(row -> {

        if(isRowHeader(row)) {
          for(Cell cell : row) {
            map.put(cell.getColumnIndex(), cell.getRawValue());
          }
        }
        else if(isRowBody(row)) {
          //Double progress = Double.valueOf(row.getRowNum()) / Double.valueOf(rowsBodyTotal);
          Object instance = entityModel.createInstance();
          for(int i = 0; i < row.getCellCount(); i++) {
            Object value = row.getCell(i).getValue();
            String headerColumn = map.get(i);
            String fieldModelName = workMap.getFieldModelName(headerColumn).orElseGet(null);
            entityModel.setField(fieldModelName, instance, value);
          }

          instances.add((T) instance);
        }
      });
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
      System.err.println("Arquivo Excel não encontrado na pasta resources.");
    }
    catch (IOException e) {
      e.printStackTrace();
      System.err.println("Erro ao tentar fazer leitura do arquivo Excel.");
    }
    finally {

      try {rows.close(); }
      catch (Exception e) { System.err.println("Erro ao tentar fechar Stream rows [finally]."); }

      try { readableWorkbook.close(); }
      catch (Exception e) { System.err.println("Erro ao tentar fechar ReadableWorkbook [finally]."); }

      try { file.close(); }
      catch (Exception e) { System.err.println("Erro ao tentar fechar File [finally]."); }
    }

    return instances;
  }

  private boolean isRowHeader(Row row) {
    return row.getRowNum() == 1;
  }

  private boolean isRowBody(Row row) {
    return row.getRowNum() > 1;
  }
}
