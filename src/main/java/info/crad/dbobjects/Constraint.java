package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Constraint implements DbObject {

  protected Table table;
  protected String name;
  protected String constraintType;
  protected String status;
  protected String indexName;
  protected List<String> columns = new ArrayList<>();

  public void setIndex(List<String> columns) {
    this.columns = columns;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  @JsonProperty("name")
  public String name() {
    return name;
  }

  public String getConstraintType() {
    return constraintType;
  }

  public String getStatus() {
    return status;
  }

  public String getIndexName() {
    return indexName;
  }

  public void addColumn(String column) {
    columns.add(column);
  }

  public List<String> getColumns() {
    return columns;
  }

  public String createStatement() {
    StringBuilder stmt = new StringBuilder("alter table ").append(table.name().toUpperCase()).append("\n");
    if (constraintType.equals("P")) {
      stmt.append("  add constraint ").append(name).append(" primary key ");
      stmt.append("(").append(String.join(", ", columns)).append(")\n");

      stmt.append("  using index\n");
      final Index index = table.getIndex(indexName);
      stmt.append(index.getTablespace().create()).append(";\n");
      if (index.getLogging().equals("NO"))
        stmt.append("alter index ").append(index.name()).append(" nologging;");
      stmt.append("\n");
    } else if (constraintType.equals("R")) {
//      StringBuilder stmt = new StringBuilder("alter table ").append(table.name().toUpperCase()).append("\n");
//      stmt.append("  add constraint ").append(name).append(constraintType.equals("P") ? " foreign key " : "");
//      final String cols = columns.stream().collect(Collectors.joining(", ", "(", ")"));
//      stmt.append("  references "); // TODO .append("EIGKLA" (KL_EIGNER);
      return "";
    }
    return stmt.toString();
  }

  public String toString() {
    return name;
  }

  public String typeShort() {
    return "sql";
  }

}
