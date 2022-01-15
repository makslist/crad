package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleProcedure extends Procedure {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PROCEDURE'";
  private static final Function<Procedure, String> DDL = proc -> "create or replace procedure " + proc.name() + " as " + String.join("\n", proc.code()) + "\n";
  private static final String SQL = "SELECT * FROM USER_SOURCE WHERE TYPE = 'PROCEDURE' AND NAME = ? ORDER BY LINE";

  public static void create(Connection conn, Procedure proc) {
    DbObject.create(conn, OracleProcedure.DDL.apply(proc));
  }

  public static List<String> allProcedures(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleProcedure.ALL_SQL)) {
      List<String> procedures = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        procedures.add(resultSet.getString("OBJECT_NAME"));
      return procedures;
    }
  }

  public static OracleProcedure get(Connection conn, String name) throws SQLException {
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
    OracleProcedure procedure = new OracleProcedure();
    procedure.name = name;
    procedure.code = code;
    return procedure;
  }

}
