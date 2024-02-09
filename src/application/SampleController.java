package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SampleController {

	@FXML
	private Button checkBt;
	@FXML
	private TextField inputTf, percentTf;

	private static LanguageModel model;
	static Double p;

	public static void chooseFile() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("txt files", "*.txt"));
		File file = fc.showOpenDialog(null);
		try {
			Scanner scanner;
			scanner = new Scanner(file);
			readFile(file);
		} catch (FileNotFoundException e1) {
			System.out.println();
		} catch (java.lang.NullPointerException e2) {
			System.out.println("No File selected");
		}
	}

	public static void readFile(File file) throws FileNotFoundException {
		// make an arrayList of words that are in the file
		ArrayList<String> wordsInFile = new ArrayList<>();
		if (file.exists()) {
			Scanner in;
			try {
				in = new Scanner(file);
				while (in.hasNext()) {
					String string = in.nextLine().trim();
					System.out.println(string);
					String[] sentences = string.split("\\.|\\?|\\!");
					for (String sentence : sentences) {
						// for every sentence get all the words then do bigram , trigram , and 4-gram
						sentence = sentence.replaceFirst("^ *", "");
//						if (sentence.charAt(0) == ' ') {
//						     sentence = sentence.substring(1);
//						}
//						sentence = "<S>" + sentence + "</s>";
//						System.out.println(sentence);
						String[] words = sentence.split(" ");

						for (int i = 0; i < words.length; i++) {
							words[i].trim();
							if ((!words[i].equals("\n")) || (!words[i].equals(" "))) {
								wordsInFile.add(words[i]);
							}
						}
						wordsInFile.add("**"); // when making the n grams know when to stop so it doesnt start
												// calculating the next sentence as well

					}
				}
			} catch (FileNotFoundException e1) {

			}
		}
		System.out.println(wordsInFile);
		model = new LanguageModel();
		model.setWordsInFile(wordsInFile);
		model.generateUnigrams();
		model.generateBigrams();
		model.generateTrigrams();
		model.generateQuadgrams();
		writeToCSV(wordsInFile);
	}

	public static void writeToCSV(ArrayList<String> wordsInFile) throws FileNotFoundException {
		// write langauge model into cvs -> token , token counts, token probability
		HashMap<String, Integer> uniMap = model.getUnigramMap();
		HashMap<String, Double> uniProb = model.getUnigramProb();

		HashMap<NGram, Integer> biMap = model.getBigramMap();
		HashMap<NGram, Double> biProb = model.getBigramProb();

		HashMap<NGram, Integer> triMap = model.getTrigramMap();
		HashMap<NGram, Double> triProb = model.getTrigramProb();

		HashMap<NGram, Integer> quadMap = model.getQuadgramMap();
		HashMap<NGram, Double> quadProb = model.getQuadgramProb();

		File csvFile = new File("languageModel.csv");
		PrintWriter out = new PrintWriter(csvFile);

		uniMap.forEach((key, value) -> {
			p = uniProb.get(key);
			out.printf("%s, %d, %f\n", key, value, p);
		});

		biMap.forEach((key, value) -> {
			p = biProb.get(key);
			String str = key.toString().replace(",", "");
			out.printf("%s, %d, %f\n", str, value, p);
		});

		triMap.forEach((key, value) -> {
			p = triProb.get(key);
			String str = key.toString().replace(",", "");
			out.printf("%s, %d, %f\n", str, value, p);
		});

		quadMap.forEach((key, value) -> {
			p = quadProb.get(key);
			String str = key.toString().replace(",", "");
			out.printf("%s, %d, %f\n", str, value, p);
		});

		out.close();
	}

//	public void readCSVFile() {
//		String line = "";
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("languageModel.csv"));
//			while ((line = br.readLine()) != null) {
//				System.out.println(line);
//				String[] tokens = line.split(",");
////				System.out.println(tokens[0]); // -> token
////				System.out.println(tokens[1]); // -> token count
////				System.out.println(tokens[2]); // -> probability of token
//				// the lower the probability of it the more plagiarism score is increased
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void ddetectPlagiarism() {
		Double probSentence;
		Formatter formatter = new Formatter();
		String string = inputTf.getText().trim();
		String[] tokens = string.split(" ");
		System.out.println(tokens.length);
		// calculate probability of this sentence using the model
		if (tokens.length == 1) {
			probSentence = searchCSVFile(string.trim());
			if (probSentence != 0.0) {
				System.out.println(probSentence);
			} else {
				percentTf.setText("0%");
				System.out.println("not found, prob = 0");
			}

		} else if (tokens.length == 2) {
			String str = "[" + tokens[0] + " " + tokens[1] + "]";
			probSentence = searchCSVFile(str.trim());
			if (probSentence != 0) {
				System.out.println(probSentence);
			} else {
				percentTf.setText("0%");
			}

		} else if (tokens.length == 3) {
			String str = "[" + tokens[0] + " " + tokens[1] + " " + tokens[2] + "]";
			probSentence = searchCSVFile(str.trim());
			if (probSentence != 0.0) {
				System.out.println(probSentence);
			} else {
				percentTf.setText("0%");
			}

		} else if (tokens.length == 4) {
			String str = "[" + tokens[0] + " " + tokens[1] + " " + tokens[2] + " " + tokens[3] + "]";
			probSentence = searchCSVFile(str.trim());
			if (probSentence != 0) {
				System.out.println(probSentence);
			} else {
				percentTf.setText("0%");
			}
		} else {
			// make quadgrams out of the string then find prob of each one, multiply them
			// together
			for (int i = 0; i < tokens.length - 3; i++) {
				String one = tokens[i];
				String two = tokens[i + 1];
				String three = tokens[i + 2];
				String four = tokens[i + 3];
			}
		}
	}

	@FXML
	private void detectPlagiarism() {
		Double p1;
		Double score;
		Formatter formatter = new Formatter();
		String string = inputTf.getText().trim();
		// System.out.println(string);
		String[] tokens = string.split(" ");
		System.out.println(tokens.length);
		if (tokens.length == 1) {
			p1 = searchCSVFile(string.trim());
			if (p1 != 0.0) {
				// score = 100 - (p1 * 100);
				score = p1 * 100;
				formatter.format("%.2f", score);
				percentTf.setText(formatter.toString() + "%");
				formatter.close();
			} else {
				percentTf.setText("0%");
			}
		} else if (tokens.length == 2) {
			String str = "[" + tokens[0] + " " + tokens[1] + "]";
			p1 = searchCSVFile(str.trim());
			if (p1 != 0) {
				score = p1 * 100;
				formatter.format("%.2f", score);
				percentTf.setText(formatter.toString() + "%");
			} else {
				percentTf.setText("0%");
			}
		} else if (tokens.length == 3) {
			String str = "[" + tokens[0] + " " + tokens[1] + " " + tokens[2] + "]";
			p1 = searchCSVFile(str.trim());
			if (p1 != 0) {
				score = p1 * 100;
				formatter.format("%.2f", score);
				percentTf.setText(formatter.toString() + "%");
			} else {
				percentTf.setText("0%");
			}
		} else if (tokens.length == 4) {
			String str = "[" + tokens[0] + " " + tokens[1] + " " + tokens[2] + " " + tokens[3] + "]";
			p1 = searchCSVFile(str.trim());
			if (p1 != 0) {
				score = p1 * 100;
				formatter.format("%.2f", score);
				percentTf.setText(formatter.toString() + "%");
			} else {
				percentTf.setText("0%");
			}
		}

//		} else if (string.length() == 2) {
//			// if bigram found in corpus get prob and do eq
//			// else calc prob and then do eq
//		} else if (string.length() == 3) {
//			// if trigram found in corpus do eq
//			// else calc the probability
//		} else if (string.length() == 4) {
//
//		} else {
//			// do markov calculation for prob then put it into eq i made
//		}

	}

	public Double searchCSVFile(String s) {
		String line = "";
		boolean found = false;
		Double prob = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("languageModel.csv"));
			while (((line = br.readLine()) != null) && !found) {
				String[] tokens = line.split(",");
				if (s.equals(tokens[0].trim())) {
					System.out.println("found " + s + "-> probabilty = " + tokens[2]);
					found = true;
					prob = Double.parseDouble(tokens[2]);
					return prob;
				} else {
					prob = 0.0;
				}

//				System.out.println(tokens[0]); // -> token
//				System.out.println(tokens[1]); // -> token count
//				System.out.println(tokens[2]); // -> probability of token
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prob;
	}

//	public static void readFile(File file) {
//		ArrayList<String> uniqueWords = new ArrayList<>();
//		if (file.exists()) {
//			Scanner in;
//			try {
//				in = new Scanner(file);
//				while (in.hasNext()) {
//					String string = in.nextLine().trim();
//					// System.out.println(string);
//					String sentences[] = string.split("\\.|\\?|\\!");
//					for (String sentence : sentences) {
//						String[] words = sentence.split(" ");
//						for (String word : words) {
//							if (!uniqueWords.contains(word)) {
//								uniqueWords.add(word);
//							}
//						}
//					}
//				}
//			} catch (FileNotFoundException e1) {
//
//			}
//		}
//		System.out.println(uniqueWords.toString());
//	}
}
