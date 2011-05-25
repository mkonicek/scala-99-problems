package net.martinkonicek.p99

object CrosswordSolver {
  def main(args : Array[String]) = {
		// for each line, list candidate words
		// by eliminating words which clearly cannot match
		// ..... -> HELLO , but there is no 2-char word starting with L
		//    .
		val crossword = Loader.loadFromFile("input\\p99a.dat")
		val solver = new Solver(crossword)
		solver.solve()	
		
  }
}
