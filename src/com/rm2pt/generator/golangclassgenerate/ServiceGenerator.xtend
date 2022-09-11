package com.rm2pt.generator.golangclassgenerate

import net.mydreamy.requirementmodel.rEMODEL.Service
import net.mydreamy.requirementmodel.rEMODEL.Contract
import java.util.Collection
import java.util.Map
import java.util.Set
import java.util.HashSet


class ServiceGenerator {
	public static Set<String> tempPropertySet = new HashSet<String>();
	static def generate(Service service, Map<String, Contract> contractMap){
		tempPropertySet = new HashSet<String>();
		// 先分析得到该Service的tempproperty的map
		for(tp : service.temp_property){
			tempPropertySet.add(tp.name);
		}
		if(service.name.matches(".*System")){
			'''
			package serviceGen
			
			import (
				"Auto/entity"
				"Auto/entityRepo"
				"time"
			)
			
			«FOR attr : service.temp_property»
			var «attr.name»  entity.«Tool.compileGoTypeName(attr.type)»
			«ENDFOR»
			
			«FOR op : service.operation»
			«ContractGenerator.generateSystem(contractMap.get(op.name))»
			«ENDFOR»
			'''
		}else{
			'''
			package serviceGen
			
			import (
				"Auto/entity"
				"Auto/entityRepo"
				"time"
			)
			
			
			type «service.name» struct {
				«FOR attr : service.temp_property»
				«attr.name»  entity.«Tool.compileGoTypeName(attr.type)»
				«ENDFOR»
			}
			
			«FOR op : service.operation»
			«ContractGenerator.generate(contractMap.get(op.name), service.name)»
			«ENDFOR»
			'''
		}
		
	}
}