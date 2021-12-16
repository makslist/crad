package info.crad.dbobjects;

import java.util.*;

public interface Source {

  List<String> code();

  default boolean isEqual(Source s) {
    return Arrays.mismatch(code().toArray(), s.code().toArray()) == -1L;
  }

}
