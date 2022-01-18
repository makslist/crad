package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Type implements DbObject, Source {

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
    return "create or replace " + String.join("\n", code) + ";\n";
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof Type))
      return false;

    Type type = (Type) obj;
    return Objects.equals(name, type.name) &&
        Objects.deepEquals(code, type.code);
  }

}
