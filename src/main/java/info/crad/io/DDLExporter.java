package info.crad.io;

import info.crad.dbobjects.*;

import java.io.*;

public class DDLExporter implements Exporter {

  private final String baseFolder;

  public DDLExporter(String baseFolder) {
    this.baseFolder = baseFolder;
  }

  @Override
  public void write(DbObject object) throws IOException {
    File basePath = new File(baseFolder, "ddl");
    if (basePath.isDirectory() || basePath.mkdir()) {
      File path = new File(basePath, object.getClass().getSimpleName().toLowerCase());
      if (path.isDirectory() || path.mkdir()) {
        try (BufferedWriter bw = new BufferedWriter(
            new FileWriter(new File(path, object.name().toLowerCase() + "." + object.typeShort())))) {
          bw.write(object.createStatement());
        }
      }
    }
  }

}
