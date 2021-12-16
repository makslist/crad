package info.crad;

import com.fasterxml.jackson.core.util.*;
import com.fasterxml.jackson.databind.*;
import info.crad.dbobjects.*;
import info.crad.dbobjects.oracle.*;
import info.crad.export.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Crad {

  private static final String ORACLE_URL = "jdbc:oracle:thin:@ifbdev43DB:1521:dagdev";

  private static final String BASE_FOLDER = "D:" + File.separator + "crad_test" + File.separator;

//  Connection H2_CONNECTION = Connection conn = Crad.connect("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1", "sa", "sa")
//  Connection HSQLDB2_CONNECTION = Connection conn = Crad.connect("jdbc:hsqldb:mem:myDb;sql.syntax_ora=true", "sa", "sa")

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();

    try (Connection conn = connect("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1", "sa", "sa")) {
//    try (Connection conn = connect("jdbc:hsqldb:mem:myDb;sql.syntax_ora=true", "sa", "sa")){
//    try (Connection conn = connect(ORACLE_URL, "helas", "helas")) {

      Product product = DbmsProduct.get(conn);
      if (Arrays.asList(args).contains("snapshot")) {
        System.out.println("Exporting DB objects");

        JsonExporter exporter = new JsonExporter(BASE_FOLDER);
        product.types(conn, exporter);
        product.tables(conn, exporter);
        product.sequences(conn, exporter);

        product.synonyms(conn, exporter);

        product.libraries(conn, exporter);
        product.functions(conn, exporter);
        product.procedures(conn, exporter);
        product.javaSources(conn, exporter);

        product.packages(conn, exporter);
        product.triggers(conn, exporter);

        product.views(conn, exporter);
      } else if (Arrays.asList(args).contains("gensql")) {
        System.out.println("Generate SQL");
        DDLExporter ddl = new DDLExporter(BASE_FOLDER);
        ddl.write(product.dbPackage(conn, "LOGGER"));
        ddl.write(product.view(conn, "V_AMP_PBP_AUF_AEN"));
        ddl.write(product.table(conn, "AMP_CONTAINER"));
        ddl.write(product.synonym(conn, "SAP_EDIDCI"));
        ddl.write(product.function(conn, "BOOL2CHAR"));
        ddl.write(product.procedure(conn, "ASSERT"));
        ddl.write(product.javaSource(conn, "DirList"));
        ddl.write(product.library(conn, "HOSTCMD"));
        ddl.write(product.sequence(conn, "SEQ_AMP_ANL_ID_OLT_INTERN"));
        ddl.write(product.trigger(conn, "AMP_CONTAINER_PBP_WA_POOLS_BEFORE"));
        ddl.write(product.type(conn, "C_DCP_PHM_REC"));
      } else if (Arrays.asList(args).contains("import")) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try (Statement stmt = conn.createStatement()) {
          File baseDir = new File(BASE_FOLDER, "json");
          baseDir = new File(baseDir, "sequence");
          if (baseDir.isDirectory()) {
            ObjectReader reader = mapper.readerFor(Sequence.class);
            File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null)
              for (File file : files) {
                Sequence table = reader.readValue(file);
                try {
                  stmt.execute(table.createStatement());
                } catch (SQLException e) {
                  System.out.format("SQL State: %s: %s\n", e.getSQLState(), e.getMessage());
                }
              }
          }
        }
      } else if (Arrays.asList(args).contains("diff")) {

        String SQL_ALL_OBJECTS = "SELECT OBJECT_NAME, OBJECT_TYPE FROM USER_OBJECTS"
            + " WHERE OBJECT_TYPE NOT IN ('PACKAGE BODY', 'DATABASE LINK', 'LOB', 'INDEX')"
            + "   AND OBJECT_NAME NOT LIKE 'ZZ_%'" + "   AND OBJECT_NAME NOT LIKE 'V_SST_%'"
            + "   AND OBJECT_NAME NOT LIKE 'V_HIST_%'";
        Set<String> dbObjects = new HashSet<>();
        try (PreparedStatement stmt = conn.prepareStatement(SQL_ALL_OBJECTS)) {
          stmt.setFetchSize(1000);
          ResultSet resultSet = stmt.executeQuery();
          while (resultSet.next())
            dbObjects.add(resultSet.getString("OBJECT_NAME") + "_" + resultSet.getString("OBJECT_TYPE"));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        final ObjectWriter jsonWriter = mapper.writer(prettyPrinter);
        jsonWriter.writeValue(new File(BASE_FOLDER + "cached_objects" + ".json"), dbObjects);

        // Load reference file
        final File file = new File(BASE_FOLDER + File.separator + "amp_container".toLowerCase() + ".json");
        Table tableRef = mapper.readerFor(OracleTable.class).readValue(file);
        System.out.println(tableRef.createStatement());

        Table table = product.table(conn, "AMP_CONTAINER");
        System.out.println(tableRef.diff(table));
      }

      System.out.println("Operation took: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    } catch (SQLException e) {
      System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Connection connect(String url, String user, String password) throws SQLException {
    Connection conn = DriverManager.getConnection(url, user, password);
    if (conn != null) {
//      conn.setClientInfo("ApplicationName", Crad.class.getSimpleName());
      System.out.println("Connected to " + conn.getMetaData().getDatabaseProductName() + " @ " + url);
      System.out.println("Schema: " + conn.getSchema());
    } else {
      System.out.println("Failed to make connection!");
      System.exit(1);
    }
    return conn;
  }

}
