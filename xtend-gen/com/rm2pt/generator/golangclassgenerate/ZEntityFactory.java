package com.rm2pt.generator.golangclassgenerate;

import java.util.Collection;
import java.util.TreeMap;
import net.mydreamy.requirementmodel.rEMODEL.Attribute;
import net.mydreamy.requirementmodel.rEMODEL.Entity;
import net.mydreamy.requirementmodel.rEMODEL.EnumEntity;
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveTypeCS;
import net.mydreamy.requirementmodel.rEMODEL.Reference;
import net.mydreamy.requirementmodel.rEMODEL.TypeCS;
import org.eclipse.emf.common.util.EList;

@SuppressWarnings("all")
public class ZEntityFactory {
  public static Collection<ZEntity> generateZEntities(final Collection<Entity> entities) {
    TreeMap<String, ZEntity> zEntities = new TreeMap<String, ZEntity>();
    System.out.println("第一遍,现在的entities:");
    for (final Entity e : entities) {
      {
        ZEntity ze = new ZEntity();
        String _name = e.getName();
        ZName _zName = new ZName(_name);
        ze.entityName = _zName;
        EList<Attribute> _attributes = e.getAttributes();
        for (final Attribute a : _attributes) {
          TypeCS _type = a.getType();
          boolean _matched = false;
          if (_type instanceof PrimitiveTypeCS) {
            _matched=true;
            String _name_1 = a.getName();
            TypeCS _type_1 = a.getType();
            String _name_2 = ((PrimitiveTypeCS) _type_1).getName();
            ZBasicField _zBasicField = new ZBasicField(_name_1, _name_2);
            ze.basicFields.add(_zBasicField);
          }
          if (!_matched) {
            if (_type instanceof EnumEntity) {
              _matched=true;
              String _name_1 = a.getName();
              TypeCS _type_1 = a.getType();
              ZEnumField _zEnumField = new ZEnumField(_name_1, ((EnumEntity) _type_1));
              ze.enumFields.add(_zEnumField);
            }
          }
        }
        EList<Reference> _reference = e.getReference();
        for (final Reference a_1 : _reference) {
          boolean _isIsmultiple = a_1.isIsmultiple();
          boolean _not = (!_isIsmultiple);
          if (_not) {
            String _name_1 = a_1.getName();
            String _name_2 = a_1.getEntity().getName();
            ZSingleAss _zSingleAss = new ZSingleAss(_name_1, _name_2);
            ze.singleAsses.add(_zSingleAss);
          } else {
            String _name_3 = a_1.getName();
            String _name_4 = e.getName();
            String _name_5 = a_1.getEntity().getName();
            ZMultiAss _zMultiAss = new ZMultiAss(_name_3, _name_4, _name_5);
            ze.multiAsses.add(_zMultiAss);
          }
        }
        zEntities.put(e.getName(), ze);
      }
    }
    System.out.println("第二遍,现在的entities:");
    System.out.println(entities);
    for (final Entity e_1 : entities) {
      Entity _superEntity = e_1.getSuperEntity();
      boolean _tripleNotEquals = (_superEntity != null);
      if (_tripleNotEquals) {
        System.out.println("我进来楼");
        ZEntity _get = zEntities.get(e_1.getSuperEntity().getName());
        _get.isBaseEntity = true;
        ZEntity _get_1 = zEntities.get(e_1.getName());
        String _name = e_1.getSuperEntity().getName();
        ZName _zName = new ZName(_name);
        _get_1.parentEntity = _zName;
      }
    }
    return zEntities.values();
  }
}
