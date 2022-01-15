package info.crad.io;

import info.crad.dbobjects.*;

import java.io.*;
import java.util.*;

public interface Importer {

  <T extends DbObject> T read(String name, Class<T> objClass) throws IOException;

  List<? extends DbObject> read(Class<?> objClass) throws IOException;

}
