package br.com.readfile.convert;

import br.com.readfile.Log;
import br.com.readfile.fields.BigDecimalType;
import br.com.readfile.fields.BooleanType;
import br.com.readfile.fields.DoubleType;
import br.com.readfile.fields.FieldType;
import br.com.readfile.fields.IntegerType;
import br.com.readfile.fields.LongType;
import java.math.BigDecimal;
import java.util.Map;

public final class FieldTypeConvert {

  private static Map<Class<?>, FieldType> fields = Map.of(
      BigDecimal.class, new BigDecimalType(),
      Integer.class, new IntegerType(),
      Double.class, new DoubleType(),
      Boolean.class, new BooleanType(),
      String.class, new FieldType(),
      Long.class, new LongType(),
      int.class, new IntegerType(),
      long.class, new LongType(),
      double.class, new DoubleType(),
      boolean.class, new BooleanType()
  );

  public static Object convert(Class<?> fieldType, Object value) {
    if(fields.containsKey(fieldType)) {
      return fields.get(fieldType).convert(value);
    }

    Log.error(String.format("Unmapped attribute type %s.", value.getClass().getSimpleName()));
    return value;
  }
}
