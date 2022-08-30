package com.rm2pt.generator.golangclassgenerate

import net.mydreamy.requirementmodel.rEMODEL.AtomicExpression
import net.mydreamy.requirementmodel.rEMODEL.LeftSubAtomicExpression
import net.mydreamy.requirementmodel.rEMODEL.StandardOperationExpCS
import net.mydreamy.requirementmodel.rEMODEL.VariableExpCS
import net.mydreamy.requirementmodel.rEMODEL.PredefineOp
import net.mydreamy.requirementmodel.rEMODEL.StandardNavigationCallExpCS
import net.mydreamy.requirementmodel.rEMODEL.StandardCollectionOperation
import net.mydreamy.requirementmodel.rEMODEL.RightSubAtomicExpression
import net.mydreamy.requirementmodel.rEMODEL.PropertyCallExpCS
import net.mydreamy.requirementmodel.rEMODEL.BooleanLiteralExpCS
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveLiteralExpCS
import net.mydreamy.requirementmodel.rEMODEL.Precondition
import net.mydreamy.requirementmodel.rEMODEL.LogicFormulaExpCS
import org.eclipse.emf.ecore.resource.Resource
import java.util.ArrayList
import net.mydreamy.requirementmodel.rEMODEL.Definition
import net.mydreamy.requirementmodel.rEMODEL.VariableDeclarationCS
import net.mydreamy.requirementmodel.rEMODEL.IteratorExpCS
import net.mydreamy.requirementmodel.rEMODEL.ClassiferCallExpCS

class ContractGenerator {
	static def generate(Resource resource) {
		for(pre : resource.allContents.toIterable.filter(typeof(Precondition))){
			System.out.println(generatePrecondition(pre))
		}
		for(defi : resource.allContents.toIterable.filter(typeof(Definition))){
			System.out.println(generateDefinition(defi))
		}
	}
	static def generatePrecondition(Precondition pre){
		switch oclexp : pre.oclexp{
			LogicFormulaExpCS: {
				var atomics = new ArrayList<AtomicExpression>()
				for (exp : oclexp.atomicexp){
					switch(exp){
						AtomicExpression: atomics.add(exp)
					}
				}		
				'''
				if !(
					«FOR atomic : atomics SEPARATOR '&&'»
					«generatePreAtomicExpression(atomic)» 
					«ENDFOR»
				){
					return false, ErrPreConditionUnsatisfied
				}'''
			}
			default : "goenUndefined!"
		}
		
	}
	static def generateDefinition(Definition defi){
		'''
		«FOR vari : defi.variable»
		«IF vari instanceof VariableDeclarationCS && vari.initExpression instanceof LogicFormulaExpCS && (vari.initExpression as LogicFormulaExpCS).atomicexp.length() == 1»
		var «vari.name» «Tool.compileGoTypeName(vari.type)» = «generatePreAtomicExpression((vari.initExpression as LogicFormulaExpCS).atomicexp.get(0) as AtomicExpression)»
		«ELSE»
		goenUndefined!
		«ENDIF»
		«ENDFOR»
		'''
		
	}
	
	static def generatePreAtomicExpression(AtomicExpression exp){
		var op = switch(exp.infixop) {
			case "=" : "=="
			default  : exp.infixop
		}
		'''«generatePreLeftSide(exp.leftside)» «op» «generateRightSide(exp.rightside)» '''
	}
	
	static def generatePreLeftSide(LeftSubAtomicExpression exp){
		switch(exp){
			VariableExpCS: exp.symbol
			PropertyCallExpCS : exp.name.symbol + ".Get" + exp.attribute + "()"
			PrimitiveLiteralExpCS : exp.symbol
			StandardNavigationCallExpCS : {
				if(exp.propertycall !== null && exp.classifercall === null){
					var property = exp.propertycall
					switch(exp.standardOP.name){
						case "includes": 
							property.name.symbol + ".Add" + property.attribute + "(" + exp.standardOP.object +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else if(exp.propertycall === null && exp.classifercall !== null && exp.classifercall.op == "allInstance()"){
					var classifer = exp.classifercall
					switch(exp.standardOP.name){
						case "includes": 
							"entity." + classifer.entity + "Manager.AddInAllInstance" + "(" + exp.standardOP.object +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else {
					"goenUndefined!"
				}
			}
			StandardOperationExpCS :{
				switch(exp.predefinedop.name){
					case "oclIsUndefined()" : '''(«exp.object.symbol» == nil)'''
					default : "goenUndefined!"
				}
			}
			IteratorExpCS: {
				switch(exp.iterator){
					case "any" : 
						switch(call : exp.objectCall){
							ClassiferCallExpCS: '''entity.«call.entity»Manager.GetFromAllInstance(«call.standardOP.object +")"'''
							default : "goenUndefined!"
						}'''
				}
			}
			default: "goenUndefined!"
		}
	} 
	static def generateRightSide(RightSubAtomicExpression exp){
		// 完全复制generatePreLeftSide的代码
		switch(exp){
			VariableExpCS: exp.symbol
			PropertyCallExpCS : exp.name.symbol + ".Get" + exp.attribute + "()"
			PrimitiveLiteralExpCS : exp.symbol
			StandardNavigationCallExpCS : {
				if(exp.propertycall !== null && exp.classifercall === null){
					var property = exp.propertycall
					switch(exp.standardOP.name){
						case "includes": 
							property.name.symbol + ".Add" + property.attribute + "(" + exp.standardOP.object +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else if(exp.propertycall === null && exp.classifercall !== null && exp.classifercall.op == "allInstance()"){
					var classifer = exp.classifercall
					switch(exp.standardOP.name){
						case "includes": 
							"entity." + classifer.entity + "Manager.AddInAllInstance" + "(" + exp.standardOP.object +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else {
					"goenUndefined!"
				}
			}
			StandardOperationExpCS :{
				switch(exp.predefinedop.name){
					case "oclIsUndefined()" : '''(«exp.object.symbol» == nil)'''
					default : "goenUndefined!"
				}
			}
		}
	}
	
	static def generateAtomicExpression(StandardCollectionOperation exp){
//		System.out.println("in atomic expression" + exp);
//		System.out.println("exp.leftside:" + exp.leftside);
//		System.out.println("exp.rightside" + exp.rightside);
//		System.out.println("exp.op" + exp.op);
//		System.out.println("exp.exp:" + exp.exp);
//		System.out.println("exp.num:" + exp.num);
//		if(exp.infixop === null) {
//			
//		}
	}
	static def generateLeftSubAtomicExpression(LeftSubAtomicExpression exp) {

	}
	static def generateVariableExpCS(VariableExpCS va){
		switch(va.symbol){
			case "Now" : "time.Now()"
			default:  va.symbol
		}
	}
	static def generateStandardOperationExpCS(StandardOperationExpCS exp) {
		switch(exp.predefinedop){
			
		}
	}
	static def generatePredefineOp(PredefineOp op) {
		
	}
}