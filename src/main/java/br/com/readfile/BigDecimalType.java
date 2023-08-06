package br.com.readfile;

import java.math.BigDecimal;

public final class BigDecimalType extends FieldType {

  @Override
  public Object convert(Object value) {
    return new BigDecimal(value.toString());
  }
}
