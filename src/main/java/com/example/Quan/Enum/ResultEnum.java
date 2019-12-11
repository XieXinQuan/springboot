package com.example.Quan.Enum;

public enum  ResultEnum {
    Success(1, "Success"),
    CustomException(2, "Custom Exception"),
    PromptInformation(3, "Prompt Information"),
    LoginOverDue(4, "Login Over Due"),
    GoToLogin(5, "Go To Login"),
    NoRequestMapper(404, "Go To Login"),
    SystemException(500, "System Exception");
    private Integer key;
    private String value;

    ResultEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
