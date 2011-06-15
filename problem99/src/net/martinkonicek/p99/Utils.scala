package net.martinkonicek.p99

import scala.collection.mutable.ListBuffer
 
// Cannot use pimp-my-library?
// http://stackoverflow.com/questions/5410846/how-do-i-apply-the-pimp-my-library-pattern-to-scala-collections
object Utils {
	//implicit def iterableExtensions[T](xs : Iterable[T]) = new IterableExtensions(xs)
  
	/** Splits a collection into continuous segments separated by 
	 *  continuous segments of separators.
	 *  {{{
  	 *  scala> splitSegments(List(0, 1, 2, 0, 0, 6, 7, 8), 0)
  	 *  List(List(1, 2), List(6, 7, 8))
  	 *  }}}
	 */
	def splitSegments[T](xs: Iterable[T], separator: T): List[List[T]] = {
		val splits = new ListBuffer[List[T]]
		val iter = xs.iterator
		while (iter.hasNext) {
			val cLine = iter.dropWhile(_ == separator).takeWhile(_ != separator).toList
			if (cLine.length > 0) splits += cLine
		}
		splits.toList
	}
}
 
//class IterableExtensions[T](xs: Iterable[T]) {
