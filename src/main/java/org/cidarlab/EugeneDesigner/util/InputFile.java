package org.cidarlab.EugeneDesigner.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cidarlab.EugeneDesigner.adaptors.GenBankImporter;
import org.cidarlab.EugeneDesigner.dom.FileType;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.dom.PartType;

public class InputFile {
	
	public static FileType checkType(File file){
		
		List<String> fileLines = Utilities.getFileLines(file);
		
		if(fileLines.get(0).startsWith(">")){
			return FileType.FASTA;
		} else if(fileLines.get(0).startsWith("LOCUS")){
			return FileType.GBK;
		} else if(fileLines.get(0).startsWith("<?xml version=")){
			return FileType.SBOL;
		} else{
			System.err.println("Unrecognized file type for: "+file);
			System.exit(1);
			return null;
		}
	}
	
	public static List<Part> inputToParts(File input, PartType partType, boolean getNativeRbs){
		
		List<Part> partList = new ArrayList<>();
		
		List<String> contentList = Utilities.getFileLines(input);
		
		if(InputFile.checkType(input)==FileType.FASTA){
			switch(partType.toString().toLowerCase()){
				case "promoter":
					partList = Utilities.fastaToPart(contentList, PartType.PROMOTER, false);
					break;
				case "cds":
					partList = Utilities.fastaToPart(contentList, PartType.CDS, false);
					break;
				case "rbs":
					partList = Utilities.fastaToPart(contentList, PartType.RBS, false);
					break;
				case "terminator":
					partList = Utilities.fastaToPart(contentList, PartType.TERMINATOR, false);
					break;
				default:
					break;
			}
		}
		
		if(InputFile.checkType(input)==FileType.GBK){
			partList = FeatureListToPartList.convert(GenBankImporter.analyzeGenBank(GenBankImporter.stringifyList(contentList), getNativeRbs));
		}
		if(InputFile.checkType(input)==FileType.SBOL){
			//TO-DO: add SBOL-to-Part conversion
		}
		
		return partList;
	}
	
}
