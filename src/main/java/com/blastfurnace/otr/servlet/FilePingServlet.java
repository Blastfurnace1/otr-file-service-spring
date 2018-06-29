package com.blastfurnace.otr.servlet;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.fileservice.service.FileDataService;
import com.blastfurnace.otr.service.response.GenericResponse;

@WebServlet("/ping")
public class FilePingServlet extends PingServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	FileDataService service;
	
	protected String checkServices() {
		try {
			GenericResponse<AudioFileProperties> response = service.get(1l);
			if (response.getStatus() != 0) {
				return response.getMessage();
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		
		
		return "File Server - Status OK";
	}

	public FilePingServlet() {
		// TODO Auto-generated constructor stub
	}

}
