package br.com.readfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
  public List<T> read(String filePath) {
    // Excel
    if(!workMap.constainsElements()) {
      Log.info("WorkMap has no elements");
      return Collections.emptyList();
    }

    List<T> instances = new ArrayList<>();
    FileInputStream file = null;
    ReadableWorkbook readableWorkbook = null;
    Stream<Row> rows = null;
    try {
      file = new FileInputStream(filePath);
      readableWorkbook = new ReadableWorkbook(file);
      Sheet sheet = readableWorkbook.getFirstSheet();
      rows = sheet.openStream();
      //int rowsBodyTotal = sheet.read().size();

      Map<Integer, String> map = new HashMap<>();
      rows.forEach(row -> {

        if(isRowHeader(row)) {
          for(int i = 0; i < row.getCellCount(); i++) {
            Log.info("Header");
            Optional<Cell> cell = row.getOptionalCell(i);
            if(cell.isEmpty()) {
              continue;
            }

            String headerName = cell.get().getValue().toString();
            if(workMap.constainsHeaderName(headerName)) {
              map.put(cell.get().getColumnIndex(), headerName);
            }
          }
        }
        else if(isRowBody(row)) {
          //Double progress = Double.valueOf(row.getRowNum()) / Double.valueOf(rowsBodyTotal);
          Object instance = entityModel.createInstance();
          for(int i = 0; i < row.getCellCount(); i++) {
            Log.info("Body");
            String headerColumn = map.get(i);
            String fieldModelName = workMap.getFieldName(headerColumn);
            if(Objects.nonNull(fieldModelName)) {
              Object value = null;
              Optional<Cell> cell = row.getOptionalCell(i);
              if(cell.isPresent()) {
                value = cell.get().getValue();
              }

              entityModel.setField(fieldModelName, instance, value);
            }
          }

          instances.add((T) instance);
        }
      });
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
      Log.error("Excel file not found in resources folder.");
    }
    catch (IOException e) {
      e.printStackTrace();
      Log.error("Error when trying to read the Excel file.");
    }
    finally {

      try {rows.close(); }
      catch (Exception e) { Log.error("Error trying to close Stream rows [finally]."); }

      try { readableWorkbook.close(); }
      catch (Exception e) { Log.error("Error trying to close ReadableWorkbook [finally]."); }

      try { file.close(); }
      catch (Exception e) { Log.error("Error trying to close ReadableWorkbook [finally]."); }
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
