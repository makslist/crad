package info.crad.product.h2;

import info.crad.dbobjects.*;
import info.crad.io.*;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * https://www.h2database.com/html/commands.html
 */
public class H2 implements Product {

  private final Connection conn;

  public H2(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void exportAll(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  public void importAll(Importer importer) throws IOException {
    importTables(importer);
    importViews(importer);
    importSequences(importer);
    importTriggers(importer);
  }

  @Override
  public Table table(String name) {
    return null;//TODO
  }

  @Override
  public List<Table> tables() {
    return null;//TODO
  }

  @Override
  public void exportTables(Exporter exporter) {
    //TODO
  }

  @Override
  public void importTables(Importer importer) throws IOException {
    for (DbObject obj : importer.read(Table.class))
      H2Table.create(conn, (Table) obj);
  }

  @Override
  public DbPackage dbPackage(String name) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<DbPackage> dbPackages() {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void exportPackages(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importPackages(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public View view(String name) {
    return null; //TODO
  }

  @Override
  public List<View> views() {
    return null; //TODO
  }

  @Override
  public void exportViews(Exporter exporter) {
    //TODO
  }

  @Override
  public void importViews(Importer importer) throws IOException {
    for (DbObject obj : importer.read(View.class))
      H2View.create(conn, (View) obj);
  }

  @Override
  public Synonym synonym(String name) {
    return null; //TODO
  }

  @Override
  public List<Synonym> synonyms() {
    return null; //TODO
  }

  @Override
  public void exportSynonyms(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importSynonyms(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Function function(String name) throws SQLException {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Function> functions() {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void exportFunctions(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importFunctions(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Procedure procedure(String name) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Procedure> procedures() {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void exportProcedures(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importProcedures(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public JavaSource javaSource(String name) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<JavaSource> javaSources() {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void exportJavaSources(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importJavaSources(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Library library(String name) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Library> libraries() {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void exportLibraries(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importLibraries(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Sequence sequence(String name) {
    return null; //TODO
  }

  @Override
  public List<Sequence> sequences() {
    return null; //TODO
  }

  @Override
  public void exportSequences(Exporter exporter) {
    //TODO
  }

  @Override
  public void importSequences(Importer importer) throws IOException {
    for (DbObject obj : importer.read(Sequence.class))
      H2Sequence.create(conn, (Sequence) obj);
  }

  @Override
  public Trigger trigger(String name) {
    return null; //TODO
  }

  @Override
  public List<Trigger> triggers() {
    return null; //TODO
  }

  @Override
  public void exportTriggers(Exporter exporter) {
    //TODO
  }

  @Override
  public void importTriggers(Importer importer) throws IOException {
    for (DbObject obj : importer.read(Trigger.class))
      H2Trigger.create(conn, (Trigger) obj);
  }

  @Override
  public Type type(String name) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Type> types() {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void exportTypes(Exporter exporter) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void importTypes(Importer importer) {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

}
