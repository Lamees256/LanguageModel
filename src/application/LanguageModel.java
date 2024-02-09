package application;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageModel {

	private ArrayList<String> wordsInFile;
	private HashMap<String, Integer> unigramMap;
	private HashMap<NGram, Integer> bigramMap;
	private HashMap<NGram, Integer> trigramMap;
	private HashMap<NGram, Integer> quadgramMap;
	private HashMap<String, Double> unigramProb;
	private HashMap<NGram, Double> bigramProb;
	private HashMap<NGram, Double> trigramProb;
	private HashMap<NGram, Double> quadgramProb;
	private double prob;

	public LanguageModel() {
		unigramMap = new HashMap<>();
		bigramMap = new HashMap<>();
		trigramMap = new HashMap<>();
		quadgramMap = new HashMap<>();
		unigramProb = new HashMap<>();
		bigramProb = new HashMap<>();
		trigramProb = new HashMap<>();
		quadgramProb = new HashMap<>();
	}

	public void generateUnigrams() {
		ArrayList<String> words = wordsInFile;
		for (int i = 0; i < words.size(); i++) {
			String one = words.get(i);
			if (!unigramMap.containsKey(one)) {
				unigramMap.put(one, 1);
				if (one.equalsIgnoreCase("**") || one.equalsIgnoreCase("")) {
					unigramMap.remove(one);
				}
			} else {
				if (one.equalsIgnoreCase("**") || one.equalsIgnoreCase("")) {
					unigramMap.remove(one);
				}
				Integer f = unigramMap.get(one);
				unigramMap.put(one, f + 1);
			}
		}
		System.out.println("Unigram Map:");
		System.out.println(unigramMap);
		calcUnigramProb();
	}

	public void generateBigrams() {
		ArrayList<String> words = wordsInFile;
		for (int i = 0; i < words.size() - 1; i++) {
			String one = words.get(i);
			String two = words.get(i + 1);
			String[] arr = new String[] { one, two };
			NGram ngram = new NGram(arr);
			if (!bigramMap.containsKey(ngram)) {
				bigramMap.put(ngram, 1);
				if (one.equalsIgnoreCase("**") || two.equalsIgnoreCase("**")) {
					bigramMap.remove(ngram);
				}
			} else {
				if (one.equalsIgnoreCase("**") || two.equalsIgnoreCase("**")) {
					bigramMap.remove(ngram);
				}
				Integer f = bigramMap.get(ngram);
				bigramMap.put(ngram, f + 1);
			}
//			System.out.println(one + " " + two);
		}
		System.out.println("Bigram Map:");
		System.out.println(bigramMap);
		calcBigramProb();
	}

	public void generateTrigrams() {
		ArrayList<String> words = wordsInFile;
		for (int i = 0; i < words.size() - 2; i++) {
			String one = words.get(i);
			String two = words.get(i + 1);
			String three = words.get(i + 2);
			String[] arr = new String[] { one, two, three };
			NGram ngram = new NGram(arr);
			if (!trigramMap.containsKey(ngram)) {
				trigramMap.put(ngram, 1);
				if (one.equalsIgnoreCase("**") || two.equals("**") || three.equals("**")) {
					trigramMap.remove(ngram);
				}
			} else {
				if (one.equalsIgnoreCase("**") || two.equalsIgnoreCase("**") || three.equalsIgnoreCase("**")) {
					trigramMap.remove(ngram);
				}
				Integer f = trigramMap.get(ngram);
				trigramMap.put(ngram, f + 1);
			}
		}
		System.out.println("Trigram Map");
		System.out.println(trigramMap);
		calcTrigramProb();
	}

	public void generateQuadgrams() {
		ArrayList<String> words = wordsInFile;
		for (int i = 0; i < words.size() - 3; i++) {
			String one = words.get(i);
			String two = words.get(i + 1);
			String three = words.get(i + 2);
			String four = words.get(i + 3);
			String[] arr = new String[] { one, two, three, four };
			NGram ngram = new NGram(arr);
			if (!quadgramMap.containsKey(ngram)) {
				quadgramMap.put(ngram, 1);
				if (one.equalsIgnoreCase("**") || two.equalsIgnoreCase("**") || three.equalsIgnoreCase("**")
						|| four.equalsIgnoreCase("**")) {
					quadgramMap.remove(ngram);
				}
			} else {
				if (one.equalsIgnoreCase("**") || two.equalsIgnoreCase("**") || three.equalsIgnoreCase("**")
						|| four.equalsIgnoreCase("**")) {
					quadgramMap.remove(ngram);
				}
				Integer f = quadgramMap.get(ngram);
				quadgramMap.put(ngram, f + 1);
			}
		}
		System.out.println("Quadgram Map: ");
		System.out.println(quadgramMap);
		calcQuadgramProb();
	}

	public void calcUnigramProb() {
		unigramMap.forEach((key, value) -> {
			Double a = Double.valueOf(value);
			Double b = Double.valueOf(unigramMap.size());
			prob = a / b;
			unigramProb.put(key, prob);
		});
	}

	public void calcBigramProb() {
		bigramMap.forEach((key, value) -> {
			Double a = Double.valueOf(bigramMap.get(key));
			Double b = Double.valueOf(unigramMap.get(key.getFirstWord()));
			prob = a / b;
			bigramProb.put(key, prob);
		});
	}

	public void calcTrigramProb() {
		trigramMap.forEach((key, value) -> {
			Double a = Double.valueOf(trigramMap.get(key));
			Double b = Double.valueOf(bigramMap.get(key.triToBi(key)));
			prob = a / b;
			trigramProb.put(key, prob);
//			System.out.println(key.triToBi(key));
//			System.out.println(bigramMap.get(key.triToBi(key)));
		});
	}

	public void calcQuadgramProb() {
		quadgramMap.forEach((key, value) -> {
			Double a = Double.valueOf(quadgramMap.get(key));
			Double b = Double.valueOf(trigramMap.get(key.quadToTri(key)));
			prob = a / b;
			quadgramProb.put(key, prob);
		});

	}

	public void setWordsInFile(ArrayList<String> wordsInFile) {
		this.wordsInFile = wordsInFile;
	}

	public HashMap<String, Integer> getUnigramMap() {
		return unigramMap;
	}

	public HashMap<String, Double> getUnigramProb() {
		return unigramProb;
	}

	public void setUnigramProb(HashMap<String, Double> unigramProb) {
		this.unigramProb = unigramProb;
	}

	public HashMap<NGram, Integer> getBigramMap() {
		return bigramMap;
	}

	public HashMap<NGram, Double> getBigramProb() {
		return bigramProb;
	}

	public HashMap<NGram, Integer> getTrigramMap() {
		return trigramMap;
	}

	public HashMap<NGram, Double> getTrigramProb() {
		return trigramProb;
	}

	public HashMap<NGram, Integer> getQuadgramMap() {
		return quadgramMap;
	}

	public HashMap<NGram, Double> getQuadgramProb() {
		return quadgramProb;
	}

}
