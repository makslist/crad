package info.crad.io;

import info.crad.dbobjects.*;

import java.io.*;

public interface Exporter {

  void write(DbObject object) throws IOException;

}
