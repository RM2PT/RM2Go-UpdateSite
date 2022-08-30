package com.rm2pt.generator.golangclassgenerate;

import com.google.common.base.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.mydreamy.requirementmodel.rEMODEL.EntityType;
import net.mydreamy.requirementmodel.rEMODEL.EnumEntity;
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveTypeCS;
import net.mydreamy.requirementmodel.rEMODEL.TypeCS;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Tool {
  public static String camelToUnderScore(final String line) {
    if (((line == null) || "".equals(line))) {
      return "";
    }
    String line2 = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
    StringBuffer sb = new StringBuffer();
    Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
    Matcher matcher = pattern.matcher(line2);
    while (matcher.find()) {
      {
        String word = matcher.group();
        sb.append(word.toLowerCase());
        String _xifexpression = null;
        int _end = matcher.end();
        int _length = line2.length();
        boolean _equals = (_end == _length);
        if (_equals) {
          _xifexpression = "";
        } else {
          _xifexpression = "_";
        }
        sb.append(_xifexpression);
      }
    }
    return sb.toString();
  }
  
  public static String initialLowerCase(final String name) {
    String _substring = name.toLowerCase().substring(0, 1);
    String _substring_1 = name.substring(1);
    return (_substring + _substring_1);
  }
  
  public static String compileGoTypeName(final TypeCS type) {
    String _xifexpression = null;
    if ((type != null)) {
      String _switchResult = null;
      boolean _matched = false;
      if (type instanceof PrimitiveTypeCS) {
        _matched=true;
        String _switchResult_1 = null;
        boolean _matched_1 = false;
        String _name = ((PrimitiveTypeCS)type).getName();
        boolean _equals = Objects.equal(_name, "Boolean");
        if (_equals) {
          _matched_1=true;
          _switchResult_1 = "bool";
        }
        if (!_matched_1) {
          String _name_1 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_1 = Objects.equal(_name_1, "String");
          if (_equals_1) {
            _matched_1=true;
            _switchResult_1 = "string";
          }
        }
        if (!_matched_1) {
          String _name_2 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_2 = Objects.equal(_name_2, "Real");
          if (_equals_2) {
            _matched_1=true;
            _switchResult_1 = "float64";
          }
        }
        if (!_matched_1) {
          String _name_3 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_3 = Objects.equal(_name_3, "Integer");
          if (_equals_3) {
            _matched_1=true;
            _switchResult_1 = "int";
          }
        }
        if (!_matched_1) {
          String _name_4 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_4 = Objects.equal(_name_4, "Date");
          if (_equals_4) {
            _matched_1=true;
            _switchResult_1 = "time.Time";
          }
        }
        if (!_matched_1) {
          _switchResult_1 = "";
        }
        _switchResult = _switchResult_1;
      }
      if (!_matched) {
        if (type instanceof EnumEntity) {
          _matched=true;
          _switchResult = ((EnumEntity)type).getName();
        }
      }
      if (!_matched) {
        if (type instanceof EntityType) {
          _matched=true;
          _switchResult = ((EntityType)type).getEntity().getName();
        }
      }
      if (!_matched) {
        _switchResult = "";
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  public static String compileSqlType(final TypeCS type) {
    String _xifexpression = null;
    if ((type != null)) {
      String _switchResult = null;
      boolean _matched = false;
      if (type instanceof PrimitiveTypeCS) {
        _matched=true;
        String _switchResult_1 = null;
        boolean _matched_1 = false;
        String _name = ((PrimitiveTypeCS)type).getName();
        boolean _equals = Objects.equal(_name, "Boolean");
        if (_equals) {
          _matched_1=true;
          _switchResult_1 = "bool";
        }
        if (!_matched_1) {
          String _name_1 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_1 = Objects.equal(_name_1, "String");
          if (_equals_1) {
            _matched_1=true;
            _switchResult_1 = "varchar(255)";
          }
        }
        if (!_matched_1) {
          String _name_2 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_2 = Objects.equal(_name_2, "Real");
          if (_equals_2) {
            _matched_1=true;
            _switchResult_1 = "float";
          }
        }
        if (!_matched_1) {
          String _name_3 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_3 = Objects.equal(_name_3, "Integer");
          if (_equals_3) {
            _matched_1=true;
            _switchResult_1 = "int";
          }
        }
        if (!_matched_1) {
          String _name_4 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_4 = Objects.equal(_name_4, "Date");
          if (_equals_4) {
            _matched_1=true;
            _switchResult_1 = "datetime";
          }
        }
        if (!_matched_1) {
          _switchResult_1 = "";
        }
        _switchResult = _switchResult_1;
      }
      if (!_matched) {
        if (type instanceof EnumEntity) {
          _matched=true;
          _switchResult = "int";
        }
      }
      if (!_matched) {
        if (type instanceof EntityType) {
          _matched=true;
          _switchResult = ((EntityType)type).getEntity().getName();
        }
      }
      if (!_matched) {
        _switchResult = "";
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  public static String compileSqlBasicConstraint(final TypeCS type) {
    String _xifexpression = null;
    if ((type != null)) {
      String _switchResult = null;
      boolean _matched = false;
      if (type instanceof PrimitiveTypeCS) {
        _matched=true;
        String _switchResult_1 = null;
        boolean _matched_1 = false;
        String _name = ((PrimitiveTypeCS)type).getName();
        boolean _equals = Objects.equal(_name, "Boolean");
        if (_equals) {
          _matched_1=true;
          _switchResult_1 = "bool";
        }
        if (!_matched_1) {
          String _name_1 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_1 = Objects.equal(_name_1, "String");
          if (_equals_1) {
            _matched_1=true;
            _switchResult_1 = "varchar(255)";
          }
        }
        if (!_matched_1) {
          String _name_2 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_2 = Objects.equal(_name_2, "Real");
          if (_equals_2) {
            _matched_1=true;
            _switchResult_1 = "float";
          }
        }
        if (!_matched_1) {
          String _name_3 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_3 = Objects.equal(_name_3, "Integer");
          if (_equals_3) {
            _matched_1=true;
            _switchResult_1 = "int";
          }
        }
        if (!_matched_1) {
          String _name_4 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_4 = Objects.equal(_name_4, "Date");
          if (_equals_4) {
            _matched_1=true;
            _switchResult_1 = "datetime";
          }
        }
        if (!_matched_1) {
          _switchResult_1 = "";
        }
        _switchResult = _switchResult_1;
      }
      if (!_matched) {
        if (type instanceof EnumEntity) {
          _matched=true;
          _switchResult = "int";
        }
      }
      if (!_matched) {
        if (type instanceof EntityType) {
          _matched=true;
          _switchResult = ((EntityType)type).getEntity().getName();
        }
      }
      if (!_matched) {
        _switchResult = "";
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  public static String compileSqlBasicConstraint(final String typeName) {
    String _switchResult = null;
    if (typeName != null) {
      switch (typeName) {
        case "Boolean":
          _switchResult = "not null default(false)";
          break;
        case "String":
          _switchResult = "not null default(\'\')";
          break;
        case "Real":
          _switchResult = "not null default(0)";
          break;
        case "Integer":
          _switchResult = "not null default(0)";
          break;
        case "Date":
          _switchResult = "not null default(\'0001-01-01 00:00:00\')";
          break;
        default:
          _switchResult = "";
          break;
      }
    } else {
      _switchResult = "";
    }
    return _switchResult;
  }
  
  public static String compileGoTypeName(final String typeName) {
    String _switchResult = null;
    if (typeName != null) {
      switch (typeName) {
        case "Boolean":
          _switchResult = "bool";
          break;
        case "String":
          _switchResult = "string";
          break;
        case "Real":
          _switchResult = "float64";
          break;
        case "Integer":
          _switchResult = "int";
          break;
        case "Date":
          _switchResult = "time.Time";
          break;
        default:
          _switchResult = "";
          break;
      }
    } else {
      _switchResult = "";
    }
    return _switchResult;
  }
  
  public static String compileGoImport(final String typeName) {
    String _switchResult = null;
    if (typeName != null) {
      switch (typeName) {
        case "Date":
          _switchResult = "\"time\"";
          break;
        default:
          _switchResult = null;
          break;
      }
    } else {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  public static String compileSqlTypeName(final String typeName) {
    String _switchResult = null;
    if (typeName != null) {
      switch (typeName) {
        case "Boolean":
          _switchResult = "boolean";
          break;
        case "String":
          _switchResult = "varchar(255)";
          break;
        case "Real":
          _switchResult = "double";
          break;
        case "Integer":
          _switchResult = "int";
          break;
        case "Date":
          _switchResult = "datetime";
          break;
        default:
          _switchResult = "";
          break;
      }
    } else {
      _switchResult = "";
    }
    return _switchResult;
  }
  
  public static CharSequence removeComma(final CharSequence str) {
    for (int i = (str.length() - 1); (i >= 0); i--) {
      String _string = Character.valueOf(str.charAt(i)).toString();
      String _string_1 = ",".toString();
      boolean _equals = Objects.equal(_string, _string_1);
      if (_equals) {
        return str.subSequence(0, i);
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("why last????");
    return _builder.toString();
  }
}
