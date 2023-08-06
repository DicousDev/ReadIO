package br.com.readfile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class FieldTypeConvert {

  private static Map<Class<?>, FieldType> fieldsMapper = new HashMap<>() {{
    put(BigDecimal.class, new BigDecimalType());
    put(Integer.class, new IntegerType());
    put(Double.class, new DoubleType());
    put(Boolean.class, new BooleanType());
    put(String.class, new FieldType());
  }};

  public static Object convert(Class<?> fieldType, Object value) {
    if(fieldsMapper.containsKey(fieldType)) {
      return fieldsMapper.get(fieldType).convert(value);
    }

    Log.error(String.format("Unmapped attribute type %s.", value.getClass().getSimpleName()));
    return value;
  }
}
