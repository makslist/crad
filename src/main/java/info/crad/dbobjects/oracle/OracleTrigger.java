package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;

public class OracleTrigger extends Trigger {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'TRIGGER'  AND OBJECT_NAME NOT LIKE 'ZZ_UPD_%'";

  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'TRIGGER' AND NAME = ? ORDER BY LINE";

  public static OracleTrigger get(Connection conn, String name) throws SQLException {
    List<String> code = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String line = resultSet.getString("TEXT");
        code.add(line != null ? line.stripTrailing() : "");
      }
    }
    OracleTrigger trigger = new OracleTrigger();
    trigger.name = name;
    trigger.code = code;
    return trigger;
  }

}
