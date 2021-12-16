package info.crad.dbobjects;

public interface DbObject {

  String name();

  String createStatement();

  String type();

  String typeShort();

}
