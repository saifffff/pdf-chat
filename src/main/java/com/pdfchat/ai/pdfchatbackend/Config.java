package com.pdfchat.ai.pdfchatbackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

@Configuration
public class Config {
	
	// Bean Definitions
	
	// This model is responsible for generating vector representations of text documents.
	@Bean
	public EmbeddingModel embeddingModel() {
		return new AllMiniLmL6V2EmbeddingModel();
	}
	
	
//	Creates a bean of type PgVectorEmbeddingStore and configures it with connection details to a PostgreSQL database. 
//	This store likely stores the generated vector representations.
	@Bean
	public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
		return PgVectorEmbeddingStore.builder()
				.host("localhost")
				.port(5432)
				.database("vectordb")
				.user("root")
				.password("root")
				.table("pdf-chat")
				.dimension(384)
				.build();
	}
	
	
//	Creates a bean of type EmbeddingStoreIngestor and configures it to ingest documents and generate embeddings.
	@Bean
	public EmbeddingStoreIngestor embeddingStoreIngestor() {
		return EmbeddingStoreIngestor.builder()
				.documentSplitter(DocumentSplitters.recursive(300, 0))
				.embeddingModel(embeddingModel())
				.embeddingStore(pgVectorEmbeddingStore())
				.build();
	}
	
	
//	Creates a bean of type ConversationalRetrievalChain and configures it for retrieval tasks in a conversational context.
	@Bean
	public ConversationalRetrievalChain conversationalRetrievalChain() {
		return ConversationalRetrievalChain.builder()
				.chatLanguageModel(OllamaChatModel.builder()
						.baseUrl("http://localhost:11434")
						.modelName("llama3")
						.build())
				.contentRetriever(EmbeddingStoreContentRetriever.from(pgVectorEmbeddingStore()))
				.build();
	}
	
}
