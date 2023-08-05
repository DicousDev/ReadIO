package br.com.readfile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorkMap {

  private Map<String, String> workMap = new HashMap<>();

  public void put(String headerName, String attributeNameModel) {
    workMap.put(headerName, attributeNameModel);
  }

  public Optional<String> getFieldModelName(String headerName) {
    return Optional.of(workMap.get(headerName));
  }
}
