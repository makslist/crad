package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

public class DbPackage implements DbObject, Source {

  protected String name;
  protected List<String> spec;
  protected List<String> body;

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
  public String type() {
    return "PACKAGE";
  }

  @Override
  public String typeShort() {
    return "pck";
  }

}
