package com.rm2pt.generator.golangclassgenerate;

@SuppressWarnings("all")
public class ZName {
  public String originName;
  
  public String underline;
  
  public String initialLow;
  
  public ZName(final String originName) {
    this.originName = originName;
    this.underline = Tool.camelToUnderScore(originName);
    this.initialLow = Tool.initialLowerCase(originName);
  }
}
