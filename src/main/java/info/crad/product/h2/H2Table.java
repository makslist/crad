package info.crad.product.h2;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

public class H2Table extends Table {

  private static final String ALL_SQL = "SELECT TABLE_NAME FROM USER_TABLES";
  private static final String SQL = "SELECT * FROM USER_TABLES LEFT OUTER JOIN USER_TAB_COMMENTS USING (TABLE_NAME) WHERE TABLE_NAME = ?";

  private static final Function<Table, String> DDL = table -> {
    StringBuilder stmt = new StringBuilder("create table ").append(table.name.toUpperCase()).append("\n");
    final String cols = table.getColumns().stream().map(Table.Column::definition).collect(Collectors.joining(", \n", "(\n", "\n)"));
    stmt.append(cols).append(";");
    return stmt.toString();
  };

  private H2Table(ResultSet resultSet) throws SQLException {
    name = resultSet.getString("TABLE_NAME");
    comments = resultSet.getString("COMMENTS");
  }

  public static void create(Connection conn, Table table) {
    DbObject.create(conn, H2Table.DDL.apply(table));
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

    public static void select(Connection conn, H2Table table) throws SQLException {
      try (PreparedStatement stmtCol = conn.prepareStatement(SQL)) {
        stmtCol.setFetchSize(100);
        stmtCol.setString(1, table.name());
        ResultSet resultCol = stmtCol.executeQuery();
        while (resultCol.next())
          table.addColumn(new Column(resultCol, table));
      }
    }

  }

  public static class Privilege extends Table.Privilege {

    private static final String SQL = "SELECT * FROM USER_TAB_PRIVS WHERE TABLE_NAME = ?";

    private Privilege(ResultSet resultSet, H2Table table) throws SQLException {
      grantee = resultSet.getString("GRANTEE");
      grantable = resultSet.getString("GRANTABLE");
      actions.add(resultSet.getString("PRIVILEGE"));
    }

    public static void select(Connection conn, H2Table table) throws SQLException {
      Map<String, Privilege> privileges = new HashMap<>();
      try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
        stmt.setString(1, table.name());
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
          final String key = resultSet.getString("GRANTEE") + resultSet.getString("GRANTABLE");
          Privilege priv = privileges.get(key);
          if (priv == null) {
            priv = new Privilege(resultSet, table);
            table.addPrivilege(priv);
            privileges.put(key, priv);
          } else
            priv.addAction(resultSet.getString("PRIVILEGE"));
        }
      }
    }

  }

}
