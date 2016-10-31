import java.util.*;

public class FDChecker {

	/**
	 * Checks whether a decomposition of a table is dependency preserving under
	 * the set of functional dependencies fds
	 * 
	 * @param t1
	 * 		one of the two tables of the decomposition
	 * @param t2
	 *      the second table of the decomposition
	 * @param fds
	 *      a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is dependency preserving, false otherwise
	 **/
	public static boolean checkDepPres(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		// your code here
		// a decomposition is dependency preserving, if local functional
		// dependencies are
		// sufficient to enforce the global properties
		// To check a particular functional dependency a -> b is preserved,
		// you can run the following algorithm

		for (FunctionalDependency fd : fds) {
			// result = a
			AttributeSet result = new AttributeSet(fd.left);
			// while result has not stabilized
			AttributeSet pre_result = new AttributeSet();
			while (!pre_result.equals(result)) {
				pre_result = result;
				// for each table in the decomposition
				// t = result intersect table
				AttributeSet t = new AttributeSet(result);
				t.retainAll(t1);
				// t = closure(t) intersect table
				t = closure(t, fds);
				t.retainAll(t1);
				// result = result union t
				result.addAll(t);

				t = new AttributeSet(result);
				t.retainAll(t2);
				// t = closure(t) intersect table
				t = closure(t, fds);
				t.retainAll(t2);
				// result = result union t
				result.addAll(t);
			}
			if (!result.contains(fd.right))
				return false;

		}
		return true;
	}

	/**
	 * Checks whether a decomposition of a table is lossless under the set of
	 * functional dependencies fds
	 * 
	 * @param t1
	 *      one of the two tables of the decomposition
	 * @param t2
	 *      the second table of the decomposition
	 * @param fds
	 *      a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is lossless, false otherwise
	 **/
	public static boolean checkLossless(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		// your code here
		// Lossless decompositions do not lose information, the natural join is
		// equal to the
		// original table.
		// a decomposition is lossless if the common attributes for a superkey
		// for one of the
		// tables.

		AttributeSet intersection = new AttributeSet(t1); // find intersection
															// of t1 and t2
		intersection.retainAll(t2);

		AttributeSet intersection_closure = closure(intersection, fds); // find
																		// the
																		// closure
																		// of f

		if (intersection_closure.containsAll(t1)) { // returns true if closure
													// contains t1 or t2
			return true;
		} else if (intersection_closure.containsAll(t2)) {
			return true;
		} else {
			return false;
		}
	}

	// recommended helper method
	// finds the total set of attributes implied by attrs
	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) {
		AttributeSet closure = attrs;
		int size = -1;
		Iterator<FunctionalDependency> iterator;

		while (size != closure.size()) { // repeat while loop until finished
											// going through all attrs: no
											// further change
			iterator = fds.iterator(); // iterate over total FDs
			size = closure.size(); // update size

			while (iterator.hasNext()) {
				FunctionalDependency current = iterator.next();
				if (closure.containsAll(current.left)) { // if there is a match
															// between attrs and
															// the given FDs
					closure.add(current.right); // add RHS value to closure
				}
			}
		}

		return closure;
	}
}
