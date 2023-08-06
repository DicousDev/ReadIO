package br.com.readfile;

import java.util.HashMap;
import java.util.Map;

public class WorkMap {

  private Map<String, String> workMap = new HashMap<>();

  public void put(String headerName, String attributeName) {
    workMap.put(headerName, attributeName);
  }

  public boolean constainsElements() {
    return workMap.size() > 0;
  }

  public boolean constainsHeaderName(String headerName) {
    return workMap.containsKey(headerName);
  }

  public String getFieldName(String headerName) {
    String field = workMap.get(headerName);
    return field;
  }
}
