package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Trigger implements DbObject, Source {

  protected String name;
  protected List<String> code;

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  @JsonProperty("code")
  public List<String> code() {
    return code;
  }

  @Override
  public String createStatement() {
    return "create or replace " + String.join("\n", code) + "\n";
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof Trigger))
      return false;

    Trigger trigger = (Trigger) obj;
    return Objects.equals(name, trigger.name) &&
        Objects.deepEquals(code, trigger.code);
  }

}
