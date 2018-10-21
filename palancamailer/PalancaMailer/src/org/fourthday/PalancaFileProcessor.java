package org.fourthday;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fourthday.bean.EmailBean;

public class PalancaFileProcessor {
	private static final String FILENAME = "C:\\Users\\rich\\Palanca Request List.pdf";
	private static final String EMAILFILE = "emailList.txt";
	private List<String>mailList;
	public PalancaFileProcessor() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * read the palanca pdf
	 * @return the lines that have mailto: references
	 */
	public void readPDFFile(){
		mailList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("mailto:")) {
						mailList.add(sCurrentLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("..fixList...");
		this.fixList();
		return;
	}
	
	
	/** 
	 * the test.txt list must be edited for date.
	 */
	public void writeList() {
		File file = new File(EMAILFILE);
		file.delete();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMAILFILE))) {
			for (String line : mailList) {
				bw.write(line + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void fixList(){
		List<String> tmpList = new ArrayList<String>();
		for (String s: mailList) {
			s = s.replace("(mailto:","").replace("\\", "").replace("))", ")");
			s = s.replaceAll("[^\\p{ASCII}]", "");
			EmailBean eb = new EmailBean(s);
			System.out.println("   ..adding.. "+eb.toString());
			tmpList.add(s);
		}
		mailList = tmpList;
	}
	
	public static void main(String[] args) {
		System.out.println("Starting PalancaFileProcessor()...");
		PalancaFileProcessor pfr = new PalancaFileProcessor();
		System.out.println("..reading "+FILENAME+"...");
		pfr.readPDFFile();
		System.out.println("..writing to "+EMAILFILE);
		
		pfr.writeList();
		

	}

}
