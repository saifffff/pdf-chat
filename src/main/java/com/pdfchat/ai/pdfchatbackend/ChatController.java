package com.pdfchat.ai.pdfchatbackend;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.chain.ConversationalRetrievalChain;
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
	
	@PostMapping
	public String chatWithPdf(@RequestBody String text) {
		var answer = conversationalRetrievalChain.execute(text);
		System.out.println(answer);
		return answer;
	}
	
	  @GetMapping("/sayhi")
	  public String sayHi() {
	    return "hello I am active";
	  }
	
	
}
