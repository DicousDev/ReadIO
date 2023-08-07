package br.com.readfile.imp;

import br.com.readfile.Log;
import br.com.readfile.abstracts.ReaderExcel;
import br.com.readfile.entities.Cell;
import br.com.readfile.entities.RowSheet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ApachePoiImp implements ReaderExcel {

  private Workbook workbook = null;
  private Sheet sheet = null;
  private Boolean isOpen = null;

  @Override
  public void open(String filePath) throws FileNotFoundException, IOException {

    if(isOpenValid()) {
      return;
    }

    workbook = new XSSFWorkbook(filePath);
    sheet = getFirstSheet(workbook);
  }

  @Override
  public void close() {

    if(isCloseValid()) {
      return;
    }

    try { workbook.close(); }
    catch (Exception e) { Log.error("Error trying to close WorkBook."); }

    workbook = null;
    sheet = null;
    isOpen = null;
  }

  @Override
  public Stream<RowSheet> getRowSheet() {
    List<RowSheet> rows = new ArrayList<>();
    for(Row row : sheet) {

      List<Cell> cells = new ArrayList<>();
      for(int column = 0; column < row.getLastCellNum(); column++) {

        Cell cell = null;
        if(Objects.nonNull(row.getCell(column))) {
          org.apache.poi.ss.usermodel.Cell cellColumn = row.getCell(column);
          CellType cellType = cellColumn.getCellType();


          Object value = null;
          switch (cellType) {
            case _NONE, BLANK, ERROR -> Log.info("cell type not mapped.");
            case STRING -> value = cellColumn.getStringCellValue();
            case BOOLEAN -> value = cellColumn.getBooleanCellValue();
            case NUMERIC -> value = cellColumn.getNumericCellValue();
          }

          cell = new Cell(value, column);
        }

        cells.add(cell);
      }

      rows.add(new RowSheet(row.getRowNum(), cells));
    }

    return rows.stream();
  }

  private boolean isOpenValid() {
    return Objects.nonNull(isOpen) && isOpen.equals(true);
  }

  private boolean isCloseValid() {
    return Objects.isNull(isOpen) || isOpen.equals(false);
  }

  private Sheet getFirstSheet(Workbook workbook) {
    return workbook.getSheetAt(0);
  }
}
