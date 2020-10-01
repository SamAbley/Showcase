package InstantMessaging;

import com.fasterxml.jackson.databind.ObjectMapper;

final class MessageConstants {

    static final String INSTANTIATION_NOT_ALLOWED = "Instantiation not allowed";
    static final String USER_NAME_KEY = "username";
    static final String RECIPIENT_KEY = "recipient";
    static final String MESSAGE_KEY = "message";
    static final String TYPE_KEY = "type";
    static final ObjectMapper MAPPER = new ObjectMapper();

    private MessageConstants() {
        throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
    }
}
