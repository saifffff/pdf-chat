package com.pdfchat.ai.pdfchatbackend;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pdfchat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	@Autowired
	private ConversationalRetrievalChain conversationalRetrievalChain;
	
	@PostMapping("/query")
	public ResponseEntity<String> chatWithPdf(@RequestBody String text) {
		String text1 = URLDecoder.decode(text, StandardCharsets.UTF_8);
		String prompt = text1.split("=")[1];
		String promptTemplate = "You are an AI assistant. Only give answer of the question from the provided document. Do not provide additional information"+" Question : ";
		String query = promptTemplate + prompt;
		System.out.println("query : "+query);
		var answer = conversationalRetrievalChain.execute(text);
		System.out.println(answer);
		return ResponseEntity.ok(answer);
	}
	
	  @GetMapping("/sayhi")
	  public String sayHi() {
	    return "Hello, Welcome to PDF Chat : An AI powered application that provides an interactive chat like interaction with your PDF files.";
	  }

	
}
