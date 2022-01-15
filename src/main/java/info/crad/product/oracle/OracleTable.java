package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

public class OracleTable extends Table {

  public static final String ALL_SQL = "SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME NOT LIKE ('%$%')";
  private static final String SQL = "SELECT * FROM USER_TABLES LEFT OUTER JOIN USER_TAB_COMMENTS USING (TABLE_NAME) WHERE TABLE_NAME = ? AND TABLE_NAME NOT LIKE ('%$%')";

  private static final Function<Table, String> DDL = table -> {
    StringBuilder stmt = new StringBuilder("create table ").append(table.name.toUpperCase()).append("\n");
    final String cols = table.getColumns().stream().map(Table.Column::definition).collect(Collectors.joining(", \n", "(\n", "\n)"));
    stmt.append(cols).append("\n");

    stmt.append(table.getTablespace().create()).append(";\n");

    final String colsComts = table.getColumns().stream().filter(c -> c.getComments() != null).map(Table.Column::alterComment).collect(Collectors.joining("\n"));
    stmt.append(colsComts).append("\n");

    final String idxs = table.getIndexes().stream().filter(i -> !table.isConstraint(i)).map(Index::createStatement).collect(Collectors.joining("\n"));
    stmt.append(idxs).append("\n");

    final String cons = table.getConstraints().stream().map(Constraint::createStatement).collect(Collectors.joining("\n"));
    stmt.append(cons).append("\n");

    for (Table.Privilege privs : table.privileges.stream().sorted().collect(Collectors.toList())) {
      stmt.append("grant ");
      stmt.append(privs.sortedActions().map(String::toLowerCase).collect(Collectors.joining(", ")));
      stmt.append(" on ").append(table.name).append(" to ").append(privs.getGrantee()).append(privs.getGrantable().equals("Y") ? " with grant option " : "").append(";\n");
    }

    return stmt.toString();
  };

  private OracleTable(ResultSet resultSet) throws SQLException {
    name = resultSet.getString("TABLE_NAME");
    comments = resultSet.getString("COMMENTS");
    tablespace = new Tablespace(resultSet.getString("TABLESPACE_NAME"),
        resultSet.getString("PCT_FREE"),
        resultSet.getLong("INI_TRANS"),
        resultSet.getLong("MAX_TRANS"),
        resultSet.getLong("INITIAL_EXTENT"),
        resultSet.getLong("NEXT_EXTENT"),
        resultSet.getLong("MIN_EXTENTS"),
        resultSet.getLong("MAX_EXTENTS"));
  }

  public static void create(Connection conn, Table table) {
    DbObject.create(conn, OracleTable.DDL.apply(table));
  }

  public static Table get(Connection conn, String name) throws SQLException {
    try (PreparedStatement stmtTab = conn.prepareStatement(SQL)) {
      stmtTab.setString(1, name);
      ResultSet resultTab = stmtTab.executeQuery();
      if (resultTab.next()) {
        OracleTable table = new OracleTable(resultTab);
        Column.select(conn, table);
        OracleIndex.select(conn, table);
        OracleConstraint.select(conn, table);
        OracleTable.Privilege.select(conn, table);
        return table;
      } else
        throw new RuntimeException("Table " + name + " not found");
    }
  }

  public List<String> allTables(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleTable.ALL_SQL)) {
      List<String> tables = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        tables.add(resultSet.getString("TABLE_NAME"));
      return tables;
    }
  }

  public boolean isEqual(Table obj) {
    return Objects.equals(name, obj.name)
        && Objects.equals(getTablespace(), obj.getTablespace())
        && Objects.equals(getComments(), obj.getComments())
        && Objects.deepEquals(getColumns(), obj.getColumns())
        && Objects.deepEquals(getIndexes(), obj.getIndexes())
        && Objects.deepEquals(getConstraints(), obj.getConstraints())
        && Objects.deepEquals(privileges, obj.privileges);
  }

  public String alterTo(OracleTable obj) {
    return null;
  }

  public static class Column extends Table.Column {

    public static final String SQL = "SELECT * FROM USER_TAB_COLUMNS LEFT OUTER JOIN USER_COL_COMMENTS USING (TABLE_NAME, COLUMN_NAME) WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";

    private Column(ResultSet resultSet, Table table) throws SQLException {
      this.table = table;
      name = resultSet.getString("COLUMN_NAME");
      dataType = resultSet.getString("DATA_TYPE");
      dataTypeMod = resultSet.getString("DATA_TYPE_MOD");
      dataTypeOwner = resultSet.getString("DATA_TYPE_OWNER");
      dataLength = resultSet.getLong("DATA_LENGTH");
      dataPrecision = resultSet.getLong("DATA_PRECISION");
      dataScale = resultSet.getLong("DATA_SCALE");
      nullable = "Y".equals(resultSet.getString("NULLABLE"));
      columnId = resultSet.getLong("COLUMN_ID");
      defaultLength = resultSet.getLong("DEFAULT_LENGTH");
      dataDefault = resultSet.getString("DATA_DEFAULT");
      charLength = resultSet.getLong("CHAR_LENGTH");
      comments = resultSet.getString("COMMENTS");
    }

    public static void select(Connection conn, OracleTable table) throws SQLException {
      try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
        stmt.setFetchSize(100);
        stmt.setString(1, table.name());
        ResultSet resultCol = stmt.executeQuery();
        while (resultCol.next()) table.addColumn(new Column(resultCol, table));
      }
    }

  }

  public static class Privilege extends Table.Privilege {

    private static final String SQL = "SELECT * FROM USER_TAB_PRIVS WHERE TABLE_NAME = ?";

    private Privilege(ResultSet resultSet, OracleTable table) throws SQLException {
      grantee = resultSet.getString("GRANTEE");
      grantable = resultSet.getString("GRANTABLE");
      actions.add(resultSet.getString("PRIVILEGE"));
    }

    public static void select(Connection conn, OracleTable table) throws SQLException {
      Map<String, Privilege> privileges = new HashMap<>();
      try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
        stmt.setFetchSize(100);
        stmt.setString(1, table.name());
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
          final String key = resultSet.getString("GRANTEE") + resultSet.getString("GRANTABLE");
          Privilege priv = privileges.get(key);
          if (priv == null) {
            priv = new Privilege(resultSet, table);
            table.addPrivilege(priv);
            privileges.put(key, priv);
          } else priv.addAction(resultSet.getString("PRIVILEGE"));
        }
      }
    }

  }

}
