package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

public class Index implements DbObject {

  private Tablespace tablespace;
  private Table table;
  private String name;
  private String indexType;
  private String tableOwner;
  private String logging;
  private String constraintIndex;
  private List<Column> columns = new ArrayList<>();

  public void setTable(Table table) {
    this.table = table;
  }

  @JsonGetter("name")
  public String name() {
    return name;
  }

  public String getIndexType() {
    return indexType;
  }

  public String getTableOwner() {
    return tableOwner;
  }

  public Tablespace getTablespace() {
    return tablespace;
  }

  public String getLogging() {
    return logging;
  }

  public String getConstraintIndex() {
    return constraintIndex;
  }

  public void addColumn(Column column) {
    columns.add(column);
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
    columns.forEach(c -> c.setIndex(this));
  }

  public String createStatement() {
    StringBuilder stmt = new StringBuilder("create index ").append(name.toUpperCase()).append(" on ")
        .append(table.name()).append(" ");

    final String cols = columns.stream().map(Column::name).collect(Collectors.joining(", ", "(", ")"));
    stmt.append(cols).append("\n");

    stmt.append(getTablespace().create());
    stmt.append((logging.equals("NO") ? "  nologging" : "")).append(";");
    stmt.append("\n");

    return stmt.toString();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof Index))
      return false;

    Index idx = (Index) obj;
    return Objects.equals(name, idx.name)
        && Objects.equals(indexType, idx.indexType)
        && Objects.equals(tableOwner, idx.tableOwner)
        && Objects.equals(table.name(), idx.table.name())
        && Objects.deepEquals(columns, idx.columns)
        && Objects.equals(logging, idx.logging)
        && Objects.equals(constraintIndex, idx.constraintIndex)
        && Objects.equals(tablespace, idx.tablespace);
  }

  public String toString() {
    return name;
  }

  public static class Column {

    protected Index index;
    protected String name;
    protected String descend;

    public void setIndex(Index index) {
      this.index = index;
    }

    @JsonProperty("name")
    public String name() {
      return name;
    }

    public String getDescend() {
      return descend;
    }

    public String toString() {
      return name;
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof Column))
        return false;

      Column col = (Column) obj;
      return Objects.equals(name, col.name)
          && Objects.equals(descend, col.descend);
    }

  }

}
