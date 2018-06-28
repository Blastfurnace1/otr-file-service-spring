package com.blastfurnace.otr.fileservice.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.fileservice.adapter.FileDataAdapter;
import com.blastfurnace.otr.service.response.GenericResponse;

@RestController
@RequestMapping("/rest")
public class FileServiceRestController {

	@Autowired
	private FileDataAdapter audioAdapter;

    @RequestMapping(value = "/fileExists/{id:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GenericResponse<AudioFileProperties>>  get(@PathVariable long  id) {
    	GenericResponse<AudioFileProperties> g = audioAdapter.get(id);
    	ResponseEntity<GenericResponse<AudioFileProperties>> response = new ResponseEntity<GenericResponse<AudioFileProperties>>(g, HttpStatus.OK);
    	return response;
    }

}
