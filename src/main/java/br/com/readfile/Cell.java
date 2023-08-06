package br.com.readfile;

public class Cell {

  private Object value;
  private Integer columnIndex;

  public Cell(Object value, Integer columnIndex) {
    this.value = value;
    this.columnIndex = columnIndex;
  }

  public Object getValue() {
    return value;
  }

  public Integer getColumnIndex() {
    return columnIndex;
  }
}
