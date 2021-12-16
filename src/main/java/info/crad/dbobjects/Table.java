package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

@JsonPropertyOrder({"name", "tablespaceName", "comments"})
public class Table implements DbObject, TableSpace {

  public boolean equalsPk(Table table) {
    return name().equals(table.name());
  }

  protected String name;
  protected String tablespaceName;
  protected String pctFree;
  protected long iniTrans;
  protected long maxTrans;
  protected long initialExtent;
  protected long nextExtent;
  protected long minExtents;
  protected int maxExtents;
  protected String comments;

  private List<Column> columns = new ArrayList<>();
  private List<Index> indexes = new ArrayList<>();
  private List<Constraint> constraints = new ArrayList<>();
  private List<Privilege> privileges = new ArrayList<>();

  public void setColumns(List<Column> columns) {
    this.columns = new ArrayList<>(columns);
    this.columns.forEach(c -> c.setTable(this));
  }

  public void setIndexes(List<Index> indexes) {
    this.indexes = indexes;
    indexes.forEach(i -> i.setTable(this));
  }

  public void setConstraints(List<Constraint> constraints) {
    this.constraints = constraints;
    constraints.forEach(c -> c.setTable(this));
  }

  public void setPrivileges(List<Privilege> privileges) {
    this.privileges = privileges;
  }

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  public String getTablespaceName() {
    return tablespaceName;
  }

  public String getPctFree() {
    return pctFree;
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

  public String getComments() {
    return comments;
  }

  public void addColumn(Column column) {
    columns.add(column);
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void addIndex(Index index) {
    indexes.add(index);
  }

  public List<Index> getIndexes() {
    return indexes;
  }

  public void addConstraint(Constraint constraint) {
    constraints.add(constraint);
  }

  public List<Constraint> getConstraints() {
    return constraints;
  }

  public void addPrivilege(Privilege priv) {
    privileges.add(priv);
  }

  public Index getIndex(String indexName) {
    return indexes.stream().filter(i -> i.name().equals(indexName)).findAny().orElseThrow();
  }

  public String createStatement() {
    StringBuilder stmt = new StringBuilder("create table ").append(name.toUpperCase()).append("\n");
    final String cols = columns.stream().map(Column::definition).collect(Collectors.joining(", \n", "(\n", "\n)"));
    stmt.append(cols).append("\n");

//  TODO ANSI SQL?  stmt.append(tablespace()).append(";\n");

    final String comms = columns.stream().filter(c -> c.getComments() != null).map(Column::alterComment)
        .collect(Collectors.joining("\n"));
    stmt.append(comms).append("\n");

    final String idxs = indexes.stream().filter(i -> !isConstraint(i)).map(Index::createStatement)
        .collect(Collectors.joining("\n"));
    stmt.append(idxs).append("\n");

    final String cons = constraints.stream().map(Constraint::createStatement).collect(Collectors.joining("\n"));
    stmt.append(cons).append("\n");

    for (Privilege grantee : privileges.stream().sorted().collect(Collectors.toList())) {
      stmt.append("grant ");
      stmt.append(grantee.getPrivileges().map(String::toLowerCase).collect(Collectors.joining(", ")));
      stmt.append(" on ").append(name).append(" to ").append(grantee.getGrantee())
          .append(grantee.getGrantable().equals("Y") ? " with grant option " : "").append(";\n");
    }

    return stmt.toString();
  }

  private boolean isConstraint(Index index) {
    return constraints.stream().anyMatch(c -> index.name().equals(c.getIndexName()));
  }

  @Override
  public String tablespace() {
    return "tablespace " + tablespaceName + "\n" +
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

  @Override
  public String initial() {
    return TableSpace.iec80000_13(initialExtent);
  }

  @Override
  public String next() {
    return TableSpace.iec80000_13(nextExtent);
  }

  public String diff(Table table) {
    StringBuilder sb = new StringBuilder();
    sb.append("Diffing ").append(table.name());
    if (!equalsPk(table))
      sb.append("old value: ").append(name()).append(" new value: ").append(table.name());

    Column.diff(getColumns(), table.getColumns());

    if (!getTablespaceName().equals(table.getTablespaceName()))
      sb.append("old value: ").append(getTablespaceName()).append(" new value: ").append(table.getTablespaceName());
    System.out.println(sb);
    return sb.toString();
  }

  @Override
  public String type() {
    return "TABLE";
  }

  @Override
  public String typeShort() {
    return "tab";
  }

  public static class Column {

    public static List<Column> diff(List<Column> columns1, List<Column> columns2) {
      List<Column> missing = columns1.stream().filter(e1 -> columns2.stream().noneMatch(e2 -> e2.equalsName(e1)))
          .collect(Collectors.toList());
      System.out.println("missing columns; :" + missing);

      List<Column> toDelete = columns2.stream().filter(e2 -> columns1.stream().noneMatch(e1 -> e1.equalsName(e2)))
          .collect(Collectors.toList());
      System.out.println("toDelete columns; :" + toDelete);

      List<Column> toUpdate = columns1.stream()
          .filter(e1 -> columns2.stream().anyMatch(e2 -> e2.equalsName(e1) && !e2.equals(e1)))
          .collect(Collectors.toList());
      System.out.println("toUpdate columns; :" + toUpdate);

      return Stream.concat(Stream.concat(missing.stream(), toDelete.stream()), toUpdate.stream())
          .collect(Collectors.toList());
    }

    protected Table table;
    protected String name;
    protected String dataType;
    protected String dataTypeMod;
    protected String dataTypeOwner;
    protected Long dataLength;
    protected Long dataPrecision;
    protected Long dataScale;
    protected boolean nullable;
    protected long columnId;
    protected long defaultLength;
    protected String dataDefault;
    protected long charLength;
    protected String comments;

    public void setTable(Table table) {
      this.table = (Table) table;
    }

    @JsonProperty("name")
    public String name() {
      return name;
    }

    public String getDataType() {
      return dataType;
    }

    public String getDataTypeMod() {
      return dataTypeMod;
    }

    public String getDataTypeOwner() {
      return dataTypeOwner;
    }

    public Long getDataLength() {
      return dataLength;
    }

    public Long getDataPrecision() {
      return dataPrecision;
    }

    public Long getDataScale() {
      return dataScale;
    }

    public boolean isNullable() {
      return nullable;
    }

    public long getColumnId() {
      return columnId;
    }

    public long getDefaultLength() {
      return defaultLength;
    }

    public String getDataDefault() {
      return dataDefault != null ? dataDefault.trim() : null;
    }

    public long getCharLength() {
      return charLength;
    }

    public String getComments() {
      return comments;
    }

    public String dataTypeDefinition() {
      switch (dataType) {
        case "VARCHAR2":
          return dataType + "(" + charLength + ")";
        case "NUMBER":
          return dataType
              + (dataPrecision != 0 ? ("(" + dataPrecision + (dataScale != 0 ? "," + dataScale : "") + ")") : "");
        default:
          return dataType;
      }
    }

    public String definition() {
      return "  " + name.toLowerCase() + "   " + dataTypeDefinition() + dataDefault() + (!nullable ? " not null" : "");
    }

    public String dataDefault() {
      return getDataDefault() != null ? " default " + getDataDefault() : "";
    }

    public String alterComment() {
      return getComments() == null ? null
          : "comment on column " + table.name() + "." + name().toLowerCase() + "\n  is '" + getComments() + "';";
    }

    public String toString() {
      return name + ": " + comments;
    }

    public boolean equals(Column col) {
      if (col == null)
        return false;

      return equalsName(col) && dataTypeDefinition().equals(col.dataTypeDefinition())
          && dataDefault().equals(col.dataDefault()) && isNullable() == col.isNullable();
    }

    public boolean equalsName(Column col) {
      return name().equalsIgnoreCase(col.name());
    }

    public Column diff(Column column) {
      if (!name().equals(column.name()) || !getDataType().equals(column.getDataType())
          || !getDataTypeMod().equals(column.getDataTypeMod()) || !getDataTypeOwner().equals(column.getDataTypeOwner()))
        return column;
      if (!getDataLength().equals(column.getDataLength()))
        return column;
      if (!getDataPrecision().equals(column.getDataPrecision()))
        return column;
      if (!getDataScale().equals(column.getDataScale()))
        return column;
      if (isNullable() == column.isNullable())
        return column;
      if (getColumnId() != column.getColumnId())
        return column;
      if (getDefaultLength() != column.getDefaultLength())
        return column;
      if (!getDataDefault().equals(column.getDataDefault()))
        return column;
      if (getCharLength() != column.getCharLength())
        return column;
      if (!getComments().equals(column.getComments()))
        return column;
      return null;
    }

  }

  public static class Privilege implements Comparable<Privilege> {

    public static final List<String> ORDER = Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE");

    protected String grantee;
    protected String grantable;
    protected final List<String> privileges = new ArrayList<>();

    protected void addPriv(String priv) {
      privileges.add(priv);
    }

    public String getGrantee() {
      return grantee;
    }

    public String getGrantable() {
      return grantable;
    }

    public Stream<String> getPrivileges() {
      return privileges.stream().sorted(Comparator.comparingInt(ORDER::indexOf));
    }

    @Override
    public int compareTo(Privilege o) {
      return getGrantee().compareTo(o.getGrantee());
    }

  }

}
