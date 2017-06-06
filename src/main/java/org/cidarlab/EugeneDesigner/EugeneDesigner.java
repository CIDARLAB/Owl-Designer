package org.cidarlab.EugeneDesigner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cidarlab.EugeneDesigner.adaptors.EugeneSpecification;
import org.cidarlab.EugeneDesigner.adaptors.GenBankImporter;
import org.cidarlab.EugeneDesigner.dom.FileType;
import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.dom.PartType;
import org.cidarlab.EugeneDesigner.util.CommandlineParser;
import org.cidarlab.EugeneDesigner.util.FeatureListToPartList;
import org.cidarlab.EugeneDesigner.util.InputFile;
import org.cidarlab.EugeneDesigner.util.Utilities;

import com.beust.jcommander.JCommander;

public class EugeneDesigner {
	
	private static List<Part> ribozymeList = new ArrayList<>();
	private static List<String> ribozymeContents = new ArrayList<>();
	
    public static void main( String[] args ) throws IOException, FileNotFoundException {
        
    	//System.out.println(getLogPrefix("EugeneDesigner") + "STARTED...");
    	PrintStream console = System.err;
        System.setErr(console);
        
        File errors = null;
        for(int i = 0; i < args.length; i++){
        	if(args[i].matches("^-e$")){
        		//System.out.println(getLogPrefix("EugeneDesigner") + "Redirecting errors (if any) to: " + args[i+1]);
        		errors = new File(args[i+1]);
        	}
        }
        
        if(errors != null){
        	PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(errors)), true);
        	System.setErr(ps);
        }
		
    	CommandlineParser cmd = new CommandlineParser();
    	JCommander jc = null;
    	
    	try{	
    		jc = new JCommander(cmd, args);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	//print stream for errors into the file specified in commandline argument -e file_name
    	//PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(cmd.getErrors())), true);
    	jc.setProgramName("EugeneDesigner");
    	
    	if(cmd.isHelp()){
    		getUsage(jc);
    	}
    	
    	File promoters = cmd.getPromoters();
    	File rbs = cmd.getRbs();
    	File genes = cmd.getGenes();
    	File terminators = cmd.getTerminators();
    	File ribozymes = new File("");
    	
    	if(cmd.isWithRybozyme()){
    		if(cmd.getRibozymes() != null) {
    			ribozymes = cmd.getRibozymes();
    			ribozymeContents = Utilities.getFileLines(ribozymes);
    			
    			if(InputFile.checkType(ribozymes)==FileType.FASTA){
    				ribozymeList = Utilities.fastaToPart(ribozymeContents, PartType.RIBOZYME, false);
    			}
    			if(InputFile.checkType(ribozymes)==FileType.GBK){
    				ribozymeList = FeatureListToPartList.convert(GenBankImporter.analyzeGenBank(GenBankImporter.stringifyList(ribozymeContents), false));
    			}
    			if(InputFile.checkType(ribozymes)==FileType.SBOL){
    				//TO-DO: add SBOL converter to Part objects.
    			}
    		} else {
    			System.err.println(getLogPrefix(cmd.getProjectName()) + " ERROR! \nMust provide \"-i ribozymes.fasta\" when \"-addRybozymes true\"\n");
    			System.exit(1);
    		}
    	}
    	
    	List<Part> promoterList = InputFile.inputToParts(promoters, PartType.PROMOTER, false);
    	List<Part> rbsList = null;
    	if(rbs.exists()){
    		rbsList = InputFile.inputToParts(rbs, PartType.RBS, false);
    	} else if(cmd.isDoNativeRbs()){
    		rbsList = InputFile.inputToParts(genes, PartType.CDS, true);
    	} else {
    		System.err.println("Please either provide RBS components in .fasta file or use -getRbs parameter and a GenBank input file containing gene cluster");
    		getUsage(jc);
    	}
    	List<Part> geneList = InputFile.inputToParts(genes, PartType.CDS, false);
    	List<Part> terminatorList = InputFile.inputToParts(terminators, PartType.TERMINATOR, false);
    	
    	String script = EugeneSpecification.createEugeneScript(promoterList, ribozymeList, rbsList, geneList, terminatorList, cmd.isWithRybozyme(), cmd.getDesignMethod());
    
		//System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(cmd.getOutput())), true));
    	if(cmd.getOutput() != null){
    		Utilities.writeToFile(cmd.getOutput(), script);
    	} else {
    		System.out.println(script);
    	}
    	    	
    	System.setErr(console);
    	
    	//System.out.println(getLogPrefix(cmd.getProjectName()) + " Saving Eugene specification file to: " + outPath);
        //System.out.println(getLogPrefix(cmd.getProjectName()) + " DONE.");
    	//System.out.println(Utilities.pathToProjectFolder(cmd.getProjectName()+".eug file was successfully created.\n\nFINISHED."));
    }

    private static String getLogPrefix(String project) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        return "[JIRA: " + project +"] " + formatter.format(new Date(System.currentTimeMillis())) + " ";

    }
    
    private static void getUsage(JCommander jc){
		System.out.println("\n===========================================");
    	jc.usage();
    	System.out.println("===========================================");
    	System.exit(0);
    }
    
}
