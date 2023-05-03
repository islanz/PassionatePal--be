package org.academiadecodigo.thefellowshift.passionatepal.service.client.chat;

import lombok.extern.slf4j.Slf4j;
import org.academiadecodigo.thefellowshift.passionatepal.dto.MessageDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGptRequestDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGtpRequestParameters;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatGptResponseDto;
import org.academiadecodigo.thefellowshift.passionatepal.exception.GptRequestFailed;
import org.academiadecodigo.thefellowshift.passionatepal.exception.RestTemplateResponseErrorHandler;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ChatServiceClientImpl implements ChatServiceClient {


    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateBuilder builder;

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<ChatGptResponseDto> completion(ChatGtpRequestParameters parameters, List<Message> messages) throws GptRequestFailed {
        log.debug("Initiated ChatGPT client request");

        HttpEntity<ChatGptRequestDto> entity = new HttpEntity<>(ChatGptRequestDto.builder()
                .temperature(parameters.getTemperature())
                .maxTokens(parameters.getMaxTokens())
                .model(parameters.getModel())
                .stream(parameters.getStream())
                .messageDtoList(messages.stream().map(message -> modelMapper.map(message, MessageDto.class)).toList()).build(), generateHeaders());

        RestTemplate restTemplate = this.builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
        log.debug("{}:{}", "Passed to ChatGPT client", entity.getBody());

        ResponseEntity<ChatGptResponseDto> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, ChatGptResponseDto.class);
        if (response.getStatusCode().value() != 200) {
            throw new GptRequestFailed("Response status code from gpt: " + response.getStatusCode().value());
        }
        log.debug("{}:{}", "Received from ChatGPT client", response.getBody());
        return response;

    }



    @Override
    public ResponseEntity<Resource> completionStream() {
        return null;
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;
    }
}
