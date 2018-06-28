package com.blastfurnace.otr.fileservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blastfurnace.otr.data.audiofile.AudioService;
import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.fileservice.service.FileDataService;
import com.blastfurnace.otr.service.GenericService;
import com.blastfurnace.otr.service.response.GenericResponse;

@Component("FileDataService")
public class FileDataServiceImpl implements FileDataService {

	@Autowired
	private AudioService service;
	
	private GenericService<AudioFileProperties> gService;

	public FileDataServiceImpl() {
		gService = new GenericService<AudioFileProperties>(AudioFileProperties.fieldDefinitions);
	}
	
	
	@Override
	public GenericResponse<AudioFileProperties> get(Long id) {
		return gService.get(id, service);
	}
}
