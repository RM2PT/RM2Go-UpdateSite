package com.rm2pt.generator.golangclassgenerate;

@SuppressWarnings("all")
public class ZMultiAss {
  public String originName;
  
  public String tableName;
  
  public ZName targetEntity;
  
  public ZMultiAss(final String originName, final String ownerEntityName, final String targetEntityName) {
    this.originName = originName;
    String _camelToUnderScore = Tool.camelToUnderScore(ownerEntityName);
    String _plus = (_camelToUnderScore + "_");
    String _camelToUnderScore_1 = Tool.camelToUnderScore(originName);
    String _plus_1 = (_plus + _camelToUnderScore_1);
    this.tableName = _plus_1;
    ZName _zName = new ZName(targetEntityName);
    this.targetEntity = _zName;
  }
}
