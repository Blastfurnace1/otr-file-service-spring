package com.blastfurnace.otr.fileservice.rest.adapter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blastfurnace.otr.data.audiofile.AudioService;
import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.fileservice.rest.adapter.FileDataAdapter;
import com.blastfurnace.otr.service.response.GenericResponse;

@Component("AudioDataAdapter")
public class FileDataAdapterImpl implements FileDataAdapter {

	@Autowired
	private AudioService service;
	
	@Override
	public GenericResponse<AudioFileProperties> get(Long id) {
		GenericResponse<AudioFileProperties> response = new GenericResponse<AudioFileProperties>(null);
		try {
			AudioFileProperties audio = service.get(id);
			response.setPayload(audio);

			if (audio == null) {
				response.setStatus(10l);
				response.setMessage("No Results found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(-10l);
			response.setMessage("An Error Occurred - unable to get a result");
			response.setErrorOccured(true);
			response.addError(e.getMessage());
		}

		return response;
	}
}
