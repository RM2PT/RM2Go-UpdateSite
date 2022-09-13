package com.rm2pt.generator.golangclassgenerate.serviceGen

import net.mydreamy.requirementmodel.rEMODEL.TypeCS
import java.util.Map
import java.util.HashMap
import net.mydreamy.requirementmodel.rEMODEL.VariableDeclarationCS
import java.util.Collection
import net.mydreamy.requirementmodel.rEMODEL.Service

class VariableDomain {
	Map<String, TypeCS> variableMap = new HashMap<String ,TypeCS>();
	def findType(String symbol){
		return variableMap.get(symbol);
	}
	def add(String varibleName, TypeCS type){
		variableMap.put(varibleName, type);	
	}
	def add(VariableDeclarationCS vari){
		add(vari.name, vari.type)
	}
}
class OperationDomain {
	// first String is contract name , second is service name
	Map<String, String> variableMap = new HashMap<String ,String>();
	def findService(String operationName){
		return variableMap.get(operationName);
	}
	new (Collection<Service> services){
		for(service : services){
			for(operation : service.operation){
				variableMap.put(operation.name, service.name)
			}		
		}
	}
}