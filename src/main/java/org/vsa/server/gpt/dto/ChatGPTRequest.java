package org.vsa.server.gpt.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String userMessage) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", userMessage));
    }
}
