package org.academiadecodigo.thefellowshift.passionatepal.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLogDto {

    private Integer id;
    private List<AnswerDataDto> messages;

}
