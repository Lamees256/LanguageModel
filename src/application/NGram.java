package application;

import java.util.Arrays;

public class NGram {

	private String[] array;

	public NGram(String[] array) {
		this.array = array;
	}

	// remove the elements in the array that contain **
	public void printNgram(String[] array) {

	}

	public String getFirstWord() {
		return array[0];
	}

//	public String[] getBi() {
//		String[] arr = new String[] { array[0].toString(), array[1].toString() };
//		return arr;
//	}
//
//	// given a trigram make it into a bigram

	public NGram triToBi(NGram ngram) {
		NGram n = new NGram(ngram.array);
		n.array = Arrays.copyOf(n.array, n.array.length - 1);
		return n;

	}

	public NGram quadToTri(NGram ngram) {
		NGram n = new NGram(ngram.array);
		n.array = Arrays.copyOf(n.array, n.array.length - 1);
		return n;
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	@Override
	public String toString() {
		return Arrays.toString(array);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(array);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NGram other = (NGram) obj;
		return Arrays.equals(array, other.array);
	}

}
