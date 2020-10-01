package InstantMessaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Message {

    @JsonProperty("username")
    private final Integer userName;
    @JsonProperty("message")
    private final String message;
    @JsonProperty("type")
    private final String type;
    @JsonProperty("recipient")
    private final Integer recipient;

    @JsonCreator
    public Message(@JsonProperty("username") final Integer userName, @JsonProperty("message") final String message, @JsonProperty("type") final String type, @JsonProperty("recipient") final Integer recipient) {


        Objects.requireNonNull(message);
        this.recipient = recipient;
        this.userName = userName;
        this.message = message;
        this.type = type;
    }

    Integer getUserName() {
        return this.userName;
    }

    String getMessage() {
        return this.message;
    }

    Integer getRecipient() {
        return recipient;
    }

    String getType() {
        return type;
    }
}
