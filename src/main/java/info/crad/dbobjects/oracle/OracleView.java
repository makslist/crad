package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;

public class OracleView extends View {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'VIEW' AND OBJECT_NAME NOT LIKE 'V_SST_%' AND OBJECT_NAME NOT LIKE 'V_HIST_%'";

  private static final String SQL = "SELECT * FROM USER_VIEWS WHERE VIEW_NAME = ?";
  private static final String SQL_DDL = "SELECT dbms_metadata.get_ddl('VIEW', ?, ?) AS DDL FROM DUAL";

  public static View get(Connection conn, String name) throws SQLException {
    List<String> text = new ArrayList<>();
    try (PreparedStatement stmtText = conn.prepareStatement(SQL)) {
      stmtText.setString(1, name);
      ResultSet resultSet = stmtText.executeQuery();
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
