package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;

import java.sql.*;

public class OracleSynonym extends Synonym {

  public static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'SYNONYM'";

  private static final String SQL = "SELECT * FROM USER_SYNONYMS WHERE SYNONYM_NAME = ?";

  public static Synonym get(Connection conn, String name) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next())
        return new OracleSynonym(resultSet);
    }
    return null;
  }

  private OracleSynonym(ResultSet resultSet) throws SQLException {
    this.name = resultSet.getString("SYNONYM_NAME");
    this.tableOwner = resultSet.getString("TABLE_OWNER");
    this.tableName = resultSet.getString("TABLE_NAME");
    this.dbLink = resultSet.getString("DB_LINK");
  }

}
