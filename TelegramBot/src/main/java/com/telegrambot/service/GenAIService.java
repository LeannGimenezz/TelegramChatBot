package com.telegrambot.service;

import com.telegrambot.controller.dto.ChatRequest;
import com.telegrambot.model.BookModel;

public interface GenAIService {

    String getResponse(ChatRequest request);

    String getResponseExtended(ChatRequest request);

    BookModel getBookModelFromText(String question);
}