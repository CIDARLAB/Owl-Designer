package org.cidarlab.EugeneDesigner.util;

import java.io.File;
import com.beust.jcommander.Parameter;
import lombok.Getter;

public class CommandlineParser {
	
	@Parameter(names = "-addRibozymes", description = "add optional ribozymes {true | false}", arity=1)
	@Getter private boolean withRybozyme = false;
	
	@Parameter(names = "-nativeRbs", description = "extract native RBS sequences 40 nt upstream of each gene", arity=1)
	@Getter private boolean doNativeRbs = false;
	
	@Parameter(names = "-design", description = "Design method")
	@Getter private String designMethod;
	
	@Parameter(names = "-p", description = "Promoter library (.fasta) file", converter = FileConverter.class)
	@Getter File promoters;
	
	@Parameter(names = "-i", description = "Ribozyme library (.fasta) file.\n       Required when \"-addRibozymes true\"", converter = FileConverter.class)
	@Getter File ribozymes;
	
	@Parameter(names = "-r", description = "RBS library (.fasta) file", converter = FileConverter.class)
	@Getter File rbs;
	
	@Parameter(names = "-g", description = "Gene library (.fasta) file", converter = FileConverter.class)
	@Getter File genes;
	
	@Parameter(names = "-t", description = "Terminator library (.fasta) file", converter = FileConverter.class)
	@Getter File terminators;
	
	@Parameter(names = "-project", description = "Project name")
	@Getter private String projectName;
	
	@Parameter(names = { "-help", "-h" }, description = "print this message")
	@Getter private boolean help = false;
	
	@Parameter(names = "-o", description = "<Output dir>", converter = FileConverter.class)
	@Getter File output;
	
	@Parameter(names = "-e", description = "<Error dir>", converter = FileConverter.class)
	@Getter File errors;
}
	