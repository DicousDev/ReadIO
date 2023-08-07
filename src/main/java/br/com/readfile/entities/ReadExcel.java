package br.com.readfile.entities;

import br.com.readfile.Log;
import br.com.readfile.abstracts.ReadFile;
import br.com.readfile.abstracts.ReaderExcel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ReadExcel<T> implements ReadFile {

  private final EntityModel<T> entityModel;
  private final WorkMap workMap;
  private ReaderExcel reader;

  public ReadExcel(Class<T> clazz, WorkMap workMap, ReaderExcel reader) {
    entityModel = new EntityModel<T>(clazz);
    this.workMap = workMap;
    this.reader = reader;
  }

  @Override
  public List<T> read(String filePath) throws FileNotFoundException, IOException {
    if(!workMap.constainsElements()) {
      Log.info("WorkMap has no elements");
      return Collections.emptyList();
    }

    List<T> instances = new ArrayList<>();

    try {
      reader.open(filePath);
      Map<Integer, String> headerMap = new HashMap<>();
      reader.getRowSheet().forEach(row -> {

        if(isRowHeader(row)) {
          Map<Integer, String> headerMapper = readHeaderRow(row);
          headerMap.putAll(headerMapper);
        }
        else if(isRowBody(row)) {
          T instance = (T) readBodyRow(row, headerMap);
          instances.add(instance);
        }
      });
    }
    catch (FileNotFoundException e) {
      String erro = "Excel file not found.";
      Log.error(erro);
      throw new FileNotFoundException(erro);
    }
    catch (IOException e) {
      String erro = "Error when trying to read the Excel file.";
      Log.error(erro);
      throw new IOException(erro);
    }
    finally {
      reader.close();
    }

    return instances;
  }

  protected Map<Integer, String> readHeaderRow(RowSheet row) {
    Map<Integer, String> headerMap = new HashMap<>();
    for(int i = 0; i < row.getCellsSize(); i++) {
      Optional<Cell> cell = row.getCell(i);
      if(cell.isEmpty()) {
        continue;
      }

      String headerName = cell.get().getValue().toString();
      if(workMap.constainsHeaderName(headerName)) {
        headerMap.put(cell.get().getColumnIndex(), headerName);
      }
    }

    return headerMap;
  }

  protected Object readBodyRow(RowSheet row, Map<Integer, String> headerMap) {
    Object instance = entityModel.createInstance();
    for(int i = 0; i < row.getCellsSize(); i++) {
      String headerColumn = headerMap.get(i);
      String fieldModelName = workMap.getFieldName(headerColumn);

      if(Objects.nonNull(fieldModelName)) {
        Object cellValue = readBodyCell(i, row);
        entityModel.setField(fieldModelName, instance, cellValue);
      }
    }

    return instance;
  }

  protected Object readBodyCell(int i, RowSheet row) {
    Object cellValue = null;
    Optional<Cell> cell = row.getCell(i);
    if(cell.isPresent()) {
      cellValue = cell.get().getValue();
    }

    return cellValue;
  }

  protected boolean isRowHeader(RowSheet row) {
    return reader.isFirstRow(row.getRowNumber());
  }

  protected boolean isRowBody(RowSheet row) {
    return !reader.isFirstRow(row.getRowNumber());
  }
}
