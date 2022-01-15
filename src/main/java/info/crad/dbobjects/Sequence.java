package info.crad.dbobjects;

import com.fasterxml.jackson.annotation.*;

import java.math.*;

public class Sequence implements DbObject {

  protected String name;
  protected BigDecimal minValue;
  protected BigDecimal maxValue;
  protected long incrementBy;
  protected String cycleFlag;
  protected String orderFlag;
  protected long cacheSize;
  protected BigDecimal lastNumber;

  @JsonProperty("name")
  @Override
  public String name() {
    return name;
  }

  public BigDecimal getMinValue() {
    return minValue;
  }

  public BigDecimal getMaxValue() {
    return maxValue;
  }

  public long getIncrementBy() {
    return incrementBy;
  }

  public String getCycleFlag() {
    return cycleFlag;
  }

  public String getOrderFlag() {
    return orderFlag;
  }

  public long getCacheSize() {
    return cacheSize;
  }

  public BigDecimal getLastNumber() {
    return lastNumber;
  }

  @Override
  public String createStatement() {
    return "create sequence " + name() + "\n" +
        (minValue.equals(BigDecimal.ZERO) ? "nonminvalue" : ("minvalue " + minValue)) + "\n" +
        (maxValue.equals(BigDecimal.ZERO) ? "nonmaxvalue" : ("maxvalue " + maxValue)) + "\n" +
        "start with " + lastNumber + "\n" +
        "increment by " + incrementBy + "\n" +
        (cacheSize != 0 ? ("cache " + cacheSize) : "nocache") + "\n" +
        (cycleFlag.equals("Y") ? "cycle" : "nocycle") + "\n";
//   TODO ANSI SQL? +
//        (orderFlag.equals("Y") ? "order" : "noorder") + "\n";
  }

  @Override
  public String typeShort() {
    return "seq";
  }

}
