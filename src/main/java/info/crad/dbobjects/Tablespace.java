package info.crad.dbobjects;

public class Tablespace {

  protected String tablespaceName;
  protected String pctFree;
  protected long iniTrans;
  protected long maxTrans;
  protected long initialExtent;
  protected long nextExtent;
  protected long minExtents;
  protected long maxExtents;
  public Tablespace() {
  }

  public Tablespace(String tablespaceName, String pctFree, long iniTrans, long maxTrans, long initialExtent, long nextExtent, long minExtents, long maxExtents) {
    this.tablespaceName = tablespaceName;
    this.pctFree = pctFree;
    this.iniTrans = iniTrans;
    this.maxTrans = maxTrans;
    this.initialExtent = initialExtent;
    this.nextExtent = nextExtent;
    this.minExtents = minExtents;
    this.maxExtents = maxExtents;
  }

  protected static String iec80000_13(long number) {
    if (number / (1 << 30) >= 1 && (number % (1 << 30)) == 0)
      return (number / (1 << 30)) + "G";
    if (number / (1 << 20) >= 1 && (number % (1 << 20)) == 0)
      return (number / (1 << 20)) + "M";
    if (number / (1 << 10) >= 1 && (number % (1 << 10)) == 0)
      return (number / (1 << 10)) + "K";
    return String.valueOf(number);
  }

  public String getTablespaceName() {
    return tablespaceName;
  }

  public String getPctFree() {
    return pctFree;
  }

  public long getIniTrans() {
    return iniTrans;
  }

  public long getMaxTrans() {
    return maxTrans;
  }

  public long getInitialExtent() {
    return initialExtent;
  }

  public long getNextExtent() {
    return nextExtent;
  }

  public long getMinExtents() {
    return minExtents;
  }

  public long getMaxExtents() {
    return maxExtents;
  }

  public String create() {
    return "tablespace " + tablespaceName + "\n" +
        "  pctfree " + pctFree + "\n" +
        "  initrans " + iniTrans + "\n" +
        "  maxtrans " + maxTrans + "\n" +
        "  storage\n" + "  (\n" +
        "    initial " + initial() + "\n" +
        "    next " + next() + "\n" +
        "    minextents " + minExtents + "\n" +
        "    maxextents " + (maxExtents >= Integer.MAX_VALUE - 2 ? "unlimited" : maxExtents) + "\n" +
        "  )";
  }

  public String initial() {
    return Tablespace.iec80000_13(initialExtent);
  }

  public String next() {
    return Tablespace.iec80000_13(nextExtent);
  }

}

