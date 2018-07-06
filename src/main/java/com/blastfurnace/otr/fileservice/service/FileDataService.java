package com.blastfurnace.otr.fileservice.service;


import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.service.response.GenericServiceResponse;

public interface FileDataService {

	GenericServiceResponse<AudioFileProperties> get(Long id);

	

}