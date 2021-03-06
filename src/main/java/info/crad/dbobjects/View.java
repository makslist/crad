package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonPropertyOrder({"name", "text"})
public class View implements DbObject, Source {

  protected String name;
  protected List<String> text;
  protected List<String> ddl;

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  public List<String> getDdl() {
    return ddl;
  }

  @JsonProperty("text")
  @Override
  public List<String> code() {
    return text;
  }

  @JsonProperty("text")
  public void setText(List<String> text) {
    this.text = text;
  }

  @Override
  public String createStatement() {
    return "create or replace view " + name + " as\n" + String.join("\n", text) + ";\n";
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof View))
      return false;

    View view = (View) obj;
    return Objects.equals(name, view.name) &&
        Objects.equals(text, view.text);
  }

}
