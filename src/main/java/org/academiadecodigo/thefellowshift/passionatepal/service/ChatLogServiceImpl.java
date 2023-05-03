package org.academiadecodigo.thefellowshift.passionatepal.service;

import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGtpRequestParameters;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatGptResponseDto;
import org.academiadecodigo.thefellowshift.passionatepal.model.ChatLog;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;
import org.academiadecodigo.thefellowshift.passionatepal.repository.ChatLogRepository;
import org.academiadecodigo.thefellowshift.passionatepal.service.client.chat.ChatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatLogServiceImpl implements ChatLogService {

    @Autowired
    private ChatLogRepository chatLogRepository;
    @Autowired
    private ChatServiceClient chatServiceClient;

    @Override
    public ChatLog save(ChatLog chatLog) {
        return chatLogRepository.save(chatLog);
    }


    @Override
    public ResponseEntity<ChatGptResponseDto> generateAnswer(ChatGtpRequestParameters parameters, List<Message> messages) {
        return chatServiceClient.completion(parameters, messages);
    }
    @Override
    public ChatLog getById(Integer id) {
        return chatLogRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ChatLog> findAll() {
        return chatLogRepository.findAll();
    }

    @Override
    public List<ChatLog> findAllByOrderByIdDesc() {
        return chatLogRepository.findAllByOrderByIdDesc();
    }
}
