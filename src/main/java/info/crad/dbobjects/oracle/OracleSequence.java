package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;

public class OracleSequence extends Sequence {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'SEQUENCE'";

  private static final String SQL = "SELECT * FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?";

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
