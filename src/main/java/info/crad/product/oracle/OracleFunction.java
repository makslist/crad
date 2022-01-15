package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;

public class OracleFunction extends Function {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'FUNCTION'";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'FUNCTION' AND NAME = ? ORDER BY LINE";
  private static final java.util.function.Function<Function, String> DDL = function -> "create or replace function " + function.name() + " as " + String.join("\n", function.code()) + "\n";

  public static void create(Connection conn, Function function) {
    DbObject.create(conn, OracleFunction.DDL.apply(function));
  }

  public static List<String> allFunctions(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleFunction.ALL_SQL)) {
      List<String> functions = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        functions.add(resultSet.getString("OBJECT_NAME"));
      return functions;
    }
  }

  public static OracleFunction get(Connection conn, String name) throws SQLException {
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
    OracleFunction function = new OracleFunction();
    function.name = name;
    function.code = code;
    return function;
  }

}
