package br.com.readfile;

public final class IntegerType extends FieldType {

  @Override
  public Object convert(Object value) {
    return Integer.valueOf(value.toString());
  }
}
