package info.crad.dbobjects.h2;

import info.crad.dbobjects.*;
import info.crad.export.*;

import java.io.*;
import java.sql.*;

public class H2 implements Product {

  @Override
  public Table table(Connection conn, String tableName) throws SQLException {
    return null;
  }

  @Override
  public int tables(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public DbPackage dbPackage(Connection conn, String packageName) throws SQLException {
    return null;
  }

  @Override
  public int packages(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public View view(Connection conn, String viewName) throws SQLException {
    return null;
  }

  @Override
  public int views(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Synonym synonym(Connection conn, String synonymName) throws SQLException {
    return null;
  }

  @Override
  public int synonyms(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Function function(Connection conn, String functionName) throws SQLException {
    return null;
  }

  @Override
  public int functions(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Procedure procedure(Connection conn, String procedureName) throws SQLException {
    return null;
  }

  @Override
  public int procedures(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public JavaSource javaSource(Connection conn, String javaSourceName) throws SQLException {
    return null;
  }

  @Override
  public int javaSources(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Library library(Connection conn, String libraryName) throws SQLException {
    return null;
  }

  @Override
  public int libraries(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Sequence sequence(Connection conn, String sequenceName) throws SQLException {
    return null;
  }

  @Override
  public int sequences(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Trigger trigger(Connection conn, String sequenceName) throws SQLException {
    return null;
  }

  @Override
  public int triggers(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

  @Override
  public Type type(Connection conn, String sequenceName) throws SQLException {
    return null;
  }

  @Override
  public int types(Connection conn, Exporter exporter) throws SQLException, IOException {
    return 0;
  }

}
