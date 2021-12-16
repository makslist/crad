package info.crad.dbobjects.oracle;

import com.fasterxml.jackson.annotation.*;
import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

public class OracleIndex extends Index {

  private static final String SQL = "SELECT I.* FROM USER_INDEXES I JOIN USER_TABLES T ON I.TABLE_NAME = T.TABLE_NAME WHERE T.TABLE_NAME = ?";

  public static void select(Connection conn, OracleTable table) throws SQLException {
    try (PreparedStatement stmtIdx = conn.prepareStatement(SQL)) {
      stmtIdx.setString(1, table.name());
      ResultSet resultCol = stmtIdx.executeQuery();
      while (resultCol.next()) {
        final OracleIndex index = new OracleIndex(resultCol, table);
        table.addIndex(index);
        Column.select(conn, index);
      }
    }
  }

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

  private List<Index.Column> columns = new ArrayList<>();

  public OracleIndex() {
  }

  private OracleIndex(ResultSet resultSet, OracleTable table) throws SQLException {
    this.table = table;
    name = resultSet.getString("INDEX_NAME");
    tablespaceName = resultSet.getString("TABLESPACE_NAME");
    iniTrans = resultSet.getLong("INI_TRANS");
    maxTrans = resultSet.getLong("MAX_TRANS");
    initialExtent = resultSet.getLong("INITIAL_EXTENT");
    nextExtent = resultSet.getLong("NEXT_EXTENT");
    minExtents = resultSet.getLong("MIN_EXTENTS");
    maxExtents = resultSet.getLong("MAX_EXTENTS");
    logging = resultSet.getString("LOGGING");
    constraintIndex = resultSet.getString("CONSTRAINT_INDEX");
  }

  public void setColumns(List<Index.Column> columns) {
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

  public List<Index.Column> getColumns() {
    return columns;
  }

  public String createStatement() {
    StringBuilder stmt = new StringBuilder("create index ").append(name.toUpperCase()).append(" on ")
        .append(table.name()).append(" ");

    final String cols = columns.stream().map(Index.Column::name).collect(Collectors.joining(", ", "(", ")"));
    stmt.append(cols).append("\n");

    stmt.append(tablespace()).append("\n");
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
    return null;
  }

  public static class Column extends Index.Column {

    private static final String SQL = "SELECT C.* FROM USER_IND_COLUMNS C JOIN USER_INDEXES I ON (C.INDEX_NAME = I.INDEX_NAME) WHERE I.INDEX_NAME = ? ORDER BY COLUMN_POSITION";

    protected static void select(Connection conn, final OracleIndex index) throws SQLException {
      try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
        stmt.setString(1, index.name());
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next())
          index.addColumn(new Column(resultSet, index));
      }
    }

    private Column(ResultSet resultSet, final OracleIndex index) throws SQLException {
      this.index = index;
      name = resultSet.getString("COLUMN_NAME");
      descend = resultSet.getString("DESCEND");
    }

  }

}
