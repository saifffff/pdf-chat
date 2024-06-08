package com.pdfchat.ai.pdfchatbackend;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

@Configuration
public class Config {
	
	// Bean Definitions
	
	// This model is responsible for generating vector representations of text documents.
	@Bean
	EmbeddingModel embeddingModel() {
		return new AllMiniLmL6V2EmbeddingModel();
	}
	
	
	// in memory embedding store
	@Bean
	 EmbeddingStore<TextSegment> embeddingStore(){
		
	 return new InMemoryEmbeddingStore<>();
		
	}
	
	
////	Creates a bean of type PgVectorEmbeddingStore and configures it with connection details to a PostgreSQL database. 
////	This store likely stores the generated vector representations.
//	@Bean
//	public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
//		return PgVectorEmbeddingStore.builder()
//				.host("localhost")
//				.port(5432)
//				.database("myvectordb")
//				.user("root")
//				.password("root")
//				.table("test")
//				.dimension(384)
//				.build();
//	}
	
	
	
	
//	Creates a bean of type EmbeddingStoreIngestor and configures it to ingest documents and generate embeddings.
	@Bean
	EmbeddingStoreIngestor embeddingStoreIngestor() {
		return EmbeddingStoreIngestor.builder()
				.documentSplitter(DocumentSplitters.recursive(300, 10))
				.embeddingModel(embeddingModel())
				.embeddingStore(embeddingStore())
				.build();
	}
	
	
////	Creates a bean of type ConversationalRetrievalChain and configures it for retrieval tasks in a conversational context.
//	@Bean
//	public ConversationalRetrievalChain conversationalRetrievalChain() {
//		return ConversationalRetrievalChain.builder()
//				.chatLanguageModel(OllamaChatModel.builder()
//						.baseUrl("http://localhost:11434")
//						.modelName("mistral")
//						.timeout(Duration.ofMinutes(10))
//						.build())
//				.contentRetriever(EmbeddingStoreContentRetriever.from(pgVectorEmbeddingStore()))
//				.build();
//	}
	
	@Bean
	ConversationalRetrievalChain conversationalRetrievalChain() {
		return ConversationalRetrievalChain.builder()
				.chatLanguageModel(
							OllamaChatModel.builder()
							.baseUrl("http://localhost:11434")
							.modelName("mistral")
							.timeout(Duration.ofMinutes(10))
							.build()
						).retriever(EmbeddingStoreRetriever.from(embeddingStore(), embeddingModel()))
				.build();
				
	}
	
}
