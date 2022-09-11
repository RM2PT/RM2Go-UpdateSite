package com.rm2pt.generator.golangclassgenerate.serviceGen

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
import net.mydreamy.requirementmodel.rEMODEL.FeatureCallExpCS
import net.mydreamy.requirementmodel.rEMODEL.CallExpCS
import java.util.List
import net.mydreamy.requirementmodel.rEMODEL.NestedExpCS
import org.eclipse.emf.ecore.EObject
import net.mydreamy.requirementmodel.rEMODEL.StandardNoneParameterOperation
import net.mydreamy.requirementmodel.rEMODEL.StandardDateOperation
import net.mydreamy.requirementmodel.rEMODEL.Contract
import net.mydreamy.requirementmodel.rEMODEL.Postcondition
import net.mydreamy.requirementmodel.rEMODEL.LetExpCS
import java.util.HashMap
import java.util.Map
import net.mydreamy.requirementmodel.rEMODEL.EntityType
import net.mydreamy.requirementmodel.rEMODEL.TypeCS
import net.mydreamy.requirementmodel.rEMODEL.CollectionTypeCS
import net.mydreamy.requirementmodel.rEMODEL.Entity
import net.mydreamy.requirementmodel.rEMODEL.Attribute
import net.mydreamy.requirementmodel.rEMODEL.Service
import net.mydreamy.requirementmodel.rEMODEL.OCLExpressionCS
import com.rm2pt.generator.golangclassgenerate.Tool

class ContractGenerator {
	Contract contract;
	ServiceGenerator serviceGen;
	new (Contract contract, ServiceGenerator service){
		this.contract = contract;
		serviceGen = service;
	}
	
	// 该成员在generateDeclaration中会更新
	Map<String, TypeCS> variableMap = new HashMap<String, TypeCS>();
	
	def getName(){
		return contract.op.name
	}
	
	def generate(){
		'''
		func (p *«serviceGen.name») «name» («FOR para : contract.op.parameter SEPARATOR ','»«para.name» «Common.generateType(para.type)» «ENDFOR») (result «Common.generateType(contract.op.returnType)», retErr error){
			defer func() {
				if err := entityRepo.Saver.Save(); err != nil {
					retErr = NewErrPostCondition(err)
					return
				}
			}()
			
			«generateDefinition()»
			«generatePrecondition()»
			«generatePostcondition()»
			
			return
		}
		'''
	}
	def generateDefinition(){
		if(contract.def === null){
			return ""
		}
		var ret = 
		'''
		//definition
		'''
//		System.out.println("defi is" + defi);
		for(vari : contract.def.variable){
			ret += 
			'''
			«generateDeclaration(vari)»
			'''
		}
		return ret
	}
	def generatePrecondition(){
		if(contract.pre === null){
			return ""
		}
		'''
		// precondition
		if!(
			«generateValue(contract.pre.oclexp)»){
			retErr = ErrPreConditionUnsatisfied
			return
		}
		'''
	}
	def generatePostcondition(){
		if(contract.post === null){
			return ""
		}
		switch(exp : contract.post.oclexp){
			LogicFormulaExpCS: {
				generateAction(exp)
			}
			LetExpCS: {
				'''
				«FOR v : exp.variable»
				«generateDeclaration(v)»
				«ENDFOR»
				«generateAction(exp.inExpression)»
				'''
			}
		}
	}
	def String generateAction(EObject exp){
		switch(exp){
			LogicFormulaExpCS: {
				// 必须全是and
				for(co : exp.connector){
					if(!co.equals("and")){
//					if(co !== "and"){
						return "goenUndefined!"
					}
				}
				'''
				«FOR atomic : exp.atomicexp»
				«generateAction(atomic)»
				«ENDFOR»
				'''
			}
			AtomicExpression: {
				if(exp.infixop === null){
					generateAction(exp.leftside)
					// include
					// allinstance and multiass
					// oclisnew
					// isequal	
				}else if(exp.infixop.equals("=")){
					switch(left : exp.leftside){
						// item.barcode
						PropertyCallExpCS:{
							if(!left.name.symbol.equals("self")){
								var rightTotal = generateValue(exp.rightside);
								if(exp.op !== null){
									rightTotal += exp.op + generateValue(exp.exp);
								}
								'''«generateValue(left.name)».Set«left.attribute»(«rightTotal»)'''
							}else{
								'''«generateValue(left.attribute)» = «generateValue(exp.rightside)»'''
							}
							
						}
						// currentsale
						default: {
							'''«generateValue(left)» = «generateValue(exp.rightside)»'''
						}
					}
				}else {
					"goenUndefined"
				}
			}
			StandardOperationExpCS : {
				switch(predefinedop : exp.predefinedop){
					StandardNoneParameterOperation :{
						switch(predefinedop.name){
							case "oclIsNew()" : '''«generateValue(exp.object)» = entity.«findVariableType(exp.object.symbol)»Repo.New()'''
						}
					}
					StandardDateOperation:{
						switch(predefinedop.name){
							case "isEqual" :   '''«generateValue(exp.object)».Set«generateValue(exp.property)»(«generateValue(predefinedop.object)»)'''
						}
					}
					default : "goenUndefined!"
				}
			}
			StandardNavigationCallExpCS : {
				if(exp.propertycall !== null && exp.classifercall === null){
					var property = exp.propertycall
					switch(exp.standardOP.name){
						case "includes": 
							generateValue(property.name) + ".Add" + property.attribute + "(" + generateValue(exp.standardOP.object) +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else if(exp.propertycall === null && exp.classifercall !== null && exp.classifercall.op == "allInstance()"){
					var classifer = exp.classifercall
					switch(exp.standardOP.name){
						case "includes": 
							"entity." + classifer.entity + "Repo.AddInAllInstance" + "(" + exp.standardOP.object +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else {
					"goenUndefined!"
				}
			}
			
			
		}
	}
	
	def String generateValue(EObject exp){
		switch(exp){
			// 只有left
			NestedExpCS: '''(«generateValue(exp.nestedExpression)»)''' 
			LogicFormulaExpCS: {
				if(exp.connector === null){
					generateValue(exp.atomicexp.get(0))
				}else{
					var ret = "";
					for(var i = 0; i < exp.connector.length(); i++){
						var connector = switch(exp.connector.get(i)){
							case "and" : "&&"
							case "or" : "||"
							default : "goenUndefined"
						}
						ret += 
							'''«generateValue(exp.atomicexp.get(i))» «connector»'''
					}
					ret += 
					'''«generateValue(exp.atomicexp.last())»'''
					
					return ret
				}
			}
			AtomicExpression: {
				if(exp.infixop === null){
					generateValue(exp.leftside)
				}else{
					var op = switch(exp.infixop){
						case "=" : "=="
						default: exp.infixop
					}
					'''«generateValue(exp.leftside)» «op» «generateValue(exp.rightside)»'''
				}
			}
			VariableExpCS : generateValue(exp.symbol)
			PropertyCallExpCS : '''«generateValue(exp.name)».Get«exp.attribute»()'''
			PrimitiveLiteralExpCS : exp.symbol
			StandardOperationExpCS :{
				switch(exp.predefinedop.name){
					case "oclIsUndefined()" : '''(«generateValue(exp.object)» == nil)'''
					case "sum()" : '''util.Sum(«generateValue(exp.object)»)'''
					default : "goenUndefined!"
				}
			}
			IteratorExpCS: {
				switch(exp.iterator){
					case "any" : 
						switch(call : exp.objectCall){
						ClassiferCallExpCS: {
							switch(subexp : exp.exp){
							LogicFormulaExpCS: 
								switch(atomicexp : subexp.atomicexp.get(0)){
								AtomicExpression:
									if(atomicexp.infixop == "=" && atomicexp.leftside instanceof PropertyCallExpCS){
										'''«generateRepo(call.entity)».GetFromAllInstanceBy("«Tool.camelToUnderScore((atomicexp.leftside as PropertyCallExpCS).attribute)»", «generateValue(atomicexp.rightside)»)'''
									}
									
								}
							
							}
						}
						default : "goenUndefined!"
						}
					case "collect" :{
						// 先算所有参数
						var params = ""
						for(vari : exp.varibles){
							params += generateFuncParam(vari)
						}
						'''util.Collect(«generateValue(exp.simpleCall)», func(«params») «findExpType(exp.exp)» {return «generateValue(exp.exp)»} )'''
					}
				
				}
			}
		}
	}
	def String generateValue(String symbol){
		switch(symbol){
			case "self" : ""
			case "Now" : "time.Now()"
			default:  {
				// 如果是TempProperty则需要添加p.前缀
				if(serviceGen.tempProperties.get(symbol) !== null){
					"p." + symbol
				}else{
					symbol
				}
			}
		}
	}
	
	def String generateDeclaration(VariableDeclarationCS dec){
		'''var «dec.name» «generateType(dec.type)» «IF dec.initExpression!==null» = «generateValue(dec.initExpression)»«ENDIF»'''	
	}

	def TypeCS findVariableType(String symbol){
		
	}
	def TypeCS findExpType(EObject exp){
		
	}
	def String generateType(TypeCS type){
		
	}
	def generateRepo(TypeCS type){
		switch(type){
			EntityType : "entity." + Tool.compileGoTypeName(type) + "Repo"
			default : "goenUndefined"
		}
	}
	def generateRepo(String entityName){
		"entity." + entityName + "Repo"	
	}
	def generateFuncParam(VariableDeclarationCS dec) {
		'''«dec.name» «generateType(dec.type)»'''
	}
	
	
	
	
	static def generate(Contract contract, String serviceName) {
//		// 生成前要扫描所有的变量并存储实体类型名称
//		entityVariableMap = new HashMap<String, String>();
//		if(contract.post.oclexp instanceof LetExpCS){
//			for(vari : (contract.post.oclexp as LetExpCS).variable){
//				if(vari.type instanceof EntityType){
//					entityVariableMap.put(vari.name, (vari.type as EntityType).entity.name);
//				}
//			}
//		}
		
		'''
		func (p *«serviceName») «contract.op.name»(«FOR para : contract.op.parameter SEPARATOR ','»«para.name» «Tool.compileGoTypeName(para.type)» «ENDFOR») (result «Tool.compileGoTypeName(contract.op.returnType)», retErr error){
			defer func() {
				if err := entityRepo.Saver.Save(); err != nil {
					retErr = NewErrPostCondition(err)
					return
				}
			}()
			
			«IF contract.def !== null»
			//definition
			«generateDefinition(contract.def)»
			«ENDIF»
			
			// precondition
			if!(
				«generatePrecondition(contract.pre)»){
				retErr = ErrPreConditionUnsatisfied
				return
			}
			
			// postcondition
			«generatePostcondition(contract.post)»
			
			return
		}
		'''
		

	}
	static def generateSystem(Contract contract){
		'''
		func «contract.op.name»(«FOR para : contract.op.parameter SEPARATOR ','»«para.name» «Tool.compileGoTypeName(para.type)» «ENDFOR») (result «Tool.compileGoTypeName(contract.op.returnType)», retErr error){
			defer func() {
				if err := entityRepo.Saver.Save(); err != nil {
					retErr = NewErrPostCondition(err)
					return
				}
			}()
			
			«IF contract.def !== null»
			//definition
			«generateDefinition(contract.def)»
			«ENDIF»
			
			// precondition
			if!(
				«generatePrecondition(contract.pre)»){
				retErr = ErrPreConditionUnsatisfied
				return
			}
			
			// postcondition
			«generatePostcondition(contract.post)»
			
			return
		}
		'''
	}
	static def generateDefinition(Definition defi){
		var ret = ''''''
		System.out.println("defi is" + defi);
		for(vari : defi.variable){
			switch(exps : vari.initExpression){
				LogicFormulaExpCS: ret += 
				'''
				«Common.generateDeclaration(vari)» = «Common.generateValue(exps.atomicexp.get(0))»
				'''
			}
			
		}
		return ret
	}
	static def generatePrecondition(Precondition pre){	
		switch oclexp : pre.oclexp{
			LogicFormulaExpCS: 
				new ZPreExpression(oclexp).generate
			BooleanLiteralExpCS: Common.generateValue(oclexp)
			default : "goenUndefined!"
		}
		
	}
	static def generatePostcondition(Postcondition post){
		switch(exp : post.oclexp){
			LogicFormulaExpCS: {
				'''
				«FOR e : exp.atomicexp»
				«Common.generateAction(e)»
				«ENDFOR»
				'''
			}
			LetExpCS: {
				'''
				«FOR v : exp.variable»
				«Common.generateDeclaration(v)»
				«ENDFOR»
				«FOR e : (exp.inExpression as LogicFormulaExpCS).atomicexp»
				«Common.generateAction(e)»
				«ENDFOR»
				'''
			}
		}
	}
	

	
//	static def generateDefinition(Definition defi){
//		'''
//		«FOR vari : defi.variable»
//		«IF vari instanceof VariableDeclarationCS && vari.initExpression instanceof LogicFormulaExpCS && (vari.initExpression as LogicFormulaExpCS).atomicexp.length() == 1»
//		var «vari.name» «Tool.compileGoTypeName(vari.type)» = «generatePreAtomicExpression((vari.initExpression as LogicFormulaExpCS).atomicexp.get(0) as AtomicExpression)»
//		«ELSE»
//		goenUndefined!
//		«ENDIF»
//		«ENDFOR»
//		'''
//	}
	
}

class ZPreExpression {
	public List<ZPreExpression> subexp = new ArrayList<ZPreExpression>();
	public List<String> connectors = new ArrayList<String>();
	public String exp;
	
	def String generate(){
		if(subexp.length() == 0){
			exp
		}else{
			var ret = "";
			for(var i = 0; i < connectors.length(); i++){
				ret += '''
						(«subexp.get(i).generate()») «connectors.get(i)»
						'''
			}
			ret += subexp.get(subexp.length()-1).generate()
			return ret
		}
	}
	new (LogicFormulaExpCS exp){
		for(e : exp.atomicexp){
			switch(e){
				AtomicExpression : 
					subexp.add(new ZPreExpression(e))				
				NestedExpCS: {
					switch(nested : e.nestedExpression){
						LogicFormulaExpCS: subexp.add(new ZPreExpression(nested))
					}
				}
			}
		}
		
		for(c : exp.connector){
			switch(c) {
				case "and" : connectors.add("&&")
				case "or" : connectors.add("||")
			}
		}
		
	}
	private new (AtomicExpression exp){
		var op = switch(exp.infixop){
			case "=" : "=="
			default: exp.infixop
		}
		this.exp = '''«Common.generateValue(exp.leftside)» «op» «Common.generateValue(exp.rightside)»'''
	}
	
	
}

class Common { 
	static def String generateType(TypeCS type){
		switch(type){
			EntityType: "entity." + Tool.compileGoTypeName(type)
			CollectionTypeCS : {
				switch(type.name.name){
					case "Set":	"[]" + generateType(type.type)
					default : "goenUndefined"
				}
			}
			default : Tool.compileGoTypeName(type)
		}
	}
	
	static def String getExpType(EObject exp){
		switch(exp){
			LogicFormulaExpCS: {
				if(exp.connector !== null){
					return "bool"
				}else{
					getExpType(exp.atomicexp.get(0))
				}
			}
			AtomicExpression : {
				if(exp.infixop === null){
					getExpType(exp.leftside)
				}
			}
			PropertyCallExpCS: {
				var objectType = getSymbolType(exp.name.symbol)
				switch(objectType){
					EntityType: {
						generateType(getAttributeType(objectType, exp.attribute))
					}
				}
				
			}
		}
	}
	static def TypeCS getAttributeType(EntityType type, String attribute){
		for(attr : type.entity.attributes){
			switch(attr){
				Attribute: if(attr.name.equals(attribute)){
					generateValue(attr.type)
				}
			}
		}
	}
	static def TypeCS getSymbolType(String symbol){
		
	}
	
	static def generateRepo(TypeCS type){
		"entity." + Tool.compileGoTypeName(type) + "Repo"
	}
	static def generateRepo(String entityName){
		"entity." + Tool.compileGoTypeName(entityName) + "Repo"
	}
	
	static def generateSetter(PropertyCallExpCS exp1, String exp2){
		'''«generateValue(exp1.name)».Set«exp1.attribute»(«exp2»)'''
	}
	
	static def CharSequence generateAction(EObject exp){
		switch(exp){
			AtomicExpression: {
				if(exp.infixop === null){
					generateAction(exp.leftside)
					// include
					// allinstance and multiass
					// oclisnew
					// isequal	
				}else if(exp.infixop == "="){
					switch(left : exp.leftside){
						// item.barcode
						PropertyCallExpCS:{
							if(!left.name.symbol.equals("self")){
								var rightTotal = generateValue(exp.rightside);
								if(exp.op !== null){
									rightTotal += exp.op + generateValue(exp.exp);
								}
								generateSetter(left, rightTotal);
							}else{
								'''«generateValue(left.attribute)» = «generateValue(exp.rightside)»'''
							}
							
						}
						// currentsale
						default: {
							'''«generateValue(left)» = «generateValue(exp.rightside)»'''
						}
					}
				}
			}
			StandardOperationExpCS : {
				switch(predefinedop : exp.predefinedop){
					StandardNoneParameterOperation :{
						switch(predefinedop.name){
							case "oclIsNew()" : '''«generateValue(exp.object)» = entity.«findVariableType(exp.object)»Repo.New()'''
						}
					}
					StandardDateOperation:{
						switch(predefinedop.name){
							case "isEqual" :   '''«generateValue(exp.object)».Set«generateValue(exp.property)»(«generateValue(predefinedop.object)»)'''
						}
					}
					default : "goenUndefined!"
				}
			}
			StandardNavigationCallExpCS : {
				if(exp.propertycall !== null && exp.classifercall === null){
					var property = exp.propertycall
					switch(exp.standardOP.name){
						case "includes": 
							generateValue(property.name) + ".Add" + property.attribute + "(" + generateValue(exp.standardOP.object) +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else if(exp.propertycall === null && exp.classifercall !== null && exp.classifercall.op == "allInstance()"){
					var classifer = exp.classifercall
					switch(exp.standardOP.name){
						case "includes": 
							"entity." + classifer.entity + "Repo.AddInAllInstance" + "(" + exp.standardOP.object +")"
						case "excludes" : "goenUndefined!"
						default : "goenUndefined!"
					}
				}else {
					"goenUndefined!"
				}
			}
			
			
		}
	}
	
	
	static def String findVariableType(VariableExpCS exp){
//		return ContractGenerator.entityVariableMap.get(exp.symbol);
	}
	
	static def String generateValue(EObject exp){
		switch(exp){
			// 只有left
			LogicFormulaExpCS: {
				if(exp.connector === null){
					generateValue(exp.atomicexp.get(0))
				}else{
					
				}
			}
			AtomicExpression: generateValue(exp.leftside)
			VariableExpCS : generateValue(exp.symbol)
			PropertyCallExpCS : '''«generateValue(exp.name)».Get«exp.attribute»()'''
			PrimitiveLiteralExpCS : exp.symbol
			StandardOperationExpCS :{
				switch(exp.predefinedop.name){
					case "oclIsUndefined()" : '''(«generateValue(exp.object)» == nil)'''
					case "sum()" : '''util.Sum(«generateValue(exp.object)»)'''
					default : "goenUndefined!"
				}
			}
			IteratorExpCS: {
				switch(exp.iterator){
					case "any" : 
						switch(call : exp.objectCall){
						ClassiferCallExpCS: {
							switch(subexp : exp.exp){
							LogicFormulaExpCS: 
								switch(atomicexp : subexp.atomicexp.get(0)){
								AtomicExpression:
									if(atomicexp.infixop == "=" && atomicexp.leftside instanceof PropertyCallExpCS){
										'''«generateRepo(call.entity)».GetFromAllInstanceBy("«Tool.camelToUnderScore((atomicexp.leftside as PropertyCallExpCS).attribute)»", «generateValue(atomicexp.rightside)»)'''
									}
									
								}
							
							}
						}
						default : "goenUndefined!"
						}
					case "collect" :{
						// 先算所有参数
						var params = ""
						for(vari : exp.varibles){
							params += generateFuncParam(vari)
						}
						'''util.Collect(«generateValue(exp.simpleCall)», func(«params») «getExpType(exp.exp)» {return «generateValue(exp.exp)»} )'''
					}
				
				}
			}
		}
	}
	static def String generateValue(String symbol){
		switch(symbol){
			case "self" : ""
			case "Now" : "time.Now()"
			default:  ""
			
		}
	}
	static def generateFuncParam(EObject e){
		switch(e){
			VariableDeclarationCS:'''«e.name» «generateType(e.type)»'''
			default : "goenUndefined"
		}
	}
	static def generateDeclaration(EObject v){
		switch(v){
			VariableDeclarationCS:'''var «v.name» «generateType(v.type)»'''
			default : "goenUndefined"
		}
		
	}
	
	
	
	
}



