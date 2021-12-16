package info.crad.dbobjects;

import info.crad.export.*;

import java.io.*;
import java.sql.*;

public interface Product {

  Table table(Connection conn, String tableName) throws SQLException;

  int tables(Connection conn, Exporter exporter) throws SQLException, IOException;

  DbPackage dbPackage(Connection conn, String packageName) throws SQLException;

  int packages(Connection conn, Exporter exporter) throws SQLException, IOException;

  View view(Connection conn, String viewName) throws SQLException;

  int views(Connection conn, Exporter exporter) throws SQLException, IOException;

  Synonym synonym(Connection conn, String synonymName) throws SQLException;

  int synonyms(Connection conn, Exporter exporter) throws SQLException, IOException;

  Function function(Connection conn, String functionName) throws SQLException;

  int functions(Connection conn, Exporter exporter) throws SQLException, IOException;

  Procedure procedure(Connection conn, String procedureName) throws SQLException;

  int procedures(Connection conn, Exporter exporter) throws SQLException, IOException;

  JavaSource javaSource(Connection conn, String javaSourceName) throws SQLException;

  int javaSources(Connection conn, Exporter exporter) throws SQLException, IOException;

  Library library(Connection conn, String libraryName) throws SQLException;

  int libraries(Connection conn, Exporter exporter) throws SQLException, IOException;

  Sequence sequence(Connection conn, String sequenceName) throws SQLException;

  int sequences(Connection conn, Exporter exporter) throws SQLException, IOException;

  Trigger trigger(Connection conn, String sequenceName) throws SQLException;

  int triggers(Connection conn, Exporter exporter) throws SQLException, IOException;

  Type type(Connection conn, String sequenceName) throws SQLException;

  int types(Connection conn, Exporter exporter) throws SQLException, IOException;

}
