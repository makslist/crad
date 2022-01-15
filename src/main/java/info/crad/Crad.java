package info.crad;

import info.crad.dbobjects.*;
import info.crad.io.*;
import info.crad.product.oracle.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Crad {

  private static final String ORACLE_URL = "jdbc:oracle:thin:@ifbdev43DB:1521:dagdev";

  private static final String BASE_FOLDER = "D:" + File.separator + "crad" + File.separator;

//  Connection H2_CONNECTION = Connection conn = Crad.connect("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1", "sa", "sa")
//  Connection HSQLDB2_CONNECTION = Connection conn = Crad.connect("jdbc:hsqldb:mem:myDb;sql.syntax_ora=true", "sa", "sa")

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();

//    try (Connection conn = connect("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1", "sa", "sa")) {
//    try (Connection conn = connect("jdbc:hsqldb:mem:myDb", "sa", "sa")) {
    try (Connection conn = connect(ORACLE_URL, "helas", "helas")) {

      Product product = DbmsProduct.get(conn);
      if (Arrays.asList(args).contains("export")) {
        System.out.println("Exporting DB objects");
        product.exportAll(new JsonExporter(BASE_FOLDER));
      } else if (Arrays.asList(args).contains("gensql")) {
        System.out.println("Generate SQL");
        product.exportAll(new DDLExporter(BASE_FOLDER));
      } else if (Arrays.asList(args).contains("import")) {
        System.out.println("Creating DB objects");
        product.importAll(new JsonImporter(BASE_FOLDER));
      } else if (Arrays.asList(args).contains("diff")) {
        System.out.println("Diffing DB objects");
//        conn.setClientInfo("OCSID.MODULE", "diff");
        Table tableRef = product.table("amp_container", new JsonImporter(BASE_FOLDER));
//        System.out.println(tableRef.createStatement());

        OracleTable table = (OracleTable) product.table(tableRef.name());
        if (table.isEqual(tableRef))
          System.out.println("Tables are equal.");
        else
          System.out.println("Tables are different.");
//        System.out.println(tableRef.diff(table));
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
      conn.setAutoCommit(false);
      System.out.println("Connected to " + conn.getMetaData().getDatabaseProductName() + " @ " + url);
      System.out.println("Schema: " + conn.getSchema());
    } else {
      System.out.println("Failed to make connection!");
      System.exit(1);
    }
    return conn;
  }

}
