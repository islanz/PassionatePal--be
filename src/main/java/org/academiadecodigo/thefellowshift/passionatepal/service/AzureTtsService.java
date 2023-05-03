package org.academiadecodigo.thefellowshift.passionatepal.service;

import org.academiadecodigo.thefellowshift.passionatepal.service.client.tts.TextToSpeechClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Service
public class AzureTtsService implements TtsService {


    @Autowired
    private ChatLogService chatLogService;
    @Autowired
    private TextToSpeechClient textToSpeechClient;

    @Override
    public void makeTtsRequest(String text, String style, String character, String filename) {
        CompletableFuture<byte[]> completableFuture = textToSpeechClient.synthesize(text, style, character, filename);
        completableFuture.thenAccept(s -> {
            saveAudio(s, filename);
        });
    }


    public void saveAudio(byte[] data, String filename) {

        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);

        File dstFile = new File(Paths.get(".").toAbsolutePath().normalize() + File.separator + "sound" + File.separator + filename);
        AudioInputStream inputStream = new AudioInputStream(new ByteArrayInputStream(data), format, data.length / format.getFrameSize());

        // Write the audio input stream to a .wav file
        try {
            AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE, dstFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
