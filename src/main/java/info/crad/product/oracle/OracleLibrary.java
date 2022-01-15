package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleLibrary extends Library {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'LIBRARY'";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'LIBRARY' AND NAME = ? ORDER BY LINE";
  private static final Function<Library, String> DDL = library -> "create or replace library " + library.name() + " as " + String.join("\n", library.code()) + "\n";

  public static void create(Connection conn, Library lib) {
    DbObject.create(conn, OracleLibrary.DDL.apply(lib));
  }

  public static List<String> allLibraries(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleLibrary.ALL_SQL)) {
      List<String> libraries = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        libraries.add(resultSet.getString("OBJECT_NAME"));
      return libraries;
    }
  }

  public static OracleLibrary get(Connection conn, String name) throws SQLException {
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
    OracleLibrary function = new OracleLibrary();
    function.name = name;
    function.code = code;
    return function;
  }

}
