package com.blastfurnace.otr.fileservice.adapter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.fileservice.adapter.FileDataAdapter;
import com.blastfurnace.otr.fileservice.service.FileDataService;
import com.blastfurnace.otr.service.response.GenericResponse;

@Component("FileDataAdapter")
public class FileDataAdapterImpl implements FileDataAdapter {

	@Autowired
	private FileDataService service;
	
	@Override
	public GenericResponse<AudioFileProperties> get(Long id) {
		return service.get(id);
	}
}
