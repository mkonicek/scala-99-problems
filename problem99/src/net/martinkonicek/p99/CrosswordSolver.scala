package net.martinkonicek.p99

import scala.io.Source

object CrosswordSolver {
  def main(args : Array[String]) = {
		// for each line, list candidate words
		// by eliminating words which clearly cannot match
		// ..... -> HELLO , but there is no 2-char word starting with L
		//    .
		val crossword = Crossword.loadFromFile("input\\p99a.dat")
		crossword.print
		//val solver = new Solver(crossword)
		//solver.solve()	
		
  }
}
