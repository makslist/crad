package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleJavaSource extends JavaSource {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'JAVA SOURCE'";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'JAVA SOURCE' AND NAME = ? ORDER BY LINE";
  private static final Function<JavaSource, String> DDL = java -> "create or replace and compile java source named \"" + java.name() + "\" as" + String.join("\n", java.code()) + "\n";

  public static void create(Connection conn, JavaSource java) {
    DbObject.create(conn, OracleJavaSource.DDL.apply(java));
  }

  public static List<String> allJavaSources(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleJavaSource.ALL_SQL)) {
      List<String> javaSources = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        javaSources.add(resultSet.getString("OBJECT_NAME"));
      return javaSources;
    }
  }

  public static OracleJavaSource get(Connection conn, String name) throws SQLException {
    List<String> code = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setFetchSize(1000);
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String line = resultSet.getString("TEXT");
        code.add(line != null ? line.stripTrailing() : "");
      }
    }
    OracleJavaSource java = new OracleJavaSource();
    java.name = name;
    java.code = code;
    return java;
  }

}
