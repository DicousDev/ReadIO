package br.com.readfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

public final class FastExcelReaderImp implements ReadExcelAbstract {

  private FileInputStream file = null;
  private ReadableWorkbook readableWorkbook = null;
  private Stream<Row> rows = null;
  private Sheet sheet = null;
  private Boolean isOpen = false;

  @Override
  public void open(String filePath) {

    try {
      file = new FileInputStream(filePath);
      readableWorkbook = new ReadableWorkbook(file);
      Sheet sheet = readableWorkbook.getFirstSheet();
      rows = sheet.openStream();
      isOpen = true;
    }
    catch (FileNotFoundException e) {
      isOpen = false;
      e.printStackTrace();
      Log.error("Excel file not found in resources folder.");
    }
    catch (IOException e) {
      isOpen = false;
      e.printStackTrace();
      Log.error("Error when trying to read the Excel file.");
    }
  }

  @Override
  public void close() {

    try {rows.close(); }
    catch (Exception e) { Log.error("Error trying to close Stream rows [finally]."); }

    try { readableWorkbook.close(); }
    catch (Exception e) { Log.error("Error trying to close ReadableWorkbook [finally]."); }

    try { file.close(); }
    catch (Exception e) { Log.error("Error trying to close ReadableWorkbook [finally]."); }

    isOpen = null;
    sheet = null;
    rows = null;
    readableWorkbook = null;
    file = null;
  }

  @Override
  public List<RowSheet> getRowSheet() {
    return rows.map(row -> convertRow(row)).collect(Collectors.toList());
  }

  private RowSheet convertRow(Row row) {
    return new RowSheet(row.getRowNum(), convertCells(row));
  }

  private List<Cell> convertCells(Row row) {
    return row.getCells(0, row.getCellCount()).stream()
      .map(cell -> new Cell(cell.getValue(), cell.getColumnIndex()))
      .collect(Collectors.toList());
  }
}
