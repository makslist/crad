package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleTrigger extends Trigger {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'TRIGGER'  AND OBJECT_NAME NOT LIKE 'ZZ_UPD_%'";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'TRIGGER' AND NAME = ? ORDER BY LINE";
  private static final Function<Trigger, String> DDL = trigger -> "create or replace " + String.join("\n", trigger.code()) + "\n";

  public static void create(Connection conn, Trigger trigger) {
    DbObject.create(conn, OracleTrigger.DDL.apply(trigger));
  }

  public static List<String> allTriggers(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleTrigger.ALL_SQL)) {
      List<String> triggers = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        triggers.add(resultSet.getString("OBJECT_NAME"));
      return triggers;
    }
  }

  public static OracleTrigger get(Connection conn, String name) throws SQLException {
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
    OracleTrigger trigger = new OracleTrigger();
    trigger.name = name;
    trigger.code = code;
    return trigger;
  }

}
