package com.blastfurnace.otr.fileservice.util;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.blastfurnace.otr.data.audiofile.AudioService;
import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.util.CSVUtils;
import com.blastfurnace.otr.util.FileUtils;
import com.blastfurnace.otr.util.StringUtils;

import entagged.audioformats.*;


public class FileDataLoader {

	private static Log log = LogFactory.getLog(FileDataLoader.class);
	
	@Autowired
	private AudioService service;

	// specify buffer size for extraction
	static final int BUFFER = 8192;

	private String disc = "MP3_1"; // 

	private String outFileDir = "C:/temp/";

	//Specify destination where file will be unzipped
	static String destinationDirectory = "C:/temp/";   

	private Vector<AudioFileProperties> fileProperties = new Vector<AudioFileProperties>();
	
	public static void main(String[] args) {
		FileDataLoader a = new FileDataLoader();
		a.iterateDirectories("E:/");
	}

	public void iterateDirectories(String baseDir) {
		disc = FileUtils.getDiscVolumeLabel(baseDir); 
		String outfile = outFileDir + disc + ".csv";
		log.info("Iterating directories ...");
		iterateDirectories(baseDir, "", baseDir);
		CSVUtils csv = new CSVUtils(AudioFileProperties.fieldDefinitions);
		csv.writeResults(fileProperties, outfile, ",");
	}

	public void iterateDirectories (String baseDir, String zipfile, String originalDir) {
		File f = new File(baseDir);
		String[] list = f.list();
		for (int i = 0; i < list.length; i++) {
			File dirCheck = new File(baseDir + list[i] + "/");
			// Process subdirectories
			if (dirCheck.isDirectory()) {
				iterateDirectories(baseDir + list[i] + "/", zipfile, ((zipfile.length() > 0) ? originalDir : baseDir + list[i] + "/"));
			} else {
				if (!list[i].endsWith("url")) { 
					if (list[i].endsWith("zip")) { 
						log.info("found a zip file " + list[i]);
					} else {
						addMp3File(originalDir, baseDir + list[i], baseDir + "/" + list[i], zipfile);
					}
				}
			}
		}	
	}

	public void addEntry(AudioFileProperties afp) {
		if (afp == null) {
			log.error("afp was null");
		}	
		String filename = null;
		try {      
			afp = service.save(afp);
		} catch (Exception e) {
			log.error("We died while inserting a record " + e.getMessage());
		}
		
		try {
			filename = afp.getDirectory().replaceAll("'", "") + afp.getFilename().replaceAll("'", "");
			File f = new File(filename);
			if (f.exists() == false) {
				File g = new File(afp.getDirectory() + afp.getFilename());
				if (g.exists()) {
					g.renameTo(f);
				} else {
					log.error("Can't load file: " + filename);
				}
			}
		} catch (Exception e) {
			log.error("Can't load file: " + filename + " " + e.getMessage());
		}
	}
	
	private boolean fileIsCatalogued(String directory, String filename) {
		// Look up item based on baseDir, filename, disc
		directory = StringUtils.replaceInvalidChars(directory);
		filename = StringUtils.replaceInvalidChars(filename);
		List<AudioFileProperties> audioFiles = service.findByDirectoryFilenameAndDiscId(directory, filename, disc);
		if (audioFiles.isEmpty() == false) {
			return true;
		}
		
		return false;
	}
	
	public void addMp3File(String baseDir, String filename, String realFileName, String zipfile) {
		if (fileIsCatalogued(baseDir, filename)) {
			return;
		}
		
		File file = new File(filename);
		try {
			AudioFile audioFile = AudioFileIO.read(file); //Reads the given file.
			AudioFileProperties audio = FileServiceHelper.getAudioFileProperties(baseDir, file, zipfile, filename, disc, audioFile);
			fileProperties.add(audio);
			log.info("Loading result into Database ... please wait");
			addEntry(audio);
		} catch (Exception ex) {
			addNonMp3File(baseDir, filename, realFileName, zipfile, false, disc);
		}
	}

	public void addNonMp3File (String baseDir, String filename,  String realFileName, String zipfile, boolean badFile, String disc) {
		File file = new File(filename);
		AudioFileProperties audio = FileServiceHelper.getAudioFileProperties(baseDir, file, zipfile, filename, disc, badFile);  
		fileProperties.add(audio);
		log.info("Loading result into Database ... please wait");
		addEntry(audio);
	}
}

