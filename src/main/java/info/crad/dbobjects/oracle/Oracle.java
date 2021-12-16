package info.crad.dbobjects.oracle;

import info.crad.dbobjects.*;
import info.crad.export.*;

import java.io.*;
import java.sql.*;

public class Oracle implements Product {

  @Override
  public Table table(Connection conn, String tableName) throws SQLException {
    return OracleTable.get(conn, tableName);
  }

  @Override
  public int tables(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleTable.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(table(conn, resultSet.getString("TABLE_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public DbPackage dbPackage(Connection conn, String packageName) throws SQLException {
    return OraclePackage.get(conn, packageName);
  }

  @Override
  public int packages(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OraclePackage.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(dbPackage(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public View view(Connection conn, String viewName) throws SQLException {
    return OracleView.get(conn, viewName);
  }

  @Override
  public int views(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleView.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(view(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Synonym synonym(Connection conn, String synonymName) throws SQLException {
    return OracleSynonym.get(conn, synonymName);
  }

  @Override
  public int synonyms(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleSynonym.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(synonym(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Function function(Connection conn, String functionName) throws SQLException {
    return OracleFunction.get(conn, functionName);
  }

  @Override
  public int functions(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleFunction.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(function(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Procedure procedure(Connection conn, String procedureName) throws SQLException {
    return OracleProcedure.get(conn, procedureName);
  }

  @Override
  public int procedures(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleProcedure.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(procedure(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public JavaSource javaSource(Connection conn, String javaSourceName) throws SQLException {
    return OracleJavaSource.get(conn, javaSourceName);
  }

  @Override
  public int javaSources(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleJavaSource.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(javaSource(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Library library(Connection conn, String libraryName) throws SQLException {
    return OracleLibrary.get(conn, libraryName);
  }

  @Override
  public int libraries(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleLibrary.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(library(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Sequence sequence(Connection conn, String sequenceName) throws SQLException {
    return OracleSequence.get(conn, sequenceName);
  }

  @Override
  public int sequences(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleSequence.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(sequence(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Trigger trigger(Connection conn, String sequenceName) throws SQLException {
    return OracleTrigger.get(conn, sequenceName);
  }

  @Override
  public int triggers(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleTrigger.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(trigger(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

  @Override
  public Type type(Connection conn, String sequenceName) throws SQLException {
    return OracleType.get(conn, sequenceName);
  }

  @Override
  public int types(Connection conn, Exporter exporter) throws SQLException, IOException {
    int sqlCount = 0;
    try (PreparedStatement stmt = conn.prepareStatement(OracleType.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        exporter.write(type(conn, resultSet.getString("OBJECT_NAME")));
        sqlCount++;
      }
    }
    return sqlCount;
  }

}
