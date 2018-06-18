package com.blastfurnace.otr.rest.adapter;

import com.blastfurnace.otr.model.AudioFileProperties;
import com.blastfurnace.otr.rest.response.GenericRestResponse;
public interface AudioDataAdapter {

	GenericRestResponse<AudioFileProperties> get(Long id);

	

}