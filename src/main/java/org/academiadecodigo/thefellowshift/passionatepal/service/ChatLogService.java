package org.academiadecodigo.thefellowshift.passionatepal.service;

import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGtpRequestParameters;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatGptResponseDto;
import org.academiadecodigo.thefellowshift.passionatepal.model.ChatLog;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatLogService {


    ResponseEntity<ChatGptResponseDto> generateAnswer(ChatGtpRequestParameters parameters, List<Message> messages);
    ChatLog save(ChatLog chatLog);
    ChatLog getById(Integer id);

    List<ChatLog> findAll();
    List<ChatLog> findAllByOrderByIdDesc();
}
