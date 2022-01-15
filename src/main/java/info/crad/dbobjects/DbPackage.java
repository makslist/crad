package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

public class DbPackage implements DbObject, Source {

  public List<String> spec;
  public List<String> body;
  protected String name;

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  @JsonProperty("code")
  @Override
  public List<String> code() {
    return Stream.concat(spec.stream(), body.stream()).collect(Collectors.toList());
  }

  @Override
  public String createStatement() {
    return "create or replace " + String.join("\n", spec) + "\n/\n" + "create or replace "
        + String.join("\n", body) + "\n/";
  }

  @Override
  public String typeShort() {
    return "pck";
  }

}
