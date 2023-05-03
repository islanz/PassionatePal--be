package org.academiadecodigo.thefellowshift.passionatepal.service.client.tts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AzureTextToSpeechClient implements TextToSpeechClient {


    @Value("${azure.api.key}")
    private String apiKey;
    private final String API_TOKEN_URL = "https://westeurope.api.cognitive.microsoft.com/sts/v1.0/issuetoken";
    private final String API_URL = "https://westeurope.tts.speech.microsoft.com/cognitiveservices/v1";

    @Async
    @Override
    public CompletableFuture<byte[]> synthesize(String text, String style, String character, String filename) {
        String language = "en-US";

        RestTemplate restTemplate = new RestTemplate();
        // Token request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        // Request access token
        ResponseEntity<String> response = restTemplate.exchange(API_TOKEN_URL, HttpMethod.POST, entity, String.class);
        String token = response.getBody();


        // Tts request headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.set("Content-Type", "application/ssml+xml");
        httpHeaders.set("X-Microsoft-OutputFormat", "raw-16khz-16bit-mono-pcm");
        httpHeaders.set("User-Agent", "gsoftwarestorytellerinstance001");
        //String requestBody = "<speak version='1.0' xml:lang='" + language + "'><voice xml:lang='en-US' xml:gender='Male' name='en-US-ChristopherNeural'>" + text + " </voice></speak>";
        String requestBody = "<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"https://www.w3.org/2001/mstts\" xml:lang=\"" + language + "\"> <voice name=\"" + character + "\"> <mstts:express-as style=\"" + style + "\" styledegree=\"2\">" + text + "</mstts:express-as> </voice> </speak>";
        System.out.println("Started Azure request");

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        //Tts request
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, httpEntity, byte[].class);
        byte[] audioData = responseEntity.getBody();



        return CompletableFuture.completedFuture(audioData);
    }


}
