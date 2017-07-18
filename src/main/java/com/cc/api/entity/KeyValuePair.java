package com.cc.api.entity;

public class KeyValuePair {

    private int key;

    private String Value;

    public KeyValuePair() {
    }

    public KeyValuePair(int key, String value) {
        this.key = key;
        Value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = value;
    }
}
