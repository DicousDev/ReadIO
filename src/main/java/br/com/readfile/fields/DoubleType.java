package br.com.readfile.fields;

public final class DoubleType extends FieldType {

  public Object convert(Object value) {
    return Double.parseDouble(value.toString());
  }
}
