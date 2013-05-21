/*Author: Raghavendra Shruti Rao
 *Date: 28-April-2013 
 *Purpose: Converts data in CSV file to Tuples as needed for 691-Research Topicology Project
 */

package umbc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSVTagReader {

	public static void main(String args[]) {
		CSVReader reader;
		//CSVWriter writer;
		BufferedReader ccsReader;
		BufferedWriter writer;

		try {
			reader = new CSVReader(new FileReader("ACM_meta.csv"));
			//writer = new CSVWriter(new FileWriter("ACM_meta_output.csv"));
			ccsReader = new BufferedReader(new FileReader("ccs.txt"));
			writer = new BufferedWriter(new FileWriter("ACM_meta_output.txt"));

			List<String[]> rowList = reader.readAll();
			
			for (String[] nextLine : rowList) {
			    // nextLine[] is an array of values from the line
				for (int i=0; i<nextLine.length; i++) {
					System.out.println(nextLine[i]);
				}
				
			    // feed in your array (or convert your data to an array)
				String col1 = parseCurlyBraces(nextLine, 1);
				String col2 = parseCurlyBraces(nextLine, 2);
				String col3 = parseCurlyBraces(nextLine, 3);
				List<String> col4 = parseSemicolon(parseCurlyBraces(nextLine, 4));
				List<String> col5 = parseSemicolon(parseCurlyBraces(nextLine, 5));
				List<String> col6 = parseComma(ccsReader.readLine());
				
				if (col4 != null && col5 != null) {
					String[] outputArray = new String[1];
					outputArray[0] = "<" + col2 + ">";
					outputArray[0] += " dcterms:identifier \"" + col1 + "\";";
					outputArray[0] += " dcterms:title \"" + col3 + "\";";
					
					for (String col3Str : col4) {
						outputArray[0] += " dcterms:creator \"" + col3Str + "\";";
					}
					
					for (int j=0; j < col5.size(); j++) {
						outputArray[0] += " dcterms:subject \"" + col5.get(j) + "\"";
						
						if (j == col5.size()-1) {
							if (col6 != null) {
								for (int k=0; k<col6.size(); k++) {
									String ccs = col6.get(k);
									outputArray[0] += "; dcterms:source \"" + ccs + "\"";
								}
							}
							
							outputArray[0] += ".";
						} else {
							outputArray[0] += ";";
						}
					}

					List<String[]> outputArrayList = new ArrayList<String[]>();
					outputArrayList.add(outputArray);
				    
					if (outputArray != null) {
						//writer.writeAll(outputArrayList);
						for (String str : outputArray) {
							writer.write(str + "\n");
						}
					}
				}
			}
			
			reader.close();
			ccsReader.close();
		    writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("CSV File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception occured");
			e.printStackTrace();
		}
	}
	
	private static String parseCurlyBraces(String[] input, int position) {
		if (input == null || position < 1)
			return "";
		
		int sIdx = 0;
		int eIdx = 0;
		int currentPosition = 0;
		String str = "";
		
		for (int i=0; i<input.length; i++) {
			str += input[i];
		}

		while (currentPosition < position) {
			sIdx = str.indexOf("{", sIdx);
			eIdx = str.indexOf("}", sIdx);
			++currentPosition;
			
			if (sIdx == -1)
				return "";

			if (currentPosition == position) {
				return str.substring(sIdx+1, eIdx);
			}
			
			++sIdx;
		}
	
		return "";
	}

	private static List<String> parseSemicolon(String input) {
		List<String> output = new ArrayList<String>();

		if (input == null || input.isEmpty())
			return output;
		
		int sIdx = 0;
		int eIdx = 0;
		String str = input;

		while (sIdx < input.length()) {
			eIdx = str.indexOf(";", sIdx);
			
			if (eIdx == -1) {
				output.add(str.substring(sIdx, input.length()));
				break;
			}

			output.add(str.substring(sIdx, eIdx));

			sIdx = eIdx+1;
		}
	
		return output;
	}

	private static List<String> parseComma(String input) {
		List<String> output = new ArrayList<String>();

		if (input == null || input.isEmpty())
			return output;
		
		int sIdx = 0;
		int eIdx = 0;
		String str = input;

		while (sIdx < input.length()) {
			eIdx = str.indexOf(",", sIdx);
			
			if (eIdx == -1) {
				output.add(str.substring(sIdx, input.length()));
				break;
			}

			output.add(str.substring(sIdx, eIdx));

			sIdx = eIdx+1;
		}
	
		return output;
	}

	private static String[] toStringArray(String str){
		if (str == null || str.isEmpty()) {
			return null;
		}
		
		String[] output = new String[str.length()];
		
		for (int i=0; i<str.length();i++) {
			output[i] = str.substring(i, i+1);
		}
		
		return output;
	}
}