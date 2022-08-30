package com.rm2pt.generator.golangclassgenerate;

@SuppressWarnings("all")
public class ZBasicField {
  public ZName member;
  
  public ZBasicType type;
  
  public ZBasicField(final String attrName, final String typeName) {
    ZName _zName = new ZName(attrName);
    this.member = _zName;
    ZBasicType _zBasicType = new ZBasicType(typeName);
    this.type = _zBasicType;
  }
}
