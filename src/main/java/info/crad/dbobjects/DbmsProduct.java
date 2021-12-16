package info.crad.dbobjects;

import info.crad.dbobjects.h2.*;
import info.crad.dbobjects.hsql.*;
import info.crad.dbobjects.oracle.*;

import java.sql.*;

public enum DbmsProduct {

  Oracle, H2, HSQL;

  public static Product get(Connection conn) throws SQLException {
    String productName = conn.getMetaData().getDatabaseProductName();
    switch (productName) {
      case "Oracle":
        return new Oracle();
      case "H2":
        return new H2();
      case "HSQL Database Engine":
        return new HSQL();
      default:
        throw new DbProductNotSupported("DB product not supported: " + productName);
    }
  }

  public static class DbProductNotSupported extends SQLException {

    public DbProductNotSupported(String message) {
      super(message);
    }

  }
}
