package com.blastfurnace.otr.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.blastfurnace.otr.model.AudioFileProperties;
import com.blastfurnace.otr.respository.AudioRepository;
import com.blastfurnace.otr.util.Utils;

/**
 * Servlet implementation class FileServer
 */
@WebServlet("/FileServer")
public class FileServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Autowired
    private AudioRepository repository;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileServer() {
        super();
        // TODO Auto-generated constructor stub
    }

    /** Get the name/location of the file. */
    private AudioFileProperties getFile(HttpServletRequest request) {
    	String id = request.getParameter("id");
        long fileId = Utils.getLong(id);
        
        Optional<AudioFileProperties> file = repository.findById(fileId);
        
        return file.get();
    }
    
    private String getFileName(AudioFileProperties member) {
    if (member == null) {
        return "";
    }
    
     return member.getDirectory() + member.getFilename();
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
    
    private void setMimeType(HttpServletResponse response, AudioFileProperties member) {
    	
    	// You must tell the browser the file type you are going to send
    	// for example application/pdf, text/plain, text/html, image/jpg
    	if (member.getAudioSamplingRate() > 0) {
    		response.setContentType("audio/mpeg");
    	} else {
    		response.setContentType("application/octet-stream");
    	}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	
    	AudioFileProperties afp = getFile(request);
    	String fileName = getFileName(afp);
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
