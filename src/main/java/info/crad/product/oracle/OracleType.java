package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleType extends Type {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'TYPE'";
  private static final Function<Type, String> DDL = type -> "create or replace " + String.join("\n", type.code()) + ";\n";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'TYPE' AND NAME = ? ORDER BY LINE";

  public static void create(Connection conn, Type type) {
    DbObject.create(conn, OracleType.DDL.apply(type));
  }

  public static List<String> allTypes(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleType.ALL_SQL)) {
      List<String> types = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        types.add(resultSet.getString("OBJECT_NAME"));
      return types;
    }
  }

  public static OracleType get(Connection conn, String name) throws SQLException {
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
    OracleType type = new OracleType();
    type.name = name;
    type.code = code;
    return type;
  }

}
