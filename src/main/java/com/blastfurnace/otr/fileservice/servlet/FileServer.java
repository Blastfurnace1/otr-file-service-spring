package com.blastfurnace.otr.fileservice.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.blastfurnace.otr.util.Utils;
import com.blastfurnace.otr.data.audiofile.AudioService;
import com.blastfurnace.otr.data.audiofile.model.AudioFileProperties;
import com.blastfurnace.otr.fileservice.util.DriveMapper;

/**
 * Servlet implementation class FileServer
 */
@WebServlet("/FileServer")
public class FileServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String NO_DRIVE = "NO_DRIVE";
	private static HashMap<String, String> mappedDrives;
	
	@Autowired
    private AudioService service;
	
	private String getDrive(String volume) {
		if (mappedDrives.containsKey(volume)) {
			return mappedDrives.get(volume);
		} else {
			return NO_DRIVE;
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
		
		mappedDrives = DriveMapper.getDriveMappings();
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileServer() {
        super();
        // TODO Auto-generated constructor stub
    }

    /** Get the audio file record for the file id. */
    private AudioFileProperties getFile(HttpServletRequest request) {
    	AudioFileProperties audio = null;
    	String id = request.getParameter("id");
        long fileId = Utils.getLong(id);
        
        audio = service.get(fileId);
        // update the last requested info if the object is not null
        if (audio != null) {
        	audio.setRequestCount(audio.getRequestCount() + 1);
        	audio.setLastRequested(new Date());
        	//audio = repository.save(audio);
        }
        
        return audio;
    }
    
    /** Get the name/location of the file. */
    private String getFileName(String drive, AudioFileProperties member) {
	    if (member == null) {
	        return "";
	    }
	    
	    // Prepend drive to directory and file name
	    String volume = getDrive(member.getDiscId());
    
	    return volume + member.getDirectory() + member.getFilename();
    }
    
    /** Output the file to the response. */
    private void sendFile(HttpServletResponse response, File my_file) throws IOException {
    	  // This should send the file to browser
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(my_file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0){
           out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
    }
    
    /** get the mime type for the file. */
    private void setMimeType(HttpServletResponse response, AudioFileProperties member) {
    	
    	// TODO: use the mime type table - build service
    	// You must tell the browser the file type you are going to send
    	// for example application/pdf, text/plain, text/html, image/jpg
    	if (member.getAudioSamplingRate() > 0) {
    		response.setContentType("audio/mpeg");
    	} else {
    		// download
    		response.setContentType("application/octet-stream");
    	}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	AudioFileProperties afp = getFile(request);
    	String drive = getDrive(afp.getDiscId()); 
    	if (drive == NO_DRIVE) {
    		throw new WebServerException("Server Volume not available", new Exception("Could not get file"));
    	}
    	String fileName = getFileName(drive, afp);
    	System.out.println(fileName);
    	File my_file = new File(fileName);
    	response.setHeader("Content-disposition", "attachment; filename="+ afp.getFilename().replaceAll(" ", "_"));
    	setMimeType(response, afp);
    	if (my_file.exists() == false) {
    		throw new WebServerException("Could not find file", new Exception("Could not find file"));
    	} else {
    		try {
    			sendFile(response, my_file);
    		} catch  (Exception e) {
    			System.out.println(e.getMessage());
    			e.printStackTrace();
    			throw new WebServerException("Could not send file", new Exception(e.getMessage()));
    		}
    	}
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
