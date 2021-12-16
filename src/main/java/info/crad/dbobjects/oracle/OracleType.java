package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;

public class OracleType extends Type {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'TYPE'";

  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'TYPE' AND NAME = ? ORDER BY LINE";

  public static OracleType get(Connection conn, String name) throws SQLException {
    List<String> code = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
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
