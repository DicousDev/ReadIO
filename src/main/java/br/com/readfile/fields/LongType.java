package br.com.readfile.fields;

public class LongType extends FieldType {

  public Object convert(Object value) {

    if(value instanceof Double) {
      Double numeric = Double.parseDouble(value.toString());
      return Long.valueOf(numeric.longValue());
    }

    return Long.parseLong(value.toString());
  }
}
