package br.com.readfile.imp;

import br.com.readfile.entities.Cell;
import br.com.readfile.Log;
import br.com.readfile.entities.RowSheet;
import br.com.readfile.abstracts.ReaderExcel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public final class FastExcelReaderImp implements ReaderExcel {

  private FileInputStream file = null;
  private ReadableWorkbook readableWorkbook = null;
  private Stream<Row> rows = null;
  private Boolean isOpen = null;

  @Override
  public void open(String filePath) throws FileNotFoundException, IOException {

    if(isOpenValid()) {
      return;
    }

    file = new FileInputStream(filePath);
    readableWorkbook = new ReadableWorkbook(file);
    rows = readableWorkbook.getFirstSheet().openStream();
    isOpen = true;
  }

  @Override
  public void close() {

    if(isCloseValid()) {
      return;
    }

    try {rows.close(); }
    catch (Exception e) { Log.error("Error trying to close Stream rows."); }

    try { readableWorkbook.close(); }
    catch (Exception e) { Log.error("Error trying to close ReadableWorkbook."); }

    try { file.close(); }
    catch (Exception e) { Log.error("Error trying to close File."); }

    isOpen = null;
    rows = null;
    readableWorkbook = null;
    file = null;
  }

  @Override
  public Stream<RowSheet> getRowSheet() {
    return rows.map(row -> convertRow(row)).collect(Collectors.toList()).stream();
  }

  @Override
  public boolean isFirstRow(int row) {
    return row == 1;
  }

  private boolean isOpenValid() {
    return Objects.nonNull(isOpen) && isOpen.equals(true);
  }

  private boolean isCloseValid() {
    return Objects.isNull(isOpen) || isOpen.equals(false);
  }

  private RowSheet convertRow(Row row) {
    return new RowSheet(row.getRowNum(), convertCells(row));
  }

  private List<Cell> convertCells(Row row) {
    List<org.dhatim.fastexcel.reader.Cell> cells = row.getCells(0, row.getCellCount());
    List<Cell> cellsConverted = new ArrayList<>();
    for(org.dhatim.fastexcel.reader.Cell cell : cells) {
      Cell cellConvert = null;
      if(Objects.nonNull(cell)) {
        cellConvert = new Cell(cell.getValue(), cell.getColumnIndex());
      }

      cellsConverted.add(cellConvert);
    }

    return cellsConverted;
  }
}
