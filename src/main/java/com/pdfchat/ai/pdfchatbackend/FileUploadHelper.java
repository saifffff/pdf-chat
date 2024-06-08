package com.pdfchat.ai.pdfchatbackend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	public final String uploadDir = "C:\\Users\\angry\\OneDrive\\Documents\\Shuats\\Sem 8 - final\\pdf-chat-frontend\\pdf-chat-backend\\src\\main\\resources";
	
	public boolean uploadFile(MultipartFile file) {
		
		boolean flag = false;
		
		
		try {
			
			
//			// read file
//			InputStream is = file.getInputStream();
//			byte data[] = new byte[is.available()];
//			
//			is.read(data);
//			
//			// write file to disk
//			FileOutputStream fos = new FileOutputStream(uploadDir+File.separator+"my-file");
//			fos.write(data);
//			
//			fos.flush();
//			fos.close();
			
			Files.copy(file.getInputStream(), Paths.get(uploadDir+File.separator+"my-file.pdf"), StandardCopyOption.REPLACE_EXISTING);
			
			flag = true;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("file upload failed...");
			e.printStackTrace();
		}
		
		return flag;
		
	}
	
}
