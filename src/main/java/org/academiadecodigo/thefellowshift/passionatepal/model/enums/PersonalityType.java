package org.academiadecodigo.thefellowshift.passionatepal.model.enums;
import org.academiadecodigo.thefellowshift.passionatepal.Constants;

import java.util.NoSuchElementException;

public enum PersonalityType {

    COMPASSIONATE(Constants.DEFAULT_COMPASSIONATE_MESSAGE),
    FRISKY(Constants.DEFAULT_FRISKY_MESSAGE),
    COMEDIAN(Constants.DEFAULT_COMEDIAN_MESSAGE);


    public final String personalityReinforcementMessage;
    PersonalityType(String defaultComedianMessage) {
        personalityReinforcementMessage = defaultComedianMessage;
    }


    public static PersonalityType getPersonalityTypeByReinforcementPrompt(String reinforcementPrompt) {

        for(PersonalityType personalityType : PersonalityType.values()) {
            if (reinforcementPrompt.equalsIgnoreCase(personalityType.name())) return personalityType;
        }
        return PersonalityType.COMPASSIONATE;
    }
}
