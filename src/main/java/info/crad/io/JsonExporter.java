package info.crad.io;

import com.fasterxml.jackson.core.util.*;
import com.fasterxml.jackson.databind.*;
import info.crad.dbobjects.*;

import java.io.*;

public class JsonExporter implements Exporter {

  private static final String fileExtension = ".json";

  private final ObjectWriter jsonWriter;
  private final String baseFolder;

  public JsonExporter(String baseFolder) {
    this.baseFolder = baseFolder;
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    jsonWriter = mapper.writer(prettyPrinter);
  }

  @Override
  public void write(DbObject object) throws IOException {
    File basePath = new File(baseFolder);
    if (basePath.isDirectory() || basePath.mkdir()) {
      File path = new File(basePath, object.getClass().getSuperclass().getSimpleName().toLowerCase());
      if (path.isDirectory() || path.mkdir()) {
        System.out.println("Writing: " + object.name().toLowerCase());
        jsonWriter.writeValue(new File(path, object.name().toLowerCase() + fileExtension), object);
      }
    }
  }

}
