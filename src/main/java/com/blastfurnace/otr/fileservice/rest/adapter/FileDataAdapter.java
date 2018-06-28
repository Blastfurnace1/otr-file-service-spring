package com.blastfurnace.otr.fileservice.rest.adapter;


import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.service.response.GenericResponse;
public interface FileDataAdapter {

	GenericResponse<AudioFileProperties> get(Long id);

	

}