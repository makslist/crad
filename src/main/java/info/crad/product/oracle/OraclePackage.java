package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OraclePackage extends DbPackage {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PACKAGE' AND OBJECT_NAME NOT LIKE 'ZZ_%'";
  private static final String SQL_SPEC = "SELECT * FROM USER_SOURCE WHERE TYPE = 'PACKAGE' AND NAME = ? ORDER BY LINE";
  private static final String SQL_BODY = "SELECT * FROM USER_SOURCE WHERE TYPE = 'PACKAGE BODY' AND NAME = ? ORDER BY LINE";

  private static final String NATIVE_CODE = "alter session set plsql_code_type = native;";
  private static final String OPTIMIZE = "alter session set plsql_optimize_level = 3;";
  private static final String WARNINGS = "alter session set plsql_warnings = 'enable:all';";

  private static final Function<DbPackage, String> createDdl = dbPackage -> NATIVE_CODE + "\n" + OPTIMIZE + "\n" + WARNINGS + "\n" + "create or replace " + String.join("\n", dbPackage.spec) + "\n/\n" + "create or replace "
      + String.join("\n", dbPackage.body) + "\n/";

  public static void create(Connection conn, DbPackage dbPackage) {
    DbObject.create(conn, OraclePackage.createDdl.apply(dbPackage));
  }

  public static List<String> allDbPackages(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OraclePackage.ALL_SQL)) {
      List<String> packages = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        packages.add(resultSet.getString("OBJECT_NAME"));
      return packages;
    }
  }

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
