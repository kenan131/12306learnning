package com.example.bin.util;

/**
 * @author: bin.jiang
 * @date: 2023/11/3 17:58
 **/

public class Message {
    private String type;
    private String value;

    public Message(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
