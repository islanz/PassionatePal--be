package org.academiadecodigo.thefellowshift.passionatepal.service.client.tts;

import java.util.concurrent.CompletableFuture;

public interface TextToSpeechClient {

    CompletableFuture<byte[]> synthesize(String text, String style, String character, String filename);
}
