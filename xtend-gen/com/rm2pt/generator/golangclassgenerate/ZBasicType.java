package com.rm2pt.generator.golangclassgenerate;

@SuppressWarnings("all")
public class ZBasicType {
  public String goName;
  
  public String sqlName;
  
  public String sqlConstraint;
  
  public String goImport;
  
  public ZBasicType(final String type) {
    this.goName = Tool.compileGoTypeName(type);
    this.goImport = Tool.compileGoImport(type);
    this.sqlName = Tool.compileSqlTypeName(type);
    this.sqlConstraint = Tool.compileSqlBasicConstraint(type);
  }
}
