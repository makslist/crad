package info.crad.dbobjects;

import java.sql.*;

public interface DbObject {

  static void create(Connection conn, String ddl) {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(ddl);
    } catch (SQLException e) {
      System.out.format("SQL State: %s: %s\n", e.getSQLState(), e.getMessage());
      System.out.println("    " + ddl);
    }
  }

  String name();

  String createStatement();

  String typeShort();

}
