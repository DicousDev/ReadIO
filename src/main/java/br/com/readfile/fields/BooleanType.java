package br.com.readfile.fields;

public final class BooleanType extends FieldType {

  @Override
  public Object convert(Object value) {
    return Boolean.valueOf(value.toString());
  }
}
