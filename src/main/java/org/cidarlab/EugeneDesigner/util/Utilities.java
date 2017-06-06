package org.cidarlab.EugeneDesigner.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cidarlab.EugeneDesigner.dom.Part;
import org.cidarlab.EugeneDesigner.dom.PartType;

public class Utilities {

	public static final String pathToProjectFolder(String projectName) {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		String path = s + Utilities.getFileDivider() + projectName;
		
		return path;
	}
	
	public static List<Part> fastaToPart(List<String> fastaLines, PartType type, boolean isReverse) {
        List<Part> partList = new ArrayList<>();
        String name = "";
        String seq = "";
        for (String line : fastaLines) {

            if (line.startsWith(">") && (line.trim().length() > 1)) {
                if (!seq.isEmpty() && !name.isEmpty()) {
                	Part part = new Part(name, type, isReverse, seq);
                	partList.add(part);
                    seq = "";
                }
                name = line.substring(1);
            } else {
                seq += line;
            }
        }
        if (!name.isEmpty() && !seq.isEmpty()) {
        	Part part = new Part(name, type, isReverse, seq);
            partList.add(part);
        }
        return partList;
    }
	
	public static String readFastaFile(File fastaFile){
		// output.out is a text file containing Array[] lod, produced by Eugene.
		//String output = getResourcesFilepath()+"output.out";
				
		// read output line by line and concatenate the contents into a String eugContents.
		List<String> fileList = getFileLines(fastaFile);
		String contents = "";
			for(String s:fileList){
				contents += s + "\n";
		}
		return contents;
	}
	
	public static String writeToFile(File file, String content){
        try {
            FileWriter fr = new FileWriter(file);
            try (BufferedWriter br = new BufferedWriter(fr)) {
                br.write(content);
                br.flush();
            }
            
        } catch (FileNotFoundException ex) {
            System.err.println("File " + file.getName() + " not found.");
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return file.getAbsolutePath();
    }
    
    public static List<String> getFileLines(InputStream inp){
        BufferedReader br = new BufferedReader(new InputStreamReader(inp));
        List<String> lines = new ArrayList<String>();
        String line = "";
        try {
            while((line = br.readLine()) != null){
                if(!line.trim().equals("")){
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    
    // takes string representations of fasta files in JSON request
    public static List<String> getFastaLines(String filepath){
        List<String> lines = new ArrayList<String>();
        Scanner sc = new Scanner(filepath);
		String line = "";
		while (sc.hasNextLine()) {
			  line = sc.nextLine();
			  if(!line.trim().equals("")){
		      	System.out.println(line);
		      	lines.add(line);
		      }
			}
			sc.close();
        return lines;
    }
    
    public static List<String> getFileLines(String filepath){
        List<String> lines = new ArrayList<String>();
        File file = new File(filepath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = br.readLine()) != null){
                if(!line.trim().equals("")){
                	//System.out.println(line);
                	lines.add(line);
                }
            }
            br.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, e);
        }
        return lines;
    }
    
    public static List<String> getFileLines(File file){
        List<String> lines = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = br.readLine()) != null){
                if(!line.trim().equals("")){
                	//System.out.println(line);
                	lines.add(line);
                }
            }
            br.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, e);
        }
        return lines;
    }
    
    public static boolean isSolaris() {
        String os = System.getProperty("os.name");
        return isSolaris(os);
    }

    public static boolean isSolaris(String os) {
        if (os.toLowerCase().indexOf("sunos") >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return isWindows(os);
    }

    public static boolean isWindows(String os) {
        if (os.toLowerCase().indexOf("win") >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        return isLinux(os);
    }

    public static boolean isLinux(String os) {
        if ((os.toLowerCase().indexOf("nix") >= 0) || (os.indexOf("nux") >= 0) || (os.indexOf("aix") > 0)) {
            return true;
        }
        return false;
    }
    
    public static boolean isMac() {
        String os = System.getProperty("os.name");
        return isMac(os);
    }

    public static boolean isMac(String os) {
        if (os.toLowerCase().indexOf("mac") >= 0) {
            return true;
        }
        return false;
    }
    
    public static String getFileDivider(){
        
        if(isWindows()){
            return "\\";
        }
        return "/";
    }
    
    public static String getFilepath() {
        String _filepath = Utilities.class.getClassLoader().getResource(".").getPath();
        if (_filepath.contains("/target/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/target/"));
        } else if (_filepath.contains("/src/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/src/"));
        } else if (_filepath.contains("/build/classes/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/build/classes/"));
        }
        if (Utilities.isWindows()) {
            try {
                _filepath = URLDecoder.decode(_filepath, "utf-8");
                _filepath = new File(_filepath).getPath();
                if (_filepath.contains("\\target\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\target\\"));
                } else if (_filepath.contains("\\src\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\src\\"));
                } else if (_filepath.contains("\\build\\classes\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\build\\classes\\"));
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (_filepath.contains("/target/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/target/"));
            } else if (_filepath.contains("/src/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/src/"));
            } else if (_filepath.contains("/build/classes/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/build/classes/"));
            }
        }
       
        return _filepath;
    }
    
    public static String getResourcesFilepath() {
        String _filepath = getFilepath();
        System.out.println(_filepath);
        if (Utilities.isWindows()) {
            _filepath += "\\src\\main\\resources\\";
        } else {
            _filepath += "/src/main/resources/";
        }
        return _filepath;
    }
    
    
    public static String getOutputFilepath() {
        String _filepath = getFilepath();
        if (Utilities.isWindows()) {
            _filepath += "\\src\\main\\resources\\outputs\\";
        } else {
            _filepath += "/src/main/resources/outputs/";
        }
        return _filepath;
    }
    
/*    public static String getProjectFolderPath(OwlData project) {
        String _filepath = getFilepath();
        if (Utilities.isWindows()) {
            _filepath += "\\src\\main\\resources\\outputs\\"+project.getMyProjectId()+"\\";
        } else {
            _filepath += "/src/main/resources/outputs/"+project.getMyProjectId()+"/";
        }
        return _filepath;
    }*/
    
  /*  public static String getProjectFolderPath(String project) {
        String _filepath = getFilepath();
        if (Utilities.isWindows()) {
            _filepath += "\\src\\main\\resources\\outputs\\"+project+"\\";
        } else {
            _filepath += "/src/main/resources/outputs/"+project+"/";
        }
        return _filepath;
    }*/
    
    public static void removeFile(String pathToFile){
    	File file = new File(pathToFile);
    	String path = null;
		try {
			path = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	File filePath = new File(path);
    	filePath.delete();
    }
    
    public static boolean deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        boolean isDeleted = folder.delete();
        
        if(isDeleted){
        	return true;
        } else {
        	return false;
        }
    }

	
}
