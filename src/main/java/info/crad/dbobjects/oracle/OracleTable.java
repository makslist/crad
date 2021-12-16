package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;

public class OracleTable extends Table {

  public static final String ALL_SQL = "SELECT TABLE_NAME FROM USER_TABLES";

  private static final String SQL = "SELECT * FROM USER_TABLES LEFT OUTER JOIN USER_TAB_COMMENTS USING (TABLE_NAME) WHERE TABLE_NAME = ?";

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
      }
    }
    return null;
  }

  private OracleTable(ResultSet resultSet) throws SQLException {
    name = resultSet.getString("TABLE_NAME");
    tablespaceName = resultSet.getString("TABLESPACE_NAME");
    pctFree = resultSet.getString("PCT_FREE");
    iniTrans = resultSet.getLong("INI_TRANS");
    maxTrans = resultSet.getLong("MAX_TRANS");
    initialExtent = resultSet.getLong("INITIAL_EXTENT");
    nextExtent = resultSet.getLong("NEXT_EXTENT");
    minExtents = resultSet.getLong("MIN_EXTENTS");
    maxExtents = resultSet.getInt("MAX_EXTENTS");
    comments = resultSet.getString("COMMENTS");
  }

  public static class Column extends Table.Column {

    public static final String SQL = "SELECT * FROM USER_TAB_COLUMNS LEFT OUTER JOIN USER_COL_COMMENTS USING (TABLE_NAME, COLUMN_NAME) WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";

    public static void select(Connection conn, OracleTable table) throws SQLException {
      try (PreparedStatement stmtCol = conn.prepareStatement(SQL)) {
        stmtCol.setFetchSize(100);
        stmtCol.setString(1, table.name());
        ResultSet resultCol = stmtCol.executeQuery();
        while (resultCol.next())
          table.addColumn(new Column(resultCol, table));
      }
    }

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

  }

  public static class Privilege extends Table.Privilege {

    private static final String SQL = "SELECT * FROM USER_TAB_PRIVS WHERE TABLE_NAME = ?";

    public static void select(Connection conn, OracleTable table) throws SQLException {
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
            priv.addPriv(resultSet.getString("PRIVILEGE"));
        }
      }
    }

    private Privilege(ResultSet resultSet, OracleTable table) throws SQLException {
      grantee = resultSet.getString("GRANTEE");
      grantable = resultSet.getString("GRANTABLE");
      privileges.add(resultSet.getString("PRIVILEGE"));
    }

  }

}
