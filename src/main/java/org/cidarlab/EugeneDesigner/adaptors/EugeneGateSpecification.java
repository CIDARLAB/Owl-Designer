package org.cidarlab.EugeneDesigner.adaptors;

import java.util.List;

import org.cidarlab.EugeneDesigner.dom.Part;

public class EugeneGateSpecification {

	public static String createEugeneScript(
			List<Part> promoters,
			List<Part> ribozymes,
			List<Part> rbsList,
			List<Part> genes,
			List<Part> terminators,
			boolean withRibozyme,
			String designMethod) {
	    	
	        String script = "";
	        script += "//COMMON PARTS AND PROPERTIES\n"
	                + "Property name(txt);\n"
	                + "Property SO(txt);\n"
	                + "\n"
	                + "PartType Promoter(name, SO);\n"
	                + "PartType Ribozyme(name, SO);\n"
	                + "PartType RBS(name, SO);\n"
	                + "PartType CDS(name, SO);\n"
	                + "PartType Terminator(name, SO);\n";

	        int count = 1;
	        for (Part gene : genes) {
	            script += "CDS g" + count++ + "(.SEQUENCE(\"" + gene.getSequence() + "\"), .name(\"" + gene.getName() + "\"), .SO(\"SO_0000316\"));";
	            script += "\n";
	        }

	        script += "num N = 1;\n";

	        count = 1;
	        for (Part promoter : promoters) {
	            script += "Promoter p" + count++ + "(.SEQUENCE(\"" + promoter.getSequence() + "\"), .name(\"" + promoter.getName() + "\"), .SO(\"SO_0000167\"));";
	            script += "\n";
	        }

	        count = 1;
	        for (Part ribozyme : ribozymes) {
	            script += "Ribozyme ri" + count++ + "(.SEQUENCE(\"" + ribozyme.getSequence() + "\"), .name(\"" + ribozyme.getName() + "\"), .SO(\"SO_0000627\"), .PIGEON(\"z ri 13\"));";
	            script += "\n";
	        }

	        count = 1;
	        for (Part rbs : rbsList) {
	            script += "RBS rbs" + count++ + "(.SEQUENCE(\"" + rbs.getSequence() + "\"), .name(\"" + rbs.getName() + "\"), .SO(\"SO_0000139\"));";
	            script += "\n";
	        }

	        count = 1;
	        for (Part terminator : terminators) {
	            script += "Terminator t" + count++ + "(.SEQUENCE(\"" + terminator.getSequence() + "\"), .name(\"" + terminator.getName() + "\"), .SO(\"SO_0000141\"));";
	            script += "\n";
	        }

	        script += "// Ribozyme (TRUE is with; FALSE is without); Default is TRUE.\n"
	                + "boolean riboz = " + withRibozyme + ";\n"
	                //=============================================================================
	                + "\n"

	                //MONOCISTRONIC promoter-rbs-gene-terminator DEVICE TEMPLATE
	                
	                + "Device Cello_gate();\n"
	                + "Rule r1(on Cello_gate: ALL_FORWARD);\n"
	                + "\n"
	                + "for(num i=1; i<=N; i=i+1) {\n"
	                + "  if(riboz == false) {\n"
	                + "  	Cello_gate = Cello_gate + RBS + CDS + Terminator;\n"
	                + "  } else {\n"
	                + "   Cello_gate = Cello_gate + Ribozyme + RBS + CDS + Terminator;\n"
	                + "  }\n"
	                + "  Ribozyme${\"ri\"+i};\n"
	                + "  RBS${\"rbs\"+i};\n"
	                + "  CDS${\"g\"+i};\n"
	                + "  Terminator${\"t\"+i};\n"
	                + "}\n"
	                + "\n"
				    
				    // GENERATE the DEVICE
				    + " lod = product(" + designMethod + ");\n"
				    + " println(\"The total number of devices: \" + SIZEOF(lod));\n"
	        		+ " println(lod); ";
				
				        return script;
				    }
	
}
