package com.rm2pt.generator.golangclassgenerate;

@SuppressWarnings("all")
public class ZSingleAss {
  public String originName;
  
  public ZName field;
  
  public ZName targetEntity;
  
  public ZSingleAss(final String originName, final String targetEntityName) {
    this.originName = originName;
    ZName _zName = new ZName((originName + "GoenId"));
    this.field = _zName;
    ZName _zName_1 = new ZName(targetEntityName);
    this.targetEntity = _zName_1;
  }
}
