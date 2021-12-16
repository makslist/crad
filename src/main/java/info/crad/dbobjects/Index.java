package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

public class Index implements DbObject, TableSpace {

  private Table table;
  private String name;
  private String indexType;
  private String tableOwner;
  private String tablespaceName;
  private long iniTrans;
  private long maxTrans;
  private long initialExtent;
  private long nextExtent;
  private long minExtents;
  private long maxExtents;
  private long pctFree;
  private String logging;
  private String constraintIndex;

  private List<Column> columns = new ArrayList<>();

  public void setColumns(List<Column> columns) {
    this.columns = columns;
    columns.forEach(c -> c.setIndex(this));
  }

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

  public String getTablespaceName() {
    return tablespaceName;
  }

  public long getIniTrans() {
    return iniTrans;
  }

  public long getMaxTrans() {
    return maxTrans;
  }

  public long getInitialExtent() {
    return initialExtent;
  }

  public long getNextExtent() {
    return nextExtent;
  }

  public long getMinExtents() {
    return minExtents;
  }

  public long getMaxExtents() {
    return maxExtents;
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

  public String createStatement() {
    StringBuilder stmt = new StringBuilder("create index ").append(name.toUpperCase()).append(" on ")
        .append(table.name()).append(" ");

    final String cols = columns.stream().map(Column::name).collect(Collectors.joining(", ", "(", ")"));
    stmt.append(cols).append("\n");

    stmt.append(tablespace());
    stmt.append((logging.equals("NO") ? "  nologging" : "")).append(";");
    stmt.append("\n");

    return stmt.toString();
  }

  public String tablespace() {
    return "  tablespace " + tablespaceName + "\n" +
        "  pctfree " + pctFree + "\n" +
        "  initrans " + iniTrans + "\n" +
        "  maxtrans " + maxTrans + "\n" +
        "  storage\n" + "  (\n" +
        "    initial " + initial() + "\n" +
        "    next " + next() + "\n" +
        "    minextents " + minExtents + "\n" +
        "    maxextents " + (maxExtents >= Integer.MAX_VALUE - 2 ? "unlimited" : maxExtents) + "\n" +
        "  )";
  }

  public String initial() {
    return TableSpace.iec80000_13(initialExtent);
  }

  public String next() {
    return TableSpace.iec80000_13(nextExtent);
  }

  public String toString() {
    return name;
  }

  @Override
  public String type() {
    return "INDEX";
  }

  @Override
  public String typeShort() {
    return "idx";
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

  }

}
