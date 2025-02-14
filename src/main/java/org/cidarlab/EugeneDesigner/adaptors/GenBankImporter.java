package org.cidarlab.EugeneDesigner.adaptors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava3.core.sequence.DNASequence;
import org.cidarlab.EugeneDesigner.dom.GenBankFeature;

public class GenBankImporter {
	public final static List<GenBankFeature> analyzeGenBank(String input, boolean toExtractNativeRbs) {
        String gbk = input; //turns the file into one big string
        String[] sections = gbk.split("(?=\\n\\p{Upper})"); //cut on "newline Uppercase"
        List<GenBankFeature> gbkList = new ArrayList<>();
        LinkedHashMap<String, String> gbkData = new LinkedHashMap<>(); //create place to store sections
        for (String section : sections) {
            String[] nameValue = section.trim().split("\\s\\s", 2);
            gbkData.put(nameValue[0].trim(), nameValue[1].trim());
        }
        
        /*// to check gbkData hashmap
        for(String key: gbkData.keySet()){
        	System.out.println(key + "    ->    " + gbkData.get(key));
        }*/

        String featureSection = gbkData.get("FEATURES"); //Gets the text in the features section
        //System.out.println(featureSection);
        
        String featureSectionCOR = featureSection.split("\n", 2)[1]; //Gets rid of some descriptive text that messes up regex
        //System.out.println(featureSectionCOR);
        
        
        String[] featureList = featureSectionCOR.split("(?=\\n\\s{5}?\\p{Alpha})");
        /*System.out.println("==============FEATURE ARRAY==============");
        System.out.println(Arrays.toString(featureList));
        System.out.println("==============SEQUENCE RAW===============");
        System.out.println(gbkData.get("ORIGIN"));*/
        String dnaSEQUENCE = gbkData.get("ORIGIN").replaceAll("[\\s\\d/]", ""); //removes white spaces, digits, and /
        /*System.out.println("==================DNA SEQUENCE============");
        System.out.println("substring 1-100: " + dnaSEQUENCE.substring(0, 100).toUpperCase()); // entire String cannot be printed out (too long?). 
        System.out.println("=========================================");*/
        String gbkId = gbkData.get("VERSION").split("\\s", 2)[0]; //Version is a better way to get GenBank accession pointing to the right version
       /* System.out.println("==================GenBank accession number=====================");
        System.out.println(gbkId);*/
        //String gbkDescr = gbkData.get("DEFINITION"); //to get any DEFINITIONS: organism, gene, description
        //String authors = gbkData.get("AUTHORS"); //AUTHORS are actually stored under key "REFERENCE", not under "AUTHORS" key
        //System.out.println("==================AUTHORS============");
        //System.out.println(authors);
        //System.out.println("=========================================");
        
        if (dnaSEQUENCE.isEmpty()) {
            System.err.println("ERROR: GenBank input file does not contain a sequence!");
            System.exit(1);
        }
        
        //System.out.println("=============List of Features===============");
        for (String feature : featureList) { //for each feature I need: Name, Type, Start, Stop, Direction
            GenBankFeature gbFeature = new GenBankFeature();
            
            //GenBank accession and version
            gbFeature.setOldAccession(gbkId);
            gbFeature.setFullSequence(dnaSEQUENCE.toUpperCase());
            
            String featureName = null;
            boolean isReverse = false;
            String[] splitFeatures = feature.trim().split("\\s+");
            String featureType = splitFeatures[0];
            //System.out.println(featureType);
            gbFeature.setFeatureType(featureType);
            /*System.out.println(featureType);
            System.out.println(gbFeature.getFeatureType().toLowerCase());*/
            
            String featureLengthString = splitFeatures[1];
            //this is all just slicing and dicing to find relevant information for the annotations. 
            if (feature.contains("/gene=")) {
                featureName = feature.split("/gene=\"")[1].split("\"")[0];
                gbFeature.setName(featureName); 
            } else if (feature.contains("/note=")) {
                featureName = feature.split("/note=\"")[1].split("\"")[0];
                gbFeature.setName(featureType + " : " + featureName);
            } else {
                featureName = featureType;
                gbFeature.setName(featureName);
            }
            
            //check for old locus_tag in the annotation and save its value.
            if (feature.contains("/locus_tag=")) {
                featureName = feature.split("/locus_tag=\"")[1].split("\"")[0];
                gbFeature.setGenBankId(featureName);
            }
            
            featureLengthString = featureLengthString.replaceAll("[<>]", ""); //gets rid of extraneous notation
            
            if (featureLengthString.contains("complement") && !featureLengthString.contains("join")) { //Checks direction
                isReverse = true;
                String inside = null;
                Pattern p = Pattern.compile("\\((.*?)\\)");
                Matcher m = p.matcher(featureLengthString);
                while (m.find()) {
                    inside = m.group(1);
                }
                featureLengthString = inside;
            }
            
            gbFeature.setReverseComplement(isReverse);

            //there are five cases for coordinates: X, X..Y, Complement(X..Y),join(X1..Y1,X2..Y2,...,XN..YN), complement(join())
            if (featureLengthString.contains("join")) {
                if (featureLengthString.contains("complement")) {
                    isReverse = true;
                }
                
                String joins = null;
                Pattern p = Pattern.compile("join\\((.*?)\\)");
                Matcher m = p.matcher(featureLengthString);
                
                while (m.find()) {
                    joins = m.group(1);
                }
                
                String[] contigs = joins.split(",");
                
                for (String contig : contigs) {
                    GenBankFeature joinFet = new GenBankFeature();
                    String[] startStop = contig.split("\\.\\.");
                    joinFet.setStartx(Integer.parseInt(startStop[0]));
                    joinFet.setEndx(Integer.parseInt(startStop[1]));
                    joinFet.setReverseComplement(isReverse);
                    joinFet.setName(gbFeature.getName());
                    joinFet.setGenBankId(gbFeature.getGenBankId());
                    joinFet.setFeatureType(gbFeature.getFeatureType());
                    gbkList.add(joinFet);
                }
            } else if (featureLengthString.contains(".")) {
                String[] startStop = featureLengthString.split("\\.\\.");
                gbFeature.setStartx(Integer.parseInt(startStop[0]));
                gbFeature.setEndx(Integer.parseInt(startStop[1]));
                gbkList.add(gbFeature);

            } else if (!featureLengthString.contains(".")) {       
                gbFeature.setStartx(Integer.parseInt(featureLengthString));
                gbFeature.setEndx(Integer.parseInt(featureLengthString));
                gbkList.add(gbFeature);
            }
            
/*            Save this code to filter out CDS and save those in Clotho
 * 				if(gbFeature.getFeatureType().equals("CDS")){
 * 					// TO-DO: save this feature into Clotho;
            	}*/
            
            //take dnaSEQUENCE string and save subsequences for each feature and make them to the upper case.            
            
/*            if(gbFeature.getReverseComplement()){
            	
            	String seq = dnaSEQUENCE.substring(gbFeature.getStartx()-1, gbFeature.getEndx()).toUpperCase();
            	DNASequence dnaS = new DNASequence(seq);
            	System.out.println(dnaS.getReverseComplement().getSequenceAsString());
            	
            }	*/
            	
            	
            	gbFeature.setDnaSequence(dnaSEQUENCE.substring(gbFeature.getStartx()-1, gbFeature.getEndx()).toUpperCase(),gbFeature.isReverseComplement());
          
           	     
            	// Fetching native RBS components from GenBank entry
                if(gbFeature.getFeatureType().toLowerCase().contains("cds") && (toExtractNativeRbs)){
                	GenBankFeature rbsFeature = new GenBankFeature();
                	rbsFeature = getNativeRbs(gbFeature);
                 	if(rbsFeature.getDnaSequence()!=null){
                		gbkList.add(rbsFeature);
                	}
                }
            	
        }
        
        
        return gbkList;
    }
		
	public static String stringifyList(List<String> inputs){
		StringBuilder input = new StringBuilder();
		for(String l : inputs){
			input.append(l + "\n");
			//System.out.println(l);
		}
		return input.toString();
	}
	
	/** This method takes GenBankFeature object representing CDS component,
	 *  extracts native RBS sequences 40 nucleotides upstream of CDS components,
	 *  and returns GenBankFeture objects for RBS component.
	*/
	private static GenBankFeature getNativeRbs(GenBankFeature cds){
		
		final int rbsSeqRange = 41;
		final int constructLength = cds.getFullSequence().length();
		
		GenBankFeature rbsFeature = new GenBankFeature();
		
		rbsFeature.setName("natRBS_"+cds.getName());
		rbsFeature.setFeatureType("RBS");
		rbsFeature.setFullSequence(cds.getFullSequence());
				
		if(cds.isReverseComplement()){
			rbsFeature.setReverseComplement(true);
			if(cds.getEndx()+rbsSeqRange <= constructLength){
				rbsFeature.setStartx(cds.getEndx()+1);
				rbsFeature.setEndx(cds.getEndx()+rbsSeqRange);
				System.out.println("RBS [start=" + rbsFeature.getStartx() + ", end=" + rbsFeature.getEndx() + "]");
				DNASequence gRbs = new DNASequence(cds.getFullSequence().substring(rbsFeature.getStartx(), rbsFeature.getEndx()));
				rbsFeature.setDnaSequence(gRbs.getSequenceAsString(),rbsFeature.isReverseComplement());
			} else {
				System.err.println("WARNING: native RBS sequence for CDS " + cds.getName() + " is out of bounds. Skipping."
						+ " Please make sure to include at least 40 nt upstream of that CDS.");
			}

		} else {
			rbsFeature.setReverseComplement(false);
			if(cds.getStartx() >= rbsSeqRange){
				rbsFeature.setStartx(cds.getStartx()-rbsSeqRange);
				rbsFeature.setEndx(cds.getStartx()-1);
				System.out.println("RBS [start=" + rbsFeature.getStartx() + ", end=" + rbsFeature.getEndx() + "]");
				DNASequence gRbs = new DNASequence(cds.getFullSequence().substring(cds.getStartx()-41, cds.getStartx()-1));
				rbsFeature.setDnaSequence(gRbs.getSequenceAsString(), rbsFeature.isReverseComplement());
			} else {
				System.err.println("WARNING: native RBS sequence for CDS " + cds.getName() + " is out of bounds. Skipping."
						+ " Please make sure to include at least 40 nt upstream of that CDS.");
			}
		}
		
		return rbsFeature;
	}
}
