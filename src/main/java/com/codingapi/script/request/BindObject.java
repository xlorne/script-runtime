package com.codingapi.script.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BindObject {

    private String key;
    private Object object;
}
