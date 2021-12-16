package info.crad.dbobjects;

public interface TableSpace {

  static String iec80000_13(long number) {
    if (number / (1 << 30) >= 1 && (number % (1 << 30)) == 0)
      return (number / (1 << 30)) + "G";
    if (number / (1 << 20) >= 1 && (number % (1 << 20)) == 0)
      return (number / (1 << 20)) + "M";
    if (number / (1 << 10) >= 1 && (number % (1 << 10)) == 0)
      return (number / (1 << 10)) + "K";
    return String.valueOf(number);
  }

  String tablespace();

  String initial();

  String next();

}
