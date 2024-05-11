package com.pdfchat.ai.pdfchatbackend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;

import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@SpringBootApplication
public class PdfChatBackendApplication {
	
	private EmbeddingStoreIngestor embeddingStoreIngestor;
	private String apiUrl;
	
	public PdfChatBackendApplication(EmbeddingStoreIngestor embeddingStoreIngestor, String apiUrl) {
		this.embeddingStoreIngestor = embeddingStoreIngestor;
		this.apiUrl = apiUrl;
	}

	@PostConstruct
	public void init() throws IOException, URISyntaxException {
		Document doc = loadDocument(toPath("my-file.pdf"), new ApachePdfBoxDocumentParser());
		embeddingStoreIngestor.ingest(doc);
	}
	
	private static Path toPath(String fileName) {
		try {
			URL fileUrl = PdfChatBackendApplication.class.getClassLoader().getResource(fileName);
			return Paths.get(fileUrl.toURI());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("file not found");
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PdfChatBackendApplication.class, args);
	}

}
