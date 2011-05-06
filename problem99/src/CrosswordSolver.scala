
object CrosswordSolver extends App {
	  // for each line, list candidate words
    // by eliminating words which clearly cannot match
    // ..... -> HELLO , but there is no 2-char word starting with L
    //    .
  
    // fail fast:
    // always prefer lines with least candidates.
    // when 2 lines same number of candidates
    // then every empty intersection is score +1(, every filled intersection is +4)
  
    // main algorithm: take the line with the highest score,
    // try place all possible words and recurse.
    // if no word possible, backtrack
  
    // placing a word changes scores -
    // recalculate candidates for affected lines, 
    //     if only one then +100, if zero, backtrack
    // 
    // -> maintain a heap of current ordering,
    // update it when placing a word, and when backtracking
  
    // representation of lines: 
    // 1. lines are Lists of Cell objects. some cells are shared so 
    //    that filling a cell in one line also fills it in the other line
    // 2. lines are just Lists of chars + info about intersections
    //    so that when we write a char, we must also update it in
    //    all the intersecting lines
    // (can't tell what will be faster, maybe 1.)
    // (to be able to write the output we have to keep (x,y) for every cell)
  
    // lookup of candidate words:
    // build a matrix (not map) - (words which have char c at position i)
    // (wrap it into an object to hide the implementation)
    // (maybe convert all chars to ints in advance).
    // a helper method will be useful which takes a line (partially filled)
    // and returns all possible candidate words
    
    // loading the input:
    // go line by line. horizontal segments are new lines,
    // vertical segments are built by remembering "possible vertical"
    // for every dot from the previous line.
}
