package net.martinkonicek.p99

import scala.io.Source

object CrosswordSolver {
	def main(args : Array[String]) = {
	  
		val crossword = Crossword.loadFromFile("input\\p99b.dat")
		//crossword.words.foreach(println)
		//crossword.print
		
		val solver = new Solver(crossword, isDebugOutput = true)
		solver.solve match {
			case Some(solution) => {
				solution.print
				verify(solution.crossword)
			}
			case None => println("No solution.")
		}
	}
  
	private def verify(cw: Crossword) = {
		verifyWordsCorrespondence(cw)
		println("Solution verified OK.")
  	}
  
	private def verifyWordsCorrespondence(cw: Crossword) = {
		val wordsUsed = cw.segments.map(_.word)
	  	val nonExistingWordsUsed = wordsUsed.filterNot(cw.words.contains(_))
		if (nonExistingWordsUsed.length > 0) {
	  		println("Used non-existing words!: ")
			nonExistingWordsUsed.map("\t" + _).foreach(println)
	  	}
	  	val wordsNotUsed = cw.words.filterNot(wordsUsed.contains(_))
	  	if (nonExistingWordsUsed.length > 0) {
			println("Some words were not used!: ")
			wordsNotUsed.map("\t" + _).foreach(println)
		}
	}
}
