package com.rm2pt.generator.golangclassgenerate;

import java.util.ArrayList;
import java.util.List;
import net.mydreamy.requirementmodel.rEMODEL.EnumEntity;
import net.mydreamy.requirementmodel.rEMODEL.EnumItem;
import org.eclipse.emf.common.util.EList;

@SuppressWarnings("all")
public class ZEnumType {
  public String goName;
  
  public List<String> memberName = new ArrayList<String>();
  
  public ZEnumType(final EnumEntity enumType) {
    this.goName = enumType.getName();
    EList<EnumItem> _element = enumType.getElement();
    for (final EnumItem e : _element) {
      String _name = enumType.getName();
      String _name_1 = e.getName();
      String _plus = (_name + _name_1);
      this.memberName.add(_plus);
    }
  }
}
