package net.martinkonicek.p99
import scala.collection.mutable.ArrayBuffer

import scala.io._

object Loader {  
	def loadFromFile(fileName:String) {
		val lines = Source.fromFile(fileName).getLines
		val words = lines.takeWhile{ _ != "" }.toList
		val crosswordLines = readPlan(lines)
		/* dream syntax:
		 * with file(fileName).getLines {
			val words = takeWhile { != "" }
			val plan = readPlan
		}*/
		println("words: " + (words mkString "\n"))
		println("plan: " + plan)
	}
	
	//     .
	// .......
	//  .    .
	// ...  ..
	
	def readPlan(input:Iterator[String]):ArrayBuffer[Line] = {
	};