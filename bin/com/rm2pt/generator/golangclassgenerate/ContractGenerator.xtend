package com.rm2pt.generator.golangclassgenerate

import net.mydreamy.requirementmodel.rEMODEL.AtomicExpression
import net.mydreamy.requirementmodel.rEMODEL.LeftSubAtomicExpression
import net.mydreamy.requirementmodel.rEMODEL.StandardOperationExpCS
import net.mydreamy.requirementmodel.rEMODEL.VariableExpCS
import net.mydreamy.requirementmodel.rEMODEL.PredefineOp

class ContractGenerator {
	static def generateAtomicExpression(AtomicExpression exp){
		System.out.println("in atomic expression" + exp);
		System.out.println("exp.leftside:" + exp.leftside);
		System.out.println("exp.rightside" + exp.rightside);
		System.out.println("exp.num" + exp.num);
		System.out.println("exp.exp:" + exp.exp);
		if(exp.infixop === null) {
			
		}
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