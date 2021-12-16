package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;

public class OraclePackage extends DbPackage {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PACKAGE' AND OBJECT_NAME NOT LIKE 'ZZ_%'";

  private static final String SQL_SPEC = "SELECT * FROM USER_SOURCE WHERE TYPE = 'PACKAGE' AND NAME = ? ORDER BY LINE";
  private static final String SQL_BODY = "SELECT * FROM USER_SOURCE WHERE TYPE = 'PACKAGE BODY' AND NAME = ? ORDER BY LINE";

  public static DbPackage get(Connection conn, String name) throws SQLException {
    List<String> spec = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL_SPEC)) {
      stmt.setFetchSize(1000);
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String line = resultSet.getString("TEXT");
        spec.add(line != null ? line.stripTrailing() : "");
      }
    }

    List<String> body = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL_BODY)) {
      stmt.setFetchSize(1000);
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String line = resultSet.getString("TEXT");
        body.add(line != null ? line.stripTrailing() : "");
      }
    }
    OraclePackage dbPackage = new OraclePackage();
    dbPackage.name = name;
    dbPackage.spec = spec;
    dbPackage.body = body;
    return dbPackage;
  }

}
