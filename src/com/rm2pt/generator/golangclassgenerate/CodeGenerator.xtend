/*
 * RM2PT Generator Runtime
 * generated by RM2PT v1.3.0
 */		
package com.rm2pt.generator.golangclassgenerate

import net.mydreamy.requirementmodel.rEMODEL.Entity
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import java.util.ArrayList
import net.mydreamy.requirementmodel.rEMODEL.Contract
import net.mydreamy.requirementmodel.rEMODEL.Service
import java.util.HashMap
import com.rm2pt.generator.golangclassgenerate.serviceGen.ServiceGenerator
import com.rm2pt.generator.golangclassgenerate.serviceGen.OperationDomain
import java.util.TreeMap
import java.util.List

class CodeGenerator extends AbstractGenerator {

	
	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		System.out.println("hello, goen!!!");
		var entities = new ArrayList<Entity>();
//		 这个Iterable本身不知道为什么只能遍历一次（调用一次for），所以将里面的元素进行转移
		var ite = resource.allContents.toIterable.filter(typeof(Entity));
		for(e : ite){
			System.out.println(e);
			entities.add(e);
		}
		
		System.out.println("hello, goen!!!");
		var zEntities = ZEntityFactory.generateZEntities(entities);
		
		fsa.generateFile("Auto/sql/schema.sql", SQLGenerator.generate(zEntities));
		for(e : zEntities){
			fsa.generateFile("Auto/entity/" + e.entityName.initialLow + ".go", EntityGenerator.generate(e));
		}
		fsa.generateFile("Auto/entity/init.go", EntityGenerator.generateInit(zEntities))
		
		var services = new ArrayList<Service>();
		var its = resource.allContents.toIterable.filter(typeof(Service));
		for(e : its){
			System.out.println(e);
			services.add(e);
		}
		var operationDomain = new OperationDomain(services);
		
		var contractMap = new HashMap<String, Contract>();
		for(contract : resource.allContents.toIterable.filter(typeof(Contract))){
	 		contractMap.put(contract.op.name, contract);
		}
		for(service : services){
			var serviceGen = new ServiceGenerator(service, contractMap, operationDomain)
	 		fsa.generateFile("Auto/serviceGen/" + service.name + ".go", serviceGen.generate());	
		}
		var serviceContractMap = new HashMap<Service, List<Contract>>();
		for(contract : resource.allContents.toIterable.filter(typeof(Contract))){
	 		if(!serviceContractMap.containsKey(contract.service)){
	 			serviceContractMap.put(contract.service, new ArrayList());
	 		}
	 		serviceContractMap.get(contract.service).add(contract);
		}
		fsa.generateFile("Auto/serviceGen/server.go", new ApiGenerator(serviceContractMap).generate());	
//		
		
	}

	
}

