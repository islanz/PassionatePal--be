package org.academiadecodigo.thefellowshift.passionatepal.service;

import java.util.concurrent.CompletableFuture;

public interface TtsService {


    void makeTtsRequest(String text, String style, String character, String filename);
}
