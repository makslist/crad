package info.crad.product.oracle;

import com.fasterxml.jackson.annotation.*;
import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

public class OracleIndex extends Index {

  private static final String SQL = "SELECT I.* FROM USER_INDEXES I JOIN USER_TABLES T ON I.TABLE_NAME = T.TABLE_NAME WHERE T.TABLE_NAME = ?";
  public Tablespace tablespace;
  private Table table;
  private String name;
  private String indexType;
  private String tableOwner;
  private String logging;
  private String constraintIndex;
  private List<Index.Column> columns = new ArrayList<>();

  public OracleIndex() {
  }

  private OracleIndex(ResultSet resultSet, OracleTable table) throws SQLException {
    this.table = table;
    name = resultSet.getString("INDEX_NAME");
    tablespace = new Tablespace(resultSet.getString("TABLESPACE_NAME"),
        resultSet.getString("PCT_FREE"),
        resultSet.getLong("INI_TRANS"),
        resultSet.getLong("MAX_TRANS"),
        resultSet.getLong("INITIAL_EXTENT"),
        resultSet.getLong("NEXT_EXTENT"),
        resultSet.getLong("MIN_EXTENTS"),
        resultSet.getLong("MAX_EXTENTS"));
    logging = resultSet.getString("LOGGING");
    constraintIndex = resultSet.getString("CONSTRAINT_INDEX");
  }

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

  public void setColumns(List<Index.Column> columns) {
    this.columns = columns;
    columns.forEach(c -> c.setIndex(this));
  }

  public String createStatement() {
    StringBuilder stmt = new StringBuilder("create index ").append(name.toUpperCase()).append(" on ")
        .append(table.name()).append(" ");

    final String cols = columns.stream().map(Index.Column::name).collect(Collectors.joining(", ", "(", ")"));
    stmt.append(cols).append("\n");

    stmt.append(getTablespace().create()).append("\n");
    stmt.append((logging.equals("NO") ? "  nologging" : "")).append(";");
    stmt.append("\n");

    return stmt.toString();
  }

  public String toString() {
    return name;
  }

  @Override
  public String typeShort() {
    return null;
  }

  public static class Column extends Index.Column {

    private static final String SQL = "SELECT C.* FROM USER_IND_COLUMNS C JOIN USER_INDEXES I ON (C.INDEX_NAME = I.INDEX_NAME) WHERE I.INDEX_NAME = ? ORDER BY COLUMN_POSITION";

    private Column(ResultSet resultSet, final OracleIndex index) throws SQLException {
      this.index = index;
      name = resultSet.getString("COLUMN_NAME");
      descend = resultSet.getString("DESCEND");
    }

    protected static void select(Connection conn, final OracleIndex index) throws SQLException {
      try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
        stmt.setFetchSize(10);
        stmt.setString(1, index.name());
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next())
          index.addColumn(new Column(resultSet, index));
      }
    }

  }

}
