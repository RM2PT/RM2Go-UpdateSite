package com.rm2pt.generator.golangclassgenerate.serviceGen

import net.mydreamy.requirementmodel.rEMODEL.Service
import net.mydreamy.requirementmodel.rEMODEL.Contract
import java.util.Collection
import java.util.Map
import java.util.Set
import java.util.HashSet
import java.util.HashMap
import net.mydreamy.requirementmodel.rEMODEL.TypeCS
import java.util.List
import java.util.ArrayList
import com.rm2pt.generator.golangclassgenerate.Tool

class ServiceGenerator {
	Service service;
	
	List<ContractGenerator> contractGens = new ArrayList<ContractGenerator>();
	
	new (Service service, Map<String, Contract> contractMap){
		this.service = service;	
		
		
		// 先分析得到该Service的tempproperty的map
		for(tp : service.temp_property){
			tempProperties.add(tp.name, tp.type);
		}
		
		for(op : service.operation){
			contractGens.add(new ContractGenerator(contractMap.get(op.name), this))
		}
	}
	
	def getName(){
		return service.name;
	}
	def getTempProperties(){
		return tempProperties;
	}
	
	VariableDomain tempProperties = new VariableDomain();
	
	def generate(){
		if(service.name.matches(".*System")){
			'''
			package serviceGen
			
			import (
				"Auto/entity"
				"Auto/entityRepo"
				"time"
			)
			
			var «service.name»Instance «service.name»
			
			«FOR attr : service.temp_property»
			var «attr.name»  entity.«Tool.compileGoTypeName(attr.type)»
			«ENDFOR»
			
«««			«FOR op : service.operation»
«««			«ContractGenerator.generateSystem(contractMap.get(op.name))»
«««			«ENDFOR»
			'''
		}else{
			'''
			package serviceGen
			
			import (
				"Auto/entity"
				"Auto/entityRepo"
				"time"
			)
			
			var «service.name»Instance «service.name»
			
			type «service.name» struct {
				«FOR attr : service.temp_property»
				«attr.name»  entity.«Tool.compileGoTypeName(attr.type)»
				«ENDFOR»
			}
			
			«FOR gen : contractGens»
			«gen.generate()»
			«ENDFOR»
			'''
		}
		
	}
}