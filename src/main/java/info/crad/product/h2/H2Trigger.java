package info.crad.product.h2;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class H2Trigger extends Trigger {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'TRIGGER'  AND OBJECT_NAME NOT LIKE 'ZZ_UPD_%'";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'TRIGGER' AND NAME = ? ORDER BY LINE";
  private static final Function<Trigger, String> DDL = trigger -> "create " + String.join("\n", trigger.code()) + "\n";

  public static void create(Connection conn, Trigger trigger) {
    DbObject.create(conn, H2Trigger.DDL.apply(trigger));
  }

  public static H2Trigger get(Connection conn, String name) throws SQLException {
    List<String> code = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String line = resultSet.getString("TEXT");
        code.add(line != null ? line.stripTrailing() : "");
      }
    }
    H2Trigger trigger = new H2Trigger();
    trigger.name = name;
    trigger.code = code;
    return trigger;
  }

}
