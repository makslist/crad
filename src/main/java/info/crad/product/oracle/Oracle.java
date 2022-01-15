package info.crad.product.oracle;

import info.crad.*;
import info.crad.dbobjects.*;
import info.crad.io.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Oracle implements Product {

  public static final String OCSID_CLIENTID = "OCSID.CLIENTID";
  public static final String OCSID_MODULE = "OCSID.MODULE";
  public static final String OCSID_ACTION = "OCSID.ACTION";
  private final Connection conn;

  public Oracle(Connection conn) throws SQLClientInfoException {
    this.conn = conn;
    this.conn.setClientInfo(OCSID_CLIENTID, Crad.class.getSimpleName());
  }

  public String toString() {
    return Oracle.class.getSimpleName();
  }

  public void exportAll(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_MODULE, "export");

    exportFunctions(exporter);
    exportJavaSources(exporter);
    exportLibraries(exporter);
    exportPackages(exporter);
    exportProcedures(exporter);
    exportSequences(exporter);
    exportSynonyms(exporter);
    exportTables(exporter);
    exportTriggers(exporter);
    exportTypes(exporter);
    exportViews(exporter);
  }

  @Override
  public void importAll(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_MODULE, "import");

    importFunctions(importer);
    importJavaSources(importer);
    importLibraries(importer);
    importPackages(importer);
    importProcedures(importer);
    importSequences(importer);
    importSynonyms(importer);
    importTables(importer);
    importTriggers(importer);
    importTypes(importer);
    importViews(importer);
  }

  @Override
  public Function function(String functionName) throws SQLException {
    return OracleFunction.get(conn, functionName);
  }

  @Override
  public List<Function> functions() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "function");
    List<Function> functions = new ArrayList<>();
    for (String function : OracleFunction.allFunctions(conn))
      functions.add(OracleFunction.get(conn, function));
    return functions;
  }

  @Override
  public void exportFunctions(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "functions");
    for (String function : OracleFunction.allFunctions(conn))
      exporter.write(OracleFunction.get(conn, function));
  }

  @Override
  public void importFunctions(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "functions");
    for (DbObject obj : importer.read(Function.class))
      OracleFunction.create(conn, (Function) obj);
  }

  @Override
  public JavaSource javaSource(String javaSourceName) throws SQLException {
    return OracleJavaSource.get(conn, javaSourceName);
  }

  @Override
  public List<JavaSource> javaSources() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "javaSources");
    List<JavaSource> javaSources = new ArrayList<>();
    for (String javaSource : OracleJavaSource.allJavaSources(conn))
      javaSources.add(OracleJavaSource.get(conn, javaSource));
    return javaSources;
  }

  @Override
  public void exportJavaSources(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "javaSources");
    for (String javaSource : OracleJavaSource.allJavaSources(conn))
      exporter.write(OracleJavaSource.get(conn, javaSource));
  }

  @Override
  public void importJavaSources(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "javaSources");
    for (DbObject obj : importer.read(JavaSource.class))
      OracleJavaSource.create(conn, (JavaSource) obj);
  }

  @Override
  public Library library(String libraryName) throws SQLException {
    return OracleLibrary.get(conn, libraryName);
  }

  @Override
  public List<Library> libraries() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "libraries");
    List<Library> libraries = new ArrayList<>();
    for (String library : OracleLibrary.allLibraries(conn))
      libraries.add(OracleLibrary.get(conn, library));
    return libraries;
  }

  @Override
  public void exportLibraries(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "libraries");
    for (String library : OracleLibrary.allLibraries(conn))
      exporter.write(OracleLibrary.get(conn, library));
  }

  @Override
  public void importLibraries(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "libraries");
    for (DbObject obj : importer.read(Library.class))
      OracleLibrary.create(conn, (Library) obj);
  }

  @Override
  public DbPackage dbPackage(String packageName) throws SQLException {
    return OraclePackage.get(conn, packageName);
  }

  @Override
  public List<DbPackage> dbPackages() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "dbPackages");
    List<DbPackage> packages = new ArrayList<>();
    for (String dbPackage : OraclePackage.allDbPackages(conn))
      packages.add(OraclePackage.get(conn, dbPackage));
    return packages;
  }

  @Override
  public void exportPackages(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "dbPackages");
    for (String dbPackage : OraclePackage.allDbPackages(conn))
      exporter.write(OraclePackage.get(conn, dbPackage));
  }

  @Override
  public void importPackages(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "dbPackages");
    for (DbObject obj : importer.read(DbPackage.class))
      OraclePackage.create(conn, (DbPackage) obj);
  }

  @Override
  public Procedure procedure(String procedureName) throws SQLException {
    return OracleProcedure.get(conn, procedureName);
  }

  @Override
  public List<Procedure> procedures() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "procedures");
    List<Procedure> procedures = new ArrayList<>();
    for (String procedure : OracleProcedure.allProcedures(conn))
      procedures.add(OracleProcedure.get(conn, procedure));
    return procedures;
  }

  @Override
  public void exportProcedures(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "procedures");
    for (String procedure : OracleProcedure.allProcedures(conn))
      exporter.write(OracleProcedure.get(conn, procedure));
  }

  @Override
  public void importProcedures(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "procedures");
    for (DbObject obj : importer.read(Procedure.class))
      OracleProcedure.create(conn, (Procedure) obj);
  }

  @Override
  public Sequence sequence(String sequenceName) throws SQLException {
    return OracleSequence.get(conn, sequenceName);
  }

  @Override
  public List<Sequence> sequences() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "sequences");
    List<Sequence> sequences = new ArrayList<>();
    for (String sequence : OracleSequence.allSequences(conn))
      sequences.add(OracleSequence.get(conn, sequence));
    return sequences;
  }

  @Override
  public void exportSequences(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "sequences");
    for (String sequence : OracleSequence.allSequences(conn))
      exporter.write(OracleSequence.get(conn, sequence));
  }

  @Override
  public void importSequences(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "sequences");
    for (DbObject obj : importer.read(Sequence.class))
      OracleSequence.create(conn, (Sequence) obj);
  }

  @Override
  public Synonym synonym(String synonymName) throws SQLException {
    return OracleSynonym.get(conn, synonymName);
  }

  @Override
  public List<Synonym> synonyms() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "synonyms");
    List<Synonym> synonyms = new ArrayList<>();
    for (String synonym : OracleSynonym.allSynonyms(conn))
      synonyms.add(OracleSynonym.get(conn, synonym));
    return synonyms;
  }

  @Override
  public void exportSynonyms(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "synonyms");
    for (String synonym : OracleSynonym.allSynonyms(conn))
      exporter.write(OracleSynonym.get(conn, synonym));
  }

  @Override
  public void importSynonyms(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "synonyms");
    for (DbObject obj : importer.read(Synonym.class))
      OracleSynonym.create(conn, (Synonym) obj);
  }

  @Override
  public Table table(String tableName) throws SQLException {
    return OracleTable.get(conn, tableName);
  }

  @Override
  public List<Table> tables() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "tables");
    try (PreparedStatement stmt = conn.prepareStatement(OracleTable.ALL_SQL)) {
      List<Table> tables = new ArrayList<>();
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        tables.add(table(resultSet.getString("TABLE_NAME")));
      return tables;
    }
  }

  @Override
  public void exportTables(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "tables");
    try (PreparedStatement stmt = conn.prepareStatement(OracleTable.ALL_SQL)) {
      stmt.setFetchSize(1000);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next())
        exporter.write(table(resultSet.getString("TABLE_NAME")));
    }
  }

  @Override
  public void importTables(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "tables");
    for (DbObject obj : importer.read(Table.class))
      OracleTable.create(conn, (Table) obj);
  }

  @Override
  public Trigger trigger(String name) throws SQLException {
    return OracleTrigger.get(conn, name);
  }

  @Override
  public List<Trigger> triggers() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "triggers");
    List<Trigger> triggers = new ArrayList<>();
    for (String trigger : OracleTrigger.allTriggers(conn))
      triggers.add(OracleTrigger.get(conn, trigger));
    return triggers;
  }

  @Override
  public void exportTriggers(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "triggers");
    for (String trigger : OracleTrigger.allTriggers(conn))
      exporter.write(OracleTrigger.get(conn, trigger));
  }

  @Override
  public void importTriggers(Importer importer) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "triggers");
    for (DbObject obj : importer.read(Trigger.class))
      OracleTrigger.create(conn, (Trigger) obj);
  }

  @Override
  public Type type(String name) throws SQLException {
    return OracleType.get(conn, name);
  }

  @Override
  public List<Type> types() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "types");
    List<Type> types = new ArrayList<>();
    for (String type : OracleType.allTypes(conn))
      types.add(OracleType.get(conn, type));
    return types;
  }

  @Override
  public void exportTypes(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "types");
    for (String type : OracleType.allTypes(conn))
      exporter.write(OracleType.get(conn, type));
  }

  @Override
  public void importTypes(Importer importer) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "types");
    for (DbObject obj : importer.read(Type.class))
      OracleType.create(conn, (Type) obj);
  }

  @Override
  public View view(String name) throws SQLException {
    return OracleView.get(conn, name);
  }

  @Override
  public List<View> views() throws SQLException {
    conn.setClientInfo(OCSID_ACTION, "views");
    List<View> views = new ArrayList<>();
    for (String viewName : OracleView.allViews(conn))
      views.add(OracleView.get(conn, viewName));
    return views;
  }

  @Override
  public void exportViews(Exporter exporter) throws SQLException, IOException {
    conn.setClientInfo(OCSID_ACTION, "views");
    for (String viewName : OracleView.allViews(conn))
      exporter.write(OracleView.get(conn, viewName));
  }

  public void importViews(Importer importer) throws IOException, SQLException {
    conn.setClientInfo(OCSID_ACTION, "views");
    for (DbObject obj : importer.read(View.class))
      OracleView.create(conn, (View) obj);
  }

}
