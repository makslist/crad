package info.crad.io;

import com.fasterxml.jackson.databind.*;
import info.crad.dbobjects.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonImporter implements Importer {

  private final File baseDir;
  private final ObjectMapper mapper = new ObjectMapper();

  public JsonImporter(String baseFolder) throws NoSuchFileException {
    baseDir = new File(baseFolder);
    if (!baseDir.isDirectory())
      throw new NoSuchFileException("Directory " + baseDir + " does not exist!");
  }

  @Override
  public <T extends DbObject> T read(String name, Class<T> objClass) throws IOException {
    File objDir = new File(baseDir, objClass.getSimpleName().toLowerCase());
    if (objDir.isDirectory()) {
      ObjectReader reader = mapper.readerFor(objClass);
      File file = new File(objDir, name + ".json");
      if (file.exists())
        return reader.readValue(file);
      else
        throw new NoSuchFileException("File " + file + " does not exist!");
    } else
      throw new NoSuchFileException("Directory " + objDir + " does not exist!");
  }

  @Override
  public List<? extends DbObject> read(Class<?> objClass) throws IOException {
    File objDir = new File(baseDir, objClass.getSimpleName().toLowerCase());
    if (objDir.isDirectory()) {
      ObjectReader reader = mapper.readerFor(objClass);
      List<DbObject> dbObjects = new ArrayList<>();
      File[] files = objDir.listFiles((dir, name) -> name.endsWith(".json"));
      assert files != null;
      for (File file : files)
        dbObjects.add(reader.readValue(file));
      return dbObjects;
    } else
      return new ArrayList<>();
  }

}
