package com.rm2pt.generator.golangclassgenerate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class EntityGenerator {
  public static class Func {
    public String declare;
    
    public String body;
  }
  
  private static ArrayList<String> getInheritConsts(final Collection<ZEntity> entities) {
    ArrayList<String> strList = new ArrayList<String>();
    for (final ZEntity e : entities) {
      if ((e.parentEntity != null)) {
        strList.add((e.entityName.originName + "InheritType"));
      }
    }
    return strList;
  }
  
  public static String generateInit(final Collection<ZEntity> entities) {
    ArrayList<String> inheritConsts = EntityGenerator.getInheritConsts(entities);
    HashMap<String, ZEntity> entityMap = new HashMap<String, ZEntity>();
    for (final ZEntity e : entities) {
      entityMap.put(e.entityName.originName, e);
    }
    LinkedHashSet<ZEntity> sortedEntities = new LinkedHashSet<ZEntity>();
    for (final ZEntity e_1 : entities) {
      {
        ZEntity nowe = e_1;
        LinkedList<ZEntity> willAdd = new LinkedList<ZEntity>();
        willAdd.addFirst(nowe);
        while ((nowe.parentEntity != null)) {
          {
            nowe = entityMap.get(nowe.parentEntity.originName);
            willAdd.addFirst(nowe);
          }
        }
        for (final ZEntity eAdd : willAdd) {
          sortedEntities.add(eAdd);
        }
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package entity");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import (");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"Auto/entityManager\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"log\"");
    _builder.newLine();
    _builder.append(")");
    _builder.newLine();
    _builder.newLine();
    {
      final ArrayList<String> _converted_inheritConsts = (ArrayList<String>)inheritConsts;
      int _length = ((Object[])Conversions.unwrapArray(_converted_inheritConsts, Object.class)).length;
      boolean _notEquals = (_length != 0);
      if (_notEquals) {
        _builder.append("const (");
        _builder.newLine();
        _builder.append("\t");
        String _get = inheritConsts.get(0);
        _builder.append(_get, "\t");
        _builder.append(" entityManager.GoenInheritType = iota + 1");
        _builder.newLineIfNotEmpty();
        {
          final ArrayList<String> _converted_inheritConsts_1 = (ArrayList<String>)inheritConsts;
          List<String> _subList = inheritConsts.subList(1, ((Object[])Conversions.unwrapArray(_converted_inheritConsts_1, Object.class)).length);
          for(final String inheritConst : _subList) {
            _builder.append("\t");
            _builder.append(inheritConst, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(")");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("func init() {");
    _builder.newLine();
    {
      for(final ZEntity e_2 : sortedEntities) {
        {
          if ((e_2.parentEntity == null)) {
            _builder.append("\t");
            _builder.append("tmp");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("Manager, err := entityManager.NewManager[");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("Entity, ");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("](\"");
            _builder.append(e_2.entityName.underline, "\t");
            _builder.append("\")\t\t\t");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("tmp");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("Manager, err := entityManager.NewInheritManager[");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("Entity, ");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("](\"");
            _builder.append(e_2.entityName.underline, "\t");
            _builder.append("\", tmp");
            _builder.append(e_2.parentEntity.originName, "\t");
            _builder.append("Manager, ");
            _builder.append(e_2.entityName.originName, "\t");
            _builder.append("InheritType)\t\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("if err != nil {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("log.Fatal(err)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append(e_2.entityName.initialLow, "\t");
        _builder.append("Manager = tmp");
        _builder.append(e_2.entityName.originName, "\t");
        _builder.append("Manager");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append(e_2.entityName.originName, "\t");
        _builder.append("Manager = tmp");
        _builder.append(e_2.entityName.originName, "\t");
        _builder.append("Manager");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static CharSequence generate(final ZEntity entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package entity");
    _builder.newLine();
    _builder.newLine();
    String _generateImport = EntityGenerator.generateImport(entity);
    _builder.append(_generateImport);
    _builder.newLineIfNotEmpty();
    CharSequence _generateManagers = EntityGenerator.generateManagers(entity);
    _builder.append(_generateManagers);
    _builder.newLineIfNotEmpty();
    CharSequence _generateEnum = EntityGenerator.generateEnum(entity);
    _builder.append(_generateEnum);
    _builder.newLineIfNotEmpty();
    CharSequence _generateInterface = EntityGenerator.generateInterface(entity);
    _builder.append(_generateInterface);
    _builder.newLineIfNotEmpty();
    CharSequence _generateStruct = EntityGenerator.generateStruct(entity);
    _builder.append(_generateStruct);
    _builder.newLineIfNotEmpty();
    CharSequence _generateOtherImplements = EntityGenerator.generateOtherImplements(entity);
    _builder.append(_generateOtherImplements);
    _builder.newLineIfNotEmpty();
    CharSequence _generateGetters = EntityGenerator.generateGetters(entity);
    _builder.append(_generateGetters);
    _builder.newLineIfNotEmpty();
    CharSequence _generateSetters = EntityGenerator.generateSetters(entity);
    _builder.append(_generateSetters);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static String generateImport(final ZEntity e) {
    TreeSet<String> imports = new TreeSet<String>();
    for (final ZBasicField a : e.basicFields) {
      if ((a.type.goImport != null)) {
        imports.add(a.type.goImport);
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import(");
    _builder.newLine();
    _builder.append("\"Auto/entityManager\"");
    _builder.newLine();
    {
      for(final String i : imports) {
        _builder.append(i);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(")");
    _builder.newLine();
    _builder.newLine();
    return _builder.toString();
  }
  
  public static CharSequence generateManagers(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("var ");
    _builder.append(e.entityName.initialLow);
    _builder.append("Manager entityManager.ManagerForEntity[");
    _builder.append(e.entityName.originName);
    _builder.append("]");
    _builder.newLineIfNotEmpty();
    {
      if ((e.isBaseEntity || (e.parentEntity != null))) {
        _builder.append("var ");
        _builder.append(e.entityName.originName);
        _builder.append("Manager entityManager.InheritManagerForOther[");
        _builder.append(e.entityName.originName);
        _builder.append("]");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("var ");
        _builder.append(e.entityName.originName);
        _builder.append("Manager entityManager.ManagerForOther[");
        _builder.append(e.entityName.originName);
        _builder.append("]");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence generateEnum(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final ZEnumField a : e.enumFields) {
        _builder.append("type ");
        _builder.append(a.type.goName);
        _builder.append(" int");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("const (");
        _builder.newLine();
        _builder.append("\t");
        String _get = a.type.memberName.get(0);
        _builder.append(_get, "\t");
        _builder.append(" ");
        _builder.append(a.type.goName, "\t");
        _builder.append(" = iota");
        _builder.newLineIfNotEmpty();
        {
          List<String> _subList = a.type.memberName.subList(1, ((Object[])Conversions.unwrapArray(a.type.memberName, Object.class)).length);
          for(final String member : _subList) {
            _builder.append("\t");
            _builder.append(member, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(")");
        _builder.newLine();
      }
    }
    _builder.newLine();
    return _builder;
  }
  
  private static ArrayList<String> generateGetterDeclares(final ZEntity e) {
    ArrayList<String> strList = new ArrayList<String>();
    for (final ZBasicField a : e.basicFields) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Get");
      _builder.append(a.member.originName);
      _builder.append(" () ");
      _builder.append(a.type.goName);
      _builder.append(" ");
      strList.add(_builder.toString());
    }
    for (final ZEnumField a_1 : e.enumFields) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Get");
      _builder_1.append(a_1.member.originName);
      _builder_1.append(" () ");
      _builder_1.append(a_1.type.goName);
      _builder_1.append(" ");
      strList.add(_builder_1.toString());
    }
    for (final ZSingleAss a_2 : e.singleAsses) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("Get");
      _builder_2.append(a_2.originName);
      _builder_2.append(" () ");
      _builder_2.append(a_2.targetEntity.originName);
      _builder_2.append(" ");
      strList.add(_builder_2.toString());
    }
    for (final ZMultiAss a_3 : e.multiAsses) {
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("Get");
      _builder_3.append(a_3.originName);
      _builder_3.append(" () []");
      _builder_3.append(a_3.targetEntity.originName);
      _builder_3.append(" ");
      strList.add(_builder_3.toString());
    }
    return strList;
  }
  
  private static ArrayList<String> generateGetterBodies(final ZEntity e) {
    ArrayList<String> strList = new ArrayList<String>();
    for (final ZBasicField a : e.basicFields) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("return p.");
      _builder.append(a.member.originName);
      _builder.append(" ");
      strList.add(_builder.toString());
    }
    for (final ZEnumField a_1 : e.enumFields) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("return p.");
      _builder_1.append(a_1.member.originName);
      _builder_1.append(" ");
      strList.add(_builder_1.toString());
    }
    for (final ZSingleAss a_2 : e.singleAsses) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("if p.");
      _builder_2.append(a_2.field.originName);
      _builder_2.append(" == nil {");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("\t");
      _builder_2.append("return nil");
      _builder_2.newLine();
      _builder_2.append("} else {");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("ret, _ := ");
      _builder_2.append(a_2.targetEntity.initialLow, "\t");
      _builder_2.append("Manager.Get(*p.");
      _builder_2.append(a_2.field.originName, "\t");
      _builder_2.append(")");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("\t");
      _builder_2.append("return ret");
      _builder_2.newLine();
      _builder_2.append("}");
      strList.add(_builder_2.toString());
    }
    for (final ZMultiAss a_3 : e.multiAsses) {
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("ret, _ := ");
      _builder_3.append(a_3.targetEntity.initialLow);
      _builder_3.append("Manager.FindFromMultiAssTable(\"");
      _builder_3.append(a_3.tableName);
      _builder_3.append("\", p.GoenId)");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("return ret ");
      strList.add(_builder_3.toString());
    }
    return strList;
  }
  
  private static ArrayList<String> generateSetterBodies(final ZEntity e) {
    ArrayList<String> strList = new ArrayList<String>();
    for (final ZBasicField a : e.basicFields) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("p.");
      _builder.append(a.member.originName);
      _builder.append(" = ");
      _builder.append(a.member.initialLow);
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("p.AddBasicFieldChange(\"");
      _builder.append(a.member.underline);
      _builder.append("\")");
      strList.add(_builder.toString());
    }
    for (final ZEnumField a_1 : e.enumFields) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("p.");
      _builder_1.append(a_1.member.originName);
      _builder_1.append(" = ");
      _builder_1.append(a_1.member.initialLow);
      _builder_1.append(" ");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("p.AddBasicFieldChange(\"");
      _builder_1.append(a_1.member.underline);
      _builder_1.append("\")");
      strList.add(_builder_1.toString());
    }
    for (final ZSingleAss a_2 : e.singleAsses) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("id := ");
      _builder_2.append(a_2.targetEntity.initialLow);
      _builder_2.append("Manager.GetGoenId(");
      _builder_2.append(a_2.targetEntity.initialLow);
      _builder_2.append(")");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("p.");
      _builder_2.append(a_2.field.originName);
      _builder_2.append(" = &id");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("p.AddAssFieldChange(\"");
      _builder_2.append(a_2.field.underline);
      _builder_2.append("\")");
      strList.add(_builder_2.toString());
    }
    for (final ZMultiAss a_3 : e.multiAsses) {
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("p.AddMultiAssChange(entityManager.Include, \"");
      _builder_3.append(a_3.tableName);
      _builder_3.append("\", ");
      _builder_3.append(a_3.targetEntity.initialLow);
      _builder_3.append("Manager.GetGoenId(");
      _builder_3.append(a_3.targetEntity.initialLow);
      _builder_3.append("))");
      strList.add(_builder_3.toString());
    }
    return strList;
  }
  
  private static ArrayList<String> generateSetterDeclares(final ZEntity e) {
    ArrayList<String> strList = new ArrayList<String>();
    for (final ZBasicField a : e.basicFields) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Set");
      _builder.append(a.member.originName);
      _builder.append(" (");
      _builder.append(a.member.initialLow);
      _builder.append(" ");
      _builder.append(a.type.goName);
      _builder.append(") ");
      strList.add(_builder.toString());
    }
    for (final ZEnumField a_1 : e.enumFields) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Set");
      _builder_1.append(a_1.member.originName);
      _builder_1.append(" (");
      _builder_1.append(a_1.member.initialLow);
      _builder_1.append(" ");
      _builder_1.append(a_1.type.goName);
      _builder_1.append(") ");
      strList.add(_builder_1.toString());
    }
    for (final ZSingleAss a_2 : e.singleAsses) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("Set");
      _builder_2.append(a_2.originName);
      _builder_2.append(" (");
      _builder_2.append(a_2.targetEntity.initialLow);
      _builder_2.append(" ");
      _builder_2.append(a_2.targetEntity.originName);
      _builder_2.append(") ");
      strList.add(_builder_2.toString());
    }
    for (final ZMultiAss a_3 : e.multiAsses) {
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("Add");
      _builder_3.append(a_3.originName);
      _builder_3.append(" (");
      _builder_3.append(a_3.targetEntity.initialLow);
      _builder_3.append(" ");
      _builder_3.append(a_3.targetEntity.originName);
      _builder_3.append(") ");
      strList.add(_builder_3.toString());
    }
    return strList;
  }
  
  public static CharSequence generateInterface(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type ");
    _builder.append(e.entityName.originName);
    _builder.append(" interface{");
    _builder.newLineIfNotEmpty();
    {
      if ((e.parentEntity != null)) {
        _builder.append("\t");
        _builder.append(e.parentEntity.originName, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      ArrayList<String> _generateGetterDeclares = EntityGenerator.generateGetterDeclares(e);
      for(final String declare : _generateGetterDeclares) {
        _builder.append("\t");
        _builder.append(declare, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      ArrayList<String> _generateSetterDeclares = EntityGenerator.generateSetterDeclares(e);
      for(final String declare_1 : _generateSetterDeclares) {
        _builder.append("\t");
        _builder.append(declare_1, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence generateStruct(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type ");
    _builder.append(e.entityName.originName);
    _builder.append("Entity struct{");
    _builder.newLineIfNotEmpty();
    {
      if (((e.isBaseEntity == false) && (e.parentEntity == null))) {
        _builder.append("\t");
        _builder.append("entityManager.Entity");
        _builder.newLine();
      } else {
        if ((e.parentEntity != null)) {
          _builder.append("\t");
          _builder.append(e.parentEntity.originName, "\t");
          _builder.append("Entity");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("entityManager.FieldChange");
          _builder.newLine();
        } else {
          if ((e.isBaseEntity == true)) {
            _builder.append("\t");
            _builder.append("entityManager.BasicEntity");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    {
      for(final ZBasicField a : e.basicFields) {
        _builder.append("\t");
        _builder.append(a.member.originName, "\t");
        _builder.append(" ");
        _builder.append(a.type.goName, "\t");
        _builder.append(" `db:\"");
        _builder.append(a.member.underline, "\t");
        _builder.append("\"`");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final ZEnumField a_1 : e.enumFields) {
        _builder.append("\t");
        _builder.append(a_1.member.originName, "\t");
        _builder.append(" ");
        _builder.append(a_1.type.goName, "\t");
        _builder.append(" `db:\"");
        _builder.append(a_1.member.underline, "\t");
        _builder.append("\"`");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final ZSingleAss a_2 : e.singleAsses) {
        _builder.append("\t");
        _builder.append(a_2.field.originName, "\t");
        _builder.append(" *int `db:\"");
        _builder.append(a_2.field.underline, "\t");
        _builder.append("\"`");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private static CharSequence generateFuncPrefix(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (p *");
    _builder.append(e.entityName.originName);
    _builder.append("Entity)");
    return _builder;
  }
  
  public static CharSequence generateOtherImplements(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((e.parentEntity != null)) {
        CharSequence _generateFuncPrefix = EntityGenerator.generateFuncPrefix(e);
        _builder.append(_generateFuncPrefix);
        _builder.append(" GetParentEntity() entityManager.EntityForInheritManager {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("return &p.");
        _builder.append(e.parentEntity.originName, "\t");
        _builder.append("Entity");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence generateSetters(final ZEntity e) {
    CharSequence _xblockexpression = null;
    {
      ArrayList<EntityGenerator.Func> funcList = new ArrayList<EntityGenerator.Func>();
      ArrayList<String> dList = EntityGenerator.generateSetterDeclares(e);
      ArrayList<String> bList = EntityGenerator.generateSetterBodies(e);
      for (int i = 0; (i < ((Object[])Conversions.unwrapArray(dList, Object.class)).length); i++) {
        {
          String b = bList.get(i);
          String d = dList.get(i);
          EntityGenerator.Func func = new EntityGenerator.Func();
          func.body = b;
          func.declare = d;
          funcList.add(func);
        }
      }
      StringConcatenation _builder = new StringConcatenation();
      {
        for(final EntityGenerator.Func func : funcList) {
          CharSequence _generateFuncPrefix = EntityGenerator.generateFuncPrefix(e);
          _builder.append(_generateFuncPrefix);
          _builder.append(" ");
          _builder.append(func.declare);
          _builder.append(" {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(func.body, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public static CharSequence generateGetters(final ZEntity e) {
    CharSequence _xblockexpression = null;
    {
      ArrayList<EntityGenerator.Func> funcList = new ArrayList<EntityGenerator.Func>();
      ArrayList<String> dList = EntityGenerator.generateGetterDeclares(e);
      ArrayList<String> bList = EntityGenerator.generateGetterBodies(e);
      for (int i = 0; (i < ((Object[])Conversions.unwrapArray(dList, Object.class)).length); i++) {
        {
          String b = bList.get(i);
          String d = dList.get(i);
          EntityGenerator.Func func = new EntityGenerator.Func();
          func.body = b;
          func.declare = d;
          funcList.add(func);
        }
      }
      StringConcatenation _builder = new StringConcatenation();
      {
        for(final EntityGenerator.Func func : funcList) {
          CharSequence _generateFuncPrefix = EntityGenerator.generateFuncPrefix(e);
          _builder.append(_generateFuncPrefix);
          _builder.append(" ");
          _builder.append(func.declare);
          _builder.append(" {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(func.body, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
}
