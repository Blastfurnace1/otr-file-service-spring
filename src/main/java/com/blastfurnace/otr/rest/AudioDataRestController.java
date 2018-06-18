package com.blastfurnace.otr.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blastfurnace.otr.model.AudioFileProperties;
import com.blastfurnace.otr.rest.adapter.AudioDataAdapter;
import com.blastfurnace.otr.rest.response.GenericRestResponse;

@RestController
@RequestMapping("/rest")
public class AudioDataRestController {

	@Autowired
	private AudioDataAdapter audioAdapter;

    @RequestMapping(value = "/fileExists/{id:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GenericRestResponse<AudioFileProperties>>  get(@PathVariable long  id) {
    	GenericRestResponse<AudioFileProperties> g = audioAdapter.get(id);
    	ResponseEntity<GenericRestResponse<AudioFileProperties>> response = new ResponseEntity<GenericRestResponse<AudioFileProperties>>(g, HttpStatus.OK);
    	return response;
    }

}
