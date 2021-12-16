package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Procedure implements DbObject, Source {

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
    return "create or replace procedure " + name + " as " + String.join("\n", code) + "\n";
  }

  @Override
  public String type() {
    return "PROCEDURE";
  }

  @Override
  public String typeShort() {
    return "prc";
  }

}
