package info.crad.product.h2;

import info.crad.dbobjects.*;

import java.math.*;
import java.sql.*;
import java.util.function.Function;

public class H2Sequence extends Sequence {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'SEQUENCE'";
  private static final String SQL = "SELECT * FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?";

  private static final Function<Sequence, String> DDL = sequence -> "create sequence if not exists " + sequence.name() +
      " as integer" + "\n" +
      "start with " + sequence.getLastNumber() + "\n" +
      (sequence.getIncrementBy() == 1 ? "" : "increment by " + sequence.getIncrementBy() + "\n") +
      (sequence.getMinValue().equals(BigDecimal.ZERO) ? "no minvalue" : ("minvalue " + sequence.getMinValue().max(
          BigDecimal.valueOf(Long.MIN_VALUE)))) + "\n" +
      (sequence.getMaxValue().equals(BigDecimal.ZERO) ? "no maxvalue" : ("maxvalue " + sequence.getMaxValue().min(
          BigDecimal.valueOf(Long.MAX_VALUE - 10)))) + "\n" +
      (sequence.getCacheSize() != 0 ? ("cache " + sequence.getCacheSize()) : "no cache") + "\n" +
      (sequence.getCycleFlag().equals("Y") ? "cycle" : "no cycle") + ";";

  public static void create(Connection conn, Sequence sequence) {
    DbObject.create(conn, H2Sequence.DDL.apply(sequence));
  }

  public static H2Sequence get(Connection conn, String name) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        H2Sequence sequence = new H2Sequence();
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
