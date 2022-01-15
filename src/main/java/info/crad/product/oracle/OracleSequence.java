package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleSequence extends Sequence {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'SEQUENCE'";
  private static final Function<Sequence, String> DDL = seq -> "create sequence " + seq.name() + "\n" +
      (seq.getMinValue().equals(BigDecimal.ZERO) ? "nonminvalue" : ("minvalue " + seq.getMinValue())) + "\n" +
      (seq.getMaxValue().equals(BigDecimal.ZERO) ? "nonmaxvalue" : ("maxvalue " + seq.getMaxValue())) + "\n" +
      "start with " + seq.getLastNumber() + "\n" +
      "increment by " + seq.getIncrementBy() + "\n" +
      (seq.getCacheSize() != 0 ? ("cache " + seq.getCacheSize()) : "nocache") + "\n" +
      (seq.getCycleFlag().equals("Y") ? "cycle" : "nocycle") + "\n" + (seq.getOrderFlag().equals("Y") ? "order" : "noorder") + "\n";
  private static final String SQL = "SELECT * FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?";

  public static void create(Connection conn, Sequence seq) {
    DbObject.create(conn, OracleSequence.DDL.apply(seq));
  }

  public static List<String> allSequences(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleSequence.ALL_SQL)) {
      List<String> sequences = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        sequences.add(resultSet.getString("OBJECT_NAME"));
      return sequences;
    }
  }

  public static OracleSequence get(Connection conn, String name) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        OracleSequence sequence = new OracleSequence();
        sequence.name = resultSet.getString("SEQUENCE_NAME");
        sequence.minValue = resultSet.getBigDecimal("MIN_VALUE");
        sequence.maxValue = resultSet.getBigDecimal("MAX_VALUE");
        sequence.incrementBy = resultSet.getLong("INCREMENT_BY");
        sequence.cycleFlag = resultSet.getString("CYCLE_FLAG");
        sequence.orderFlag = resultSet.getString("ORDER_FLAG");
        sequence.cacheSize = resultSet.getLong("CACHE_SIZE");
        sequence.lastNumber = resultSet.getBigDecimal("LAST_NUMBER");
        return sequence;
      }
    }
    return null;
  }

}
