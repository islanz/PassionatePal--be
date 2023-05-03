package org.academiadecodigo.thefellowshift.passionatepal.controller;

import lombok.extern.slf4j.Slf4j;
import org.academiadecodigo.thefellowshift.passionatepal.Constants;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatLogDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGtpRequestParameters;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.AnswerDataDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatGptResponseDto;
import org.academiadecodigo.thefellowshift.passionatepal.model.ChatLog;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;
import org.academiadecodigo.thefellowshift.passionatepal.model.enums.PersonalityType;
import org.academiadecodigo.thefellowshift.passionatepal.service.ChatLogService;
import org.academiadecodigo.thefellowshift.passionatepal.service.TtsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/conversation")
@Slf4j
public class ChatLogController {

    @Autowired
    private ChatLogService chatLogService;
    @Autowired
    private TtsService ttsService;


    @PostMapping("/generate-answer")
    public ResponseEntity<AnswerDataDto> generateAnswer(@RequestBody AnswerDataDto dataDto) {

        log.debug("{}:{}", "Received from client generation request", dataDto);
        ChatGtpRequestParameters chatGtpRequestParameters = ChatGtpRequestParameters.builder()
                .model(Constants.MODEL_NAME)
                .temperature(Constants.TEMPERATURE)
                .maxTokens(Constants.MAX_TOKENS).stream(Constants.STREAM_STRATEGY_DEFAULT).build();
        AtomicReference<ChatLog> chatLog = new AtomicReference<>();
        List<Message> messages;

        String personalityTypeReceived = dataDto.getPersonalityType();
        PersonalityType personalityType = personalityTypeReceived != null  ? PersonalityType.getPersonalityTypeByReinforcementPrompt(personalityTypeReceived) : PersonalityType.COMPASSIONATE;
        Message.Gender gender = dataDto.getGender() == null ? Message.Gender.FEMALE : Message.Gender.valueOf(dataDto.getGender());
        String reinforcementPrompt = personalityType.personalityReinforcementMessage;
        if (dataDto.getId() == null) {
            chatLog.set(new ChatLog());
            messages = new ArrayList<>();
            Message messageSystem = new Message(Constants.DEFAULT_SYSTEM_MESSAGE, Message.RoleType.SYSTEM, chatLog.get(),null, null);
            messages.add(messageSystem);
        } else {
            chatLog.set(chatLogService.getById(dataDto.getId()));
            messages = chatLog.get().getMessages();
        }
        Message messagePrompt = new Message(reinforcementPrompt + dataDto.getContent(), Message.RoleType.USER, chatLog.get(),null, null);
        Message messageSave = new Message(dataDto.getContent(), Message.RoleType.USER, chatLog.get(),null,  null);
        List<Message> messagesToSave = new ArrayList<>(messages);
        messagesToSave.add(messageSave);
        messages.add(messagePrompt);

        ResponseEntity<ChatGptResponseDto> responseEntity = chatLogService.generateAnswer(chatGtpRequestParameters, messages);
        String assistantMessage = responseEntity.getBody().getChoices()[responseEntity.getBody().getChoices().length - 1].getMessage().getContent();

        ChatLog finalChatLog = chatLog.get();
        String filename = UUID.randomUUID().toString() + ".wav";
        chatLog.get().setMessages(messagesToSave);
        ttsService.makeTtsRequest(assistantMessage, "friendly", "en-US-AriaNeural", filename);

        Message assistantPrompt = new Message(assistantMessage, Message.RoleType.ASSISTANT, chatLog.get(), filename, gender);
        messagesToSave.add(assistantPrompt);
        chatLog.set(chatLogService.save(chatLog.get()));




        ChatGptResponseDto.ChoicesDTO[] choicesDTOS = responseEntity.getBody().getChoices();

        AnswerDataDto answerDataDto = new AnswerDataDto(chatLog.get().getId(), choicesDTOS[choicesDTOS.length - 1].getMessage().getContent(), personalityTypeReceived, filename, dataDto.getGender(), assistantPrompt.getCreateDateTime(), "assistant");
        log.debug("{}:{}", "Passed to client generation request", answerDataDto);

        return ResponseEntity.ok().headers(new HttpHeaders()).body(answerDataDto);
    }

    @GetMapping("/get-all-conversations")
    public ResponseEntity<List<ChatLogDto>> getAllStories() {
        log.debug("Received client get conversations request");
        System.out.println("Current absolute path is: " + Paths.get(".").toAbsolutePath().normalize() + File.separator + "sound");
        ModelMapper modelMapper = new ModelMapper();

        List<ChatLogDto> chatLogDtos = chatLogService
                .findAllByOrderByIdDesc()
                .stream()
                .map(v -> modelMapper.map(v, ChatLogDto.class))
                .toList();
        log.debug("{}:{}", "Passed to client get conversations request", chatLogDtos);

        return ResponseEntity.ok().body(chatLogDtos);
    }
}
