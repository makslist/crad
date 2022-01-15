package info.crad.dbobjects;

import info.crad.io.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public interface Product {

  Function function(String name) throws SQLException;

  default Function function(String name, Importer importer) throws IOException {
    return importer.read(name, Function.class);
  }

  List<Function> functions() throws SQLException;

  void exportFunctions(Exporter exporter) throws SQLException, IOException;

  void importFunctions(Importer importer) throws IOException, SQLException;

  JavaSource javaSource(String name) throws SQLException;

  default JavaSource javaSource(String name, Importer importer) throws IOException {
    return importer.read(name, JavaSource.class);
  }

  List<JavaSource> javaSources() throws SQLException;

  void exportJavaSources(Exporter exporter) throws SQLException, IOException;

  void importJavaSources(Importer importer) throws IOException, SQLException;

  Library library(String name) throws SQLException;

  default Library library(String name, Importer importer) throws IOException {
    return importer.read(name, Library.class);
  }

  List<Library> libraries() throws SQLException;

  void exportLibraries(Exporter exporter) throws SQLException, IOException;

  void importLibraries(Importer importer) throws IOException, SQLException;

  DbPackage dbPackage(String name) throws SQLException;

  default DbPackage dbPackage(String name, Importer importer) throws IOException {
    return importer.read(name, DbPackage.class);
  }

  List<DbPackage> dbPackages() throws SQLException;

  void exportPackages(Exporter exporter) throws SQLException, IOException;

  void importPackages(Importer importer) throws IOException, SQLException;

  Procedure procedure(String name) throws SQLException;

  default Procedure procedure(String name, Importer importer) throws IOException {
    return importer.read(name, Procedure.class);
  }

  List<Procedure> procedures() throws SQLException;

  void exportProcedures(Exporter exporter) throws SQLException, IOException;

  void importProcedures(Importer importer) throws IOException, SQLException;

  Sequence sequence(String name) throws SQLException;

  default Sequence sequence(String name, Importer importer) throws IOException {
    return importer.read(name, Sequence.class);
  }

  List<Sequence> sequences() throws SQLException;

  void exportSequences(Exporter exporter) throws SQLException, IOException;

  void importSequences(Importer importer) throws IOException, SQLException;

  Synonym synonym(String name) throws SQLException;

  default Synonym synonym(String name, Importer importer) throws IOException {
    return importer.read(name, Synonym.class);
  }

  List<Synonym> synonyms() throws SQLException;

  void exportSynonyms(Exporter exporter) throws SQLException, IOException;

  void importSynonyms(Importer importer) throws IOException, SQLException;

  Table table(String name) throws SQLException;

  default Table table(String name, Importer importer) throws IOException {
    return importer.read(name, Table.class);
  }

  List<Table> tables() throws SQLException;

  void exportTables(Exporter exporter) throws SQLException, IOException;

  void importTables(Importer importer) throws IOException, SQLException;

  Trigger trigger(String name) throws SQLException;

  default Trigger trigger(String name, Importer importer) throws IOException {
    return importer.read(name, Trigger.class);
  }

  List<Trigger> triggers() throws SQLException;

  void exportTriggers(Exporter exporter) throws SQLException, IOException;

  void importTriggers(Importer importer) throws IOException, SQLException;

  Type type(String name) throws SQLException;

  default Type type(String name, Importer importer) throws IOException {
    return importer.read(name, Type.class);
  }

  List<Type> types() throws SQLException;

  void exportTypes(Exporter exporter) throws SQLException, IOException;

  void importTypes(Importer importer) throws IOException, SQLException;

  View view(String name) throws SQLException;

  default View view(String name, Importer importer) throws IOException {
    return importer.read(name, View.class);
  }

  List<View> views() throws SQLException;

  void exportViews(Exporter exporter) throws SQLException, IOException;

  void importViews(Importer importer) throws IOException, SQLException;

  void exportAll(Exporter exporter) throws SQLException, IOException;

  void importAll(Importer importer) throws IOException, SQLException;

}
