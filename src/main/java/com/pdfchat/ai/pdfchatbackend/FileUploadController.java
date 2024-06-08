package com.pdfchat.ai.pdfchatbackend;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ai.djl.training.tracker.WarmUpTracker.Mode;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import okhttp3.internal.http.HttpMethod;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@RestController
@RequestMapping("/api/pdfchat")
public class FileUploadController {
	
	@Autowired
	private FileUploadHelper fileUploadHelper;
	@Autowired
	private EmbeddingStoreIngestor embeddingStoreIngestor;

	
//	private static Path toPath(String fileName) {
//		try {
//			URL fileUrl = PdfChatBackendApplication.class.getClassLoader().getResource(fileName);
//			System.out.println("file url : "+fileUrl);
//			return Paths.get(fileUrl.toURI());
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("file not found");
//			throw new RuntimeException(e);
//		}
//	}
	
	
	private static Path toPath(MultipartFile file) {
	    try {
	        String fileName = file.getOriginalFilename();
	        // Create a temporary file using the original filename
	        File tempFile = File.createTempFile(fileName, null);
	        // Transfer the uploaded file content to the temporary file
	        file.transferTo(tempFile);
	        // Create a Path object from the temporary file
	        Path filePath = tempFile.toPath();
	        System.out.println("File path: " + filePath);
	        return filePath;
	    } catch (Exception e) {
	        // Handle potential exceptions
	        System.out.println("Error getting file path: " + e.getMessage());
	        throw new RuntimeException(e);
	    }
	}
	
	
	
	@PostMapping("/upload")
	public ModelAndView uploadPdf(@RequestParam("pdfFile") MultipartFile file, Model model, HttpSession session){
		ModelAndView mav = new ModelAndView();
		 mav.setViewName("redirect:/docgpt"); 
		
		if(file.isEmpty()) {
			System.out.println("No files uploaded....");
			mav.addObject("err","PDF Cannot Be Empty !! Please try again...");
			return mav;
		}
		
		try {
			
			boolean flag = this.fileUploadHelper.uploadFile(file);
			System.out.println("file uploaded");
			
			if(flag) {
				
				// convert file into vectorEmbeddings
				Document doc = loadDocument(toPath(file), new ApachePdfBoxDocumentParser());
				//System.out.println("doc :"+doc);
				this.embeddingStoreIngestor.ingest(doc);
				System.out.println("file loaded into vector db");
				String fileName = file.getOriginalFilename(); // Get the original filename
				session.setAttribute("successMessage", "PDF is loaded successfully. You can now start chatting with : "+fileName);
				return mav;
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		mav.addObject("err", "Something went wrong. Please try again....!!");
		return mav;
	
	}


}