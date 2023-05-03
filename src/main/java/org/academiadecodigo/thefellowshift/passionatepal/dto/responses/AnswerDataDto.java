package org.academiadecodigo.thefellowshift.passionatepal.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.academiadecodigo.thefellowshift.passionatepal.dto.MessageDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDataDto {
    private Integer id;
    private String content;
    private String personalityType;
    private String audio;
    private String gender;
    private LocalDateTime createDateTime;
    private String role;

    @Override
    public String toString() {
        return "AnswerDataDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", personalityType='" + personalityType + '\'' +
                '}';
    }
}
