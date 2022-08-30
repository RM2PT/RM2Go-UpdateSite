package com.rm2pt.generator.golangclassgenerate;

import net.mydreamy.requirementmodel.rEMODEL.EnumEntity;

@SuppressWarnings("all")
public class ZEnumField {
  public ZName member;
  
  public ZEnumType type;
  
  public ZEnumField(final String attrName, final EnumEntity enumType) {
    ZName _zName = new ZName(attrName);
    this.member = _zName;
    ZEnumType _zEnumType = new ZEnumType(enumType);
    this.type = _zEnumType;
  }
}
