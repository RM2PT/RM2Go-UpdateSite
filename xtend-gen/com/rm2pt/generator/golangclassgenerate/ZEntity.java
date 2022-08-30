package com.rm2pt.generator.golangclassgenerate;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ZEntity {
  public ZName entityName;
  
  public List<ZBasicField> basicFields = new ArrayList<ZBasicField>();
  
  public List<ZEnumField> enumFields = new ArrayList<ZEnumField>();
  
  public List<ZSingleAss> singleAsses = new ArrayList<ZSingleAss>();
  
  public List<ZMultiAss> multiAsses = new ArrayList<ZMultiAss>();
  
  public boolean isBaseEntity = false;
  
  public ZName parentEntity;
}
