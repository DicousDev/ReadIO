package br.com.readfile;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class EntityModel<T> {

  private Class<T> clazz;
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

  private void loadDeclaredFields() {
    Field[] fields = clazz.getDeclaredFields();
    Map<String, Field> map = new HashMap<>();
    for(Field field : fields) {
      map.put(field.getName(), field);
    }

    this.fields = new SoftReference<>(map);
  }

  private boolean isFieldsLoaded() {
    return fields.get() != null;
  }

  private boolean existsDeclaredField(String fieldName)  {

    if(!isFieldsLoaded()) {
      loadDeclaredFields();
    }

    return fields.get().containsKey(fieldName);
  }

  private Optional<Field> getDeclaredField(String name) {

    if(!isFieldsLoaded()) {
      loadDeclaredFields();
    }

    if(existsDeclaredField(name)) {
      return Optional.of(fields.get().get(name));
    }
    else {
      Log.error(String.format("Field %s does not exist.", name));
      return Optional.empty();
    }
  }

  public void setField(String fieldName, Object instance, Object value)  {

    try {
      Optional<Field> field = getDeclaredField(fieldName);
      if(field.isPresent()) {
        field.get().setAccessible(true);
        field.get().set(instance, value);
      }
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
}
