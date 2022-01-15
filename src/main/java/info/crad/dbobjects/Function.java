package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Function implements DbObject, Source {

  protected String name;
  protected List<String> code;

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  @JsonProperty("code")
  @Override
  public List<String> code() {
    return code;
  }

  @Override
  public String createStatement() {
    return "create or replace function " + name + " as " + String.join("\n", code) + "\n";
  }

  @Override
  public String typeShort() {
    return "fnc";
  }

}
