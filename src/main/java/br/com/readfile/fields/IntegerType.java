package br.com.readfile.fields;

public final class IntegerType extends FieldType {

  @Override
  public Object convert(Object value) {

    if(value instanceof Double) {
      Double numeric = Double.parseDouble(value.toString());
      return Integer.valueOf(numeric.intValue());
    }

    return Integer.parseInt(value.toString());
  }
}
