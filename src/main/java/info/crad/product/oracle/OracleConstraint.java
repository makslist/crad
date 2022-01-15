package info.crad.product.oracle;

import com.fasterxml.jackson.annotation.*;
import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

public class OracleConstraint extends Constraint {

  private static final String SQL = "SELECT * FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE IN ('P', 'R', 'O') AND TABLE_NAME = ?";

  private static final String SQL_COLUMN = "SELECT CC.* FROM USER_CONS_COLUMNS CC JOIN USER_CONSTRAINTS C ON (CC.OWNER = C.OWNER AND CC.CONSTRAINT_NAME = C.CONSTRAINT_NAME) WHERE C.CONSTRAINT_NAME = ? ORDER BY POSITION";
  private List<String> columns = new ArrayList<>();

  private OracleConstraint(ResultSet resultSet, Table table) throws SQLException {
    this.table = table;
    name = resultSet.getString("CONSTRAINT_NAME");
    constraintType = resultSet.getString("CONSTRAINT_TYPE");
    status = resultSet.getString("STATUS");
    indexName = resultSet.getString("INDEX_NAME");
  }

  public static void select(Connection conn, Table table) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, table.name());
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        OracleConstraint constraint = new OracleConstraint(resultSet, table);
        table.addConstraint(constraint);
        selectCol(conn, constraint);
      }
    }
  }

  private static void selectCol(Connection conn, final OracleConstraint constraint) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SQL_COLUMN)) {
      stmt.setString(1, constraint.name());
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        constraint.columns.add(resultSet.getString("COLUMN_NAME"));
    }
  }

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
    stmt.append("  add constraint ").append(name).append(constraintType.equals("P") ? " primary key " : "");
    final String cols = columns.stream().collect(Collectors.joining(", ", "(", ")"));
    stmt.append(cols).append("\n");

    stmt.append("  using index\n");
    final Index index = table.getIndex(indexName);
    stmt.append(index.getTablespace().create()).append(";\n");
    if (index.getLogging().equals("NO"))
      stmt.append("alter index ").append(index.name()).append(" nologging;");
    stmt.append("\n");
    return stmt.toString();
  }

  public String toString() {
    return name;
  }

  public String typeShort() {
    return "sql";
  }

}
