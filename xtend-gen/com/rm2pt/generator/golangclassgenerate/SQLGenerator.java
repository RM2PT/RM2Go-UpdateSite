package com.rm2pt.generator.golangclassgenerate;

import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class SQLGenerator {
  public static CharSequence generate(final Collection<ZEntity> entities) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final ZEntity e : entities) {
        CharSequence _compileCreateTable = SQLGenerator.compileCreateTable(e);
        _builder.append(_compileCreateTable);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final ZEntity e_1 : entities) {
        CharSequence _compileAlterTable = SQLGenerator.compileAlterTable(e_1);
        _builder.append(_compileAlterTable);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence compileCreateTable(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("create table ");
    _builder.append(e.entityName.underline);
    _builder.newLineIfNotEmpty();
    _builder.append("(");
    _builder.newLine();
    _builder.append("\t");
    String _compileCreateTableItems = SQLGenerator.compileCreateTableItems(e);
    _builder.append(_compileCreateTableItems, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append(");");
    _builder.newLine();
    _builder.newLine();
    {
      for(final ZMultiAss ass : e.multiAsses) {
        _builder.append("create table ");
        _builder.append(ass.tableName);
        _builder.newLineIfNotEmpty();
        _builder.append("(");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("owner_goen_id      int,");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("possession_goen_id int,");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("primary key (owner_goen_id, possession_goen_id)");
        _builder.newLine();
        _builder.append(");");
        _builder.newLine();
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private static String compileCreateTableItems(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("goen_id                  int primary key,");
    _builder.newLine();
    {
      if ((e.parentEntity == null)) {
        _builder.append("goen_in_all_instance     bool not null default (false),");
        _builder.newLine();
      }
    }
    {
      if ((e.isBaseEntity && (e.parentEntity == null))) {
        _builder.append("goen_inherit_type    int   not null default (0),");
        _builder.newLine();
      }
    }
    {
      for(final ZBasicField field : e.basicFields) {
        _builder.append(field.member.underline);
        _builder.append(" ");
        _builder.append(field.type.sqlName);
        _builder.append(" ");
        _builder.append(field.type.sqlConstraint);
        _builder.append(",");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final ZEnumField field_1 : e.enumFields) {
        _builder.append(field_1.member.underline);
        _builder.append(" int not null default (0), #枚举类型 ");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final ZSingleAss field_2 : e.singleAsses) {
        _builder.append(field_2.field.underline);
        _builder.append(" int, ");
        _builder.newLineIfNotEmpty();
      }
    }
    CharSequence _removeComma = Tool.removeComma(_builder);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("\t\t");
    _builder_1.newLine();
    return (_removeComma + _builder_1.toString());
  }
  
  public static CharSequence compileAlterTable(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (((((Object[])Conversions.unwrapArray(e.singleAsses, Object.class)).length != 0) || (e.parentEntity != null))) {
        _builder.append("alter table ");
        _builder.append(e.entityName.underline);
        _builder.newLineIfNotEmpty();
        String _compileAlterTableItems = SQLGenerator.compileAlterTableItems(e);
        _builder.append(_compileAlterTableItems);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      for(final ZMultiAss multiAss : e.multiAsses) {
        _builder.append("alter table ");
        _builder.append(multiAss.tableName);
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("add constraint foreign key (owner_goen_id) references ");
        _builder.append(e.entityName.underline, "\t");
        _builder.append(" (goen_id) on delete cascade,");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("add constraint foreign key (possession_goen_id) references ");
        _builder.append(multiAss.targetEntity.underline, "    ");
        _builder.append(" (goen_id) on delete cascade;");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private static String compileAlterTableItems(final ZEntity e) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final ZSingleAss ass : e.singleAsses) {
        _builder.append("add constraint foreign key (");
        _builder.append(ass.field.underline);
        _builder.append(") references ");
        _builder.append(ass.targetEntity.underline);
        _builder.append("(goen_id) on delete set null,");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((e.parentEntity != null)) {
        _builder.append("add constraint foreign key (goen_id) references ");
        _builder.append(e.parentEntity.underline);
        _builder.append(" (goen_id) on delete cascade,");
        _builder.newLineIfNotEmpty();
      }
    }
    CharSequence _removeComma = Tool.removeComma(_builder);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(";");
    _builder_1.newLine();
    return (_removeComma + _builder_1.toString());
  }
}
