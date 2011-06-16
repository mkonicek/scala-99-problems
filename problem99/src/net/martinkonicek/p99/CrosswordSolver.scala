package net.martinkonicek.p99

import scala.io.Source

object CrosswordSolver {
  
	def main(args: Array[String]) = {
		val (inputFileName, isDebugOutput) = parseArguments(args)
		val crossword = Crossword.loadFromFile(inputFileName)
		val solver = new Solver(crossword, isDebugOutput)
		solver.solve match {
			case Some(solution) => {
				solution.print
			}
			case None => println("No solution.")
		}
	}
	
	def parseArguments(args: Array[String]) = args match {
		case Array() => {
		  	printUsage
		  	println("Using 'input/p99b.dat'.")
			("input/p99b.dat", false)
		}
		case Array(fileName) => (fileName, false)
		case Array(fileName, "-debug") => (fileName, true)
		case _ => {
		  	printUsage
		  	// args is not empty
			(args(0), false)
		}
	}
	
	def printUsage = println("Usage: crosswordsolver inputFile [-debug]")
}
