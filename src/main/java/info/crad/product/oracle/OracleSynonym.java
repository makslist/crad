package info.crad.product.oracle;

import info.crad.dbobjects.*;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class OracleSynonym extends Synonym {

  private static final String ALL_SQL = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = 'SYNONYM'";
  private static final String SQL = "SELECT * FROM USER_SYNONYMS WHERE SYNONYM_NAME = ?";
  private static final Function<Synonym, String> DDL = synonym -> "create or replace synonym " + synonym.name() + " for " + synonym.getTableOwner() + "." + synonym.getTableName()
      + (synonym.getDbLink() != null ? "@" + synonym.getDbLink() : "") + ";\n";

  private OracleSynonym(ResultSet resultSet) throws SQLException {
    this.name = resultSet.getString("SYNONYM_NAME");
    this.tableOwner = resultSet.getString("TABLE_OWNER");
    this.tableName = resultSet.getString("TABLE_NAME");
    this.dbLink = resultSet.getString("DB_LINK");
  }

  public static void create(Connection conn, Synonym synonym) {
    DbObject.create(conn, OracleSynonym.DDL.apply(synonym));
  }

  public static List<String> allSynonyms(Connection conn) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(OracleSynonym.ALL_SQL)) {
      List<String> synonyms = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        synonyms.add(resultSet.getString("OBJECT_NAME"));
      return synonyms;
    }
  }

  public static Synonym get(Connection conn, String name) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setString(1, name);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next())
        return new OracleSynonym(resultSet);
    }
    return null;
  }

}
