package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

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

  @Override
  public String typeShort() {
    return "syn";
  }

}
