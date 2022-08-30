package com.rm2pt.generator.golangclassgenerate;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import net.mydreamy.requirementmodel.rEMODEL.AtomicExpression;
import net.mydreamy.requirementmodel.rEMODEL.ClassiferCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.Definition;
import net.mydreamy.requirementmodel.rEMODEL.LeftSubAtomicExpression;
import net.mydreamy.requirementmodel.rEMODEL.LogicFormulaExpCS;
import net.mydreamy.requirementmodel.rEMODEL.OCLExpressionCS;
import net.mydreamy.requirementmodel.rEMODEL.Precondition;
import net.mydreamy.requirementmodel.rEMODEL.PredefineOp;
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveLiteralExpCS;
import net.mydreamy.requirementmodel.rEMODEL.PropertyCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.RightSubAtomicExpression;
import net.mydreamy.requirementmodel.rEMODEL.StandardCollectionOperation;
import net.mydreamy.requirementmodel.rEMODEL.StandardNavigationCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.StandardOperationExpCS;
import net.mydreamy.requirementmodel.rEMODEL.VariableDeclarationCS;
import net.mydreamy.requirementmodel.rEMODEL.VariableExpCS;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class ContractGenerator {
  public static void generate(final Resource resource) {
    Iterable<Precondition> _filter = Iterables.<Precondition>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Precondition.class);
    for (final Precondition pre : _filter) {
      System.out.println(ContractGenerator.generatePrecondition(pre));
    }
    Iterable<Definition> _filter_1 = Iterables.<Definition>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Definition.class);
    for (final Definition defi : _filter_1) {
      System.out.println(ContractGenerator.generateDefinition(defi));
    }
  }
  
  public static CharSequence generatePrecondition(final Precondition pre) {
    CharSequence _switchResult = null;
    OCLExpressionCS _oclexp = pre.getOclexp();
    final OCLExpressionCS oclexp = _oclexp;
    boolean _matched = false;
    if (oclexp instanceof LogicFormulaExpCS) {
      _matched=true;
      CharSequence _xblockexpression = null;
      {
        ArrayList<AtomicExpression> atomics = new ArrayList<AtomicExpression>();
        EList<EObject> _atomicexp = ((LogicFormulaExpCS)oclexp).getAtomicexp();
        for (final EObject exp : _atomicexp) {
          boolean _matched_1 = false;
          if (exp instanceof AtomicExpression) {
            _matched_1=true;
            atomics.add(((AtomicExpression)exp));
          }
        }
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if !(");
        _builder.newLine();
        {
          boolean _hasElements = false;
          for(final AtomicExpression atomic : atomics) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate("&&", "\t");
            }
            _builder.append("\t");
            CharSequence _generatePreAtomicExpression = ContractGenerator.generatePreAtomicExpression(atomic);
            _builder.append(_generatePreAtomicExpression, "\t");
            _builder.append(" ");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return false, ErrPreConditionUnsatisfied");
        _builder.newLine();
        _builder.append("}");
        _xblockexpression = _builder;
      }
      _switchResult = _xblockexpression;
    }
    if (!_matched) {
      _switchResult = "goenUndefined!";
    }
    return _switchResult;
  }
  
  public static CharSequence generateDefinition(final Definition defi) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<VariableDeclarationCS> _variable = defi.getVariable();
      for(final VariableDeclarationCS vari : _variable) {
        {
          if ((((vari instanceof VariableDeclarationCS) && (vari.getInitExpression() instanceof LogicFormulaExpCS)) && (((Object[])Conversions.unwrapArray(((LogicFormulaExpCS) vari.getInitExpression()).getAtomicexp(), Object.class)).length == 1))) {
            _builder.append("var ");
            String _name = vari.getName();
            _builder.append(_name);
            _builder.append(" ");
            String _compileGoTypeName = Tool.compileGoTypeName(vari.getType());
            _builder.append(_compileGoTypeName);
            _builder.append(" = ");
            OCLExpressionCS _initExpression = vari.getInitExpression();
            EObject _get = ((LogicFormulaExpCS) _initExpression).getAtomicexp().get(0);
            CharSequence _generatePreAtomicExpression = ContractGenerator.generatePreAtomicExpression(((AtomicExpression) _get));
            _builder.append(_generatePreAtomicExpression);
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("goenUndefined!");
            _builder.newLine();
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence generatePreAtomicExpression(final AtomicExpression exp) {
    CharSequence _xblockexpression = null;
    {
      String _switchResult = null;
      String _infixop = exp.getInfixop();
      if (_infixop != null) {
        switch (_infixop) {
          case "=":
            _switchResult = "==";
            break;
          default:
            _switchResult = exp.getInfixop();
            break;
        }
      } else {
        _switchResult = exp.getInfixop();
      }
      String op = _switchResult;
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _generatePreLeftSide = ContractGenerator.generatePreLeftSide(exp.getLeftside());
      _builder.append(_generatePreLeftSide);
      _builder.append(" ");
      _builder.append(op);
      _builder.append(" ");
      CharSequence _generateRightSide = ContractGenerator.generateRightSide(exp.getRightside());
      _builder.append(_generateRightSide);
      _builder.append(" ");
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public static CharSequence generatePreLeftSide(final LeftSubAtomicExpression exp) {
    throw new Error("Unresolved compilation problems:"
      + "\nno viable alternative at input \'\'\'\'\\r\\n\\t\\t\\t\\t\\t\\t\\tdefault : \"goenUndefined!\"\\r\\n\\t\\t\\t\\t\\t\\t}\'\'\'\'"
      + "\nmissing \'}\' at \'default\'"
      + "\nThe method or field standardOP is undefined for the type ClassiferCallExpCS"
      + "\nobject cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  public static CharSequence generateRightSide(final RightSubAtomicExpression exp) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (exp instanceof VariableExpCS) {
      _matched=true;
      _switchResult = ((VariableExpCS)exp).getSymbol();
    }
    if (!_matched) {
      if (exp instanceof PropertyCallExpCS) {
        _matched=true;
        String _symbol = ((PropertyCallExpCS)exp).getName().getSymbol();
        String _plus = (_symbol + ".Get");
        String _attribute = ((PropertyCallExpCS)exp).getAttribute();
        String _plus_1 = (_plus + _attribute);
        _switchResult = (_plus_1 + "()");
      }
    }
    if (!_matched) {
      if (exp instanceof PrimitiveLiteralExpCS) {
        _matched=true;
        _switchResult = ((PrimitiveLiteralExpCS)exp).getSymbol();
      }
    }
    if (!_matched) {
      if (exp instanceof StandardNavigationCallExpCS) {
        _matched=true;
        String _xifexpression = null;
        if (((((StandardNavigationCallExpCS)exp).getPropertycall() != null) && (((StandardNavigationCallExpCS)exp).getClassifercall() == null))) {
          String _xblockexpression = null;
          {
            PropertyCallExpCS property = ((StandardNavigationCallExpCS)exp).getPropertycall();
            String _switchResult_1 = null;
            String _name = ((StandardNavigationCallExpCS)exp).getStandardOP().getName();
            if (_name != null) {
              switch (_name) {
                case "includes":
                  String _symbol = property.getName().getSymbol();
                  String _plus = (_symbol + ".Add");
                  String _attribute = property.getAttribute();
                  String _plus_1 = (_plus + _attribute);
                  String _plus_2 = (_plus_1 + "(");
                  String _object = ((StandardNavigationCallExpCS)exp).getStandardOP().getObject();
                  String _plus_3 = (_plus_2 + _object);
                  _switchResult_1 = (_plus_3 + ")");
                  break;
                case "excludes":
                  _switchResult_1 = "goenUndefined!";
                  break;
                default:
                  _switchResult_1 = "goenUndefined!";
                  break;
              }
            } else {
              _switchResult_1 = "goenUndefined!";
            }
            _xblockexpression = _switchResult_1;
          }
          _xifexpression = _xblockexpression;
        } else {
          String _xifexpression_1 = null;
          if ((((((StandardNavigationCallExpCS)exp).getPropertycall() == null) && (((StandardNavigationCallExpCS)exp).getClassifercall() != null)) && Objects.equal(((StandardNavigationCallExpCS)exp).getClassifercall().getOp(), "allInstance()"))) {
            String _xblockexpression_1 = null;
            {
              ClassiferCallExpCS classifer = ((StandardNavigationCallExpCS)exp).getClassifercall();
              String _switchResult_1 = null;
              String _name = ((StandardNavigationCallExpCS)exp).getStandardOP().getName();
              if (_name != null) {
                switch (_name) {
                  case "includes":
                    String _entity = classifer.getEntity();
                    String _plus = ("entity." + _entity);
                    String _plus_1 = (_plus + "Manager.AddInAllInstance");
                    String _plus_2 = (_plus_1 + "(");
                    String _object = ((StandardNavigationCallExpCS)exp).getStandardOP().getObject();
                    String _plus_3 = (_plus_2 + _object);
                    _switchResult_1 = (_plus_3 + ")");
                    break;
                  case "excludes":
                    _switchResult_1 = "goenUndefined!";
                    break;
                  default:
                    _switchResult_1 = "goenUndefined!";
                    break;
                }
              } else {
                _switchResult_1 = "goenUndefined!";
              }
              _xblockexpression_1 = _switchResult_1;
            }
            _xifexpression_1 = _xblockexpression_1;
          } else {
            _xifexpression_1 = "goenUndefined!";
          }
          _xifexpression = _xifexpression_1;
        }
        _switchResult = _xifexpression;
      }
    }
    if (!_matched) {
      if (exp instanceof StandardOperationExpCS) {
        _matched=true;
        CharSequence _switchResult_1 = null;
        String _name = ((StandardOperationExpCS)exp).getPredefinedop().getName();
        if (_name != null) {
          switch (_name) {
            case "oclIsUndefined()":
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("(");
              String _symbol = ((StandardOperationExpCS)exp).getObject().getSymbol();
              _builder.append(_symbol);
              _builder.append(" == nil)");
              _switchResult_1 = _builder;
              break;
            default:
              _switchResult_1 = "goenUndefined!";
              break;
          }
        } else {
          _switchResult_1 = "goenUndefined!";
        }
        _switchResult = _switchResult_1;
      }
    }
    return _switchResult;
  }
  
  public static Object generateAtomicExpression(final StandardCollectionOperation exp) {
    return null;
  }
  
  public static Object generateLeftSubAtomicExpression(final LeftSubAtomicExpression exp) {
    return null;
  }
  
  public static String generateVariableExpCS(final VariableExpCS va) {
    String _switchResult = null;
    String _symbol = va.getSymbol();
    if (_symbol != null) {
      switch (_symbol) {
        case "Now":
          _switchResult = "time.Now()";
          break;
        default:
          _switchResult = va.getSymbol();
          break;
      }
    } else {
      _switchResult = va.getSymbol();
    }
    return _switchResult;
  }
  
  public static Object generateStandardOperationExpCS(final StandardOperationExpCS exp) {
    Object _switchResult = null;
    PredefineOp _predefinedop = exp.getPredefinedop();
    boolean _matched = false;
    return _switchResult;
  }
  
  public static Object generatePredefineOp(final PredefineOp op) {
    return null;
  }
}
