package br.com.readfile.entities;

import br.com.readfile.Log;
import br.com.readfile.convert.FieldTypeConvert;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class EntityModel<T> {

  private final Class<T> clazz;
  private Constructor<T> construtorWithoutParameters;
  private SoftReference<Map<String, Field>> fields;

  public EntityModel(Class<T> clazz) {

    if(Objects.isNull(clazz)) {
      String erro = "Erro null model";
      Log.error(erro);
      throw new RuntimeException(erro);
    }

    try {
      construtorWithoutParameters = clazz.getConstructor();
    }
    catch (NoSuchMethodException e) {
      String erro = String.format("Error when trying to get model public constructor. Check that the model %s has at least one public parameterless constructor", clazz.getSimpleName());
      Log.error(erro);
      System.err.println(erro);
    }

    this.clazz = clazz;
    loadDeclaredFields();
  }

  public void setField(String fieldName, Object instance, Object value)  {

    try {
      Field field = getDeclaredField(fieldName);
      field.setAccessible(true);

      Object convertedValue = null;
      if(Objects.nonNull(value)) {
        convertedValue = FieldTypeConvert.convert(field.getType(), value);
      }

      field.set(instance, convertedValue);
    }
    catch (IllegalAccessException e) {
      e.printStackTrace();
      Log.error("Error when trying to modify an attribute value");
    }
  }

  public T createInstance() {

    try {
      return construtorWithoutParameters.newInstance();
    }
    catch (Exception e) {
      e.printStackTrace();
      String erro = "Error when trying to create a model object";
      Log.error(erro);
      throw new RuntimeException(erro);
    }
  }

  private void loadDeclaredFields() {
    Field[] fields = clazz.getDeclaredFields();
    Map<String, Field> map = new HashMap<>();
    for(Field field : fields) {
      map.put(field.getName(), field);
    }

    this.fields = new SoftReference<>(map);
  }

  private boolean isFieldsLoaded() {

    if(Objects.isNull(fields) || Objects.isNull(fields.get())) {
      return false;
    }

    if(fields.get().isEmpty()) {
      return false;
    }
    else {
      return true;
    }
  }

  private boolean existsField(String fieldName)  {

    if(!isFieldsLoaded()) {
      loadDeclaredFields();
    }

    return fields.get().containsKey(fieldName);
  }

  private Field getDeclaredField(String name) {

    if(!isFieldsLoaded()) {
      loadDeclaredFields();
    }

    if(existsField(name)) {
      return fields.get().get(name);
    }
    else {
      String erro = String.format("Field [%s] does not exist in class [%s].", name, clazz.getSimpleName());
      Log.error(erro);
      throw new RuntimeException(erro);
    }
  }
}
