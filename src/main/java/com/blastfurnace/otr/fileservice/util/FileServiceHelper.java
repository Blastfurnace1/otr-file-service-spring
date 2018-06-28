package com.blastfurnace.otr.fileservice.util;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.util.StringUtils;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;

public class FileServiceHelper {
	
	private static Log log = LogFactory.getLog(FileServiceHelper.class);
	
	private static void setCommonFileProperties(AudioFileProperties audioFile, String baseDir, File file, String zipfile, String outputFile, String disc) {
		audioFile.setDiscId(disc);
		audioFile.setDirectory(StringUtils.replaceInvalidChars(baseDir));
		audioFile.setFilename(StringUtils.replaceInvalidChars(file.getName()));
		String[] s = file.getName().split("\\.");
		audioFile.setFileType(s[s.length -1]);
		audioFile.setFileLength(file.length());
		audioFile.setZipFile(zipfile);
		if (zipfile.length() > 0) {
			audioFile.setZipped(1);
		}
		audioFile.setSeriesId(2651l);
	}

	
	public static AudioFileProperties getAudioFileProperties(String baseDir, File file, String zipfile, String outputFile, String disc, boolean badFile) {
		AudioFileProperties audioFile = new AudioFileProperties();
		setDefaultAudioDataProperties(audioFile);
		setCommonFileProperties(audioFile, baseDir, file, zipfile, outputFile, disc);
		audioFile.setAudioFile(0);
		setAudioTagData(null, audioFile);
		
		return audioFile;
	}

	
	private static void setDefaultAudioDataProperties(AudioFileProperties audioFile) {
		audioFile.setBitrate(0);
		audioFile.setAudioChannels(0);
		audioFile.setExtraEncodeInfo("");
		audioFile.setVariableBitrate(0);	
		audioFile.setAudioSamplingRate(0);
		audioFile.setDuration(0);
		audioFile.setEncodingType("");
		audioFile.setPreciseDuration(0.0f);
	}
	
	
	private static void setAudioDataProperties(AudioFileProperties audioFile, AudioFile audioData) {
		try {
			audioFile.setBitrate(audioData.getBitrate());
		} catch (Exception ex) {
			audioFile.setBitrate(0);
		}
		try {
			audioFile.setAudioChannels(audioData.getChannelNumber());
		} catch (Exception ex) {
			audioFile.setAudioChannels(0);
		}
		try {
			audioFile.setExtraEncodeInfo(audioData.getExtraEncodingInfos());
		} catch (Exception ex) {
			audioFile.setExtraEncodeInfo("");
		}
		try {
			audioFile.setVariableBitrate(audioData.isVbr()? 1 : 0);
		} catch (Exception ex) {
			audioFile.setVariableBitrate(0);	
		}
		try {
			audioFile.setAudioSamplingRate(audioData.getSamplingRate());
		} catch (Exception ex) {
			audioFile.setAudioSamplingRate(0);
		}
		try {
			audioFile.setDuration(audioData.getLength()/60);
		} catch (Exception ex) {
			audioFile.setDuration(0);
		}
		try {
			audioFile.setEncodingType(audioData.getEncodingType());
		} catch (Exception ex) {
			audioFile.setEncodingType("");
		}
		try {
			audioFile.setPreciseDuration(audioData.getPreciseLength());
		} catch (Exception ex) {
			audioFile.setPreciseDuration(0.0f);
		}
	}
	
	public static AudioFileProperties getAudioFileProperties(String baseDir, File file, String zipfile, String outputFile, String disc, AudioFile audioData) {
		AudioFileProperties audioFile = new AudioFileProperties();
		
		setCommonFileProperties(audioFile, baseDir, file, zipfile, outputFile, disc);
		setAudioDataProperties(audioFile, audioData);
		audioFile.setAudioFile(1);
		
		setAudioTagData(audioData.getTag(), audioFile);
		return audioFile;
	}

	private static String getValidCompressedData(@SuppressWarnings("rawtypes") List list) {
		return StringUtils.replaceInvalidChars(StringUtils.compressList(list));
	}
	
	private static String getAlbum(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getAlbum());	
		} catch (Exception e) {
			log.info("Unable to get Album " + e.getMessage());
		}
		return value;
	}
	
	private static String getArtist(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getArtist());
		} catch (Exception e) {
			log.info("Unable to get Artist " + e.getMessage());
		}
		return value;
	}
	
	
	private static String getGenre(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getGenre());
		} catch (Exception e) {
			log.info("Unable to get Genre " + e.getMessage());
		}
		return value;
	}
	
	private static String getTitle(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getTitle());
		} catch (Exception e) {
			log.info("Unable to get Title " + e.getMessage());
		}
		return value;
	}
	
	private static String getYear(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getYear());
		} catch (Exception e) {
			log.info("Unable to get Title " + e.getMessage());
		}
		return value;
	}
	
	
	private static String getTrackNo(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getTrack());
		} catch (Exception e) {
			log.info("Unable to get Title " + e.getMessage());
		}
		return value;
	}
	
	private static String getComment(Tag audioTag) {
		String value = "";
		try {
			value = getValidCompressedData(audioTag.getComment());
		} catch (Exception e) {
			log.info("Unable to get Title " + e.getMessage());
		}
		return value;
	}
	
	
	private static void setEmptyValuesForTag(AudioFileProperties afp) {
		afp.setAlbum("");
		afp.setArtist("");
		afp.setArtist("");
		afp.setTitle("");
		afp.setTitle("");
		afp.setTrackNo("");
		afp.setComment("");
	}
	
	
	public static void setAudioTagData (Tag audioTag, AudioFileProperties afp) {
		if (audioTag != null && !audioTag.isEmpty()) {
			afp.setAlbum(getAlbum(audioTag));
			afp.setArtist(getArtist(audioTag));
			afp.setArtist(getGenre(audioTag));
			afp.setTitle(getTitle(audioTag));
			afp.setTitle(getYear(audioTag));
			afp.setTrackNo(getTrackNo(audioTag));
			afp.setComment(getComment(audioTag));
		} else {
			setEmptyValuesForTag(afp);
		}
	}
	
}
