package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleView extends View {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'VIEW' AND OBJECT_NAME NOT LIKE 'V_SST_%' AND OBJECT_NAME NOT LIKE 'V_HIST_%'";
  private static final String SQL = "SELECT * FROM USER_VIEWS WHERE VIEW_NAME = ?";
  private static final String SQL_DDL = "SELECT dbms_metadata.get_ddl('VIEW', ?, ?) AS DDL FROM DUAL";
  private static final Function<View, String> DDL = view -> "create or replace view " + view.name() + " as\n" + String.join("\n", view.code()) + ";\n";

  public static void create(Connection conn, View view) {
    DbObject.create(conn, OracleView.DDL.apply(view));
  }

  public static List<String> allViews(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleView.ALL_SQL)) {
      stmt.setFetchSize(1000);
      List<String> views = new ArrayList<>();
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        views.add(resultSet.getString("OBJECT_NAME"));
      return views;
    }
  }

  public static View get(Connection conn, String name) throws SQLException {
    List<String> text = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setFetchSize(1000);
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String line = resultSet.getString("TEXT");
        text.add(line != null ? line.stripTrailing() : "");
      }
    }

    List<String> ddl = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL_DDL)) {
      stmt.setString(1, name);
      stmt.setString(2, conn.getSchema());
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        ddl.add(resultSet.getString("DDL"));
    }
    final OracleView oracleView = new OracleView();
    oracleView.name = name;
    oracleView.text = text;
    oracleView.ddl = ddl;
    return oracleView;
  }

}
