package com.blastfurnace.otr.fileservice.service;


import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.service.response.GenericResponse;
public interface FileDataService {

	GenericResponse<AudioFileProperties> get(Long id);

	

}