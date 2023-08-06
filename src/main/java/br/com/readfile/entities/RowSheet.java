package br.com.readfile.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class RowSheet implements Iterable<Cell> {

  private Integer rowNumber;
  private List<Cell> cells;

  public RowSheet(Integer rowNumber, List<Cell> cells) {
    this.rowNumber = rowNumber;
    this.cells = cells;
  }

  @Override
  public Iterator<Cell> iterator() {
    return cells.iterator();
  }

  public Optional<Cell> getCell(int index) {

    if(index < 0 || index >= cells.size()) {
      Optional.empty();
    }

    return Optional.ofNullable(cells.get(index));
  }

  public int getCellsSize() {
    return cells.size();
  }

  public int getRowNumber() {
    return rowNumber;
  }
}
