package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Synonym implements DbObject {

  protected String name;
  protected String tableOwner;
  protected String tableName;
  protected String dbLink;

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  public String getTableOwner() {
    return tableOwner;
  }

  public String getTableName() {
    return tableName;
  }

  public String getDbLink() {
    return dbLink;
  }

  @Override
  public String createStatement() {
    return "create or replace synonym " + name + " for " + tableOwner + "." + tableName
        + (dbLink != null ? "@" + dbLink : "") + ";\n";
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof Synonym))
      return false;

    Synonym syn = (Synonym) obj;
    return Objects.equals(name, syn.name)
        && Objects.equals(tableOwner, syn.tableOwner)
        && Objects.equals(tableName, syn.tableName)
        && Objects.equals(dbLink, syn.dbLink);
  }

}
