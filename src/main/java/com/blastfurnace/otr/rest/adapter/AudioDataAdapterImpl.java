package com.blastfurnace.otr.rest.adapter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blastfurnace.otr.model.AudioFileProperties;
import com.blastfurnace.otr.rest.response.GenericRestResponse;
import com.blastfurnace.otr.service.AudioService;


@Component("AudioDataAdapter")
public class AudioDataAdapterImpl implements AudioDataAdapter {

	@Autowired
	private AudioService service;
	
	@Override
	public GenericRestResponse<AudioFileProperties> get(Long id) {
		GenericRestResponse<AudioFileProperties> response = new GenericRestResponse<AudioFileProperties>(null);
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
