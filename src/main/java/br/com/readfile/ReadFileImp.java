package br.com.readfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ReadFileImp<T> implements ReadFileAbstract {

  private final EntityModel<T> entityModel;
  private final WorkMap workMap;
  private ReadExcelAbstract reader;

  public ReadFileImp(Class<T> clazz, WorkMap workMap, ReadExcelAbstract reader) {
    entityModel = new EntityModel<T>(clazz);
    this.workMap = workMap;
    this.reader = reader;
  }


  @Override
  public List<T> read(String filePath) {
    if(!workMap.constainsElements()) {
      Log.info("WorkMap has no elements");
      return Collections.emptyList();
    }

    List<T> instances = new ArrayList<>();
    reader.open(filePath);

    Map<Integer, String> headerMap = new HashMap<>();
    reader.getRowSheet().forEach(row -> {

      if(isRowHeader(row)) {

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
      }
      else if(isRowBody(row)) {
        Object instance = entityModel.createInstance();
        for(int i = 0; i < row.getCellsSize(); i++) {
          String headerColumn = headerMap.get(i);
          String fieldModelName = workMap.getFieldName(headerColumn);

          if(Objects.nonNull(fieldModelName)) {
            Object value = null;
            Optional<Cell> cell = row.getCell(i);
            if(cell.isPresent()) {
              value = cell.get().getValue();
            }

            entityModel.setField(fieldModelName, instance, value);
          }
        }

        instances.add((T) instance);
      }
    });

    return instances;
  }

  private boolean isRowHeader(RowSheet row) {
    return row.getRowNumber() == 1;
  }

  private boolean isRowBody(RowSheet row) {
    return row.getRowNumber() > 1;
  }
}
