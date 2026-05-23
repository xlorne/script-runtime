package com.codingapi.script.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ScriptRequest<T> {

    private final String script;

    private final Class<T> returnType;

    private final List<Object> request;

    private final List<BindObject> binds;

    public ScriptRequest(String script, Class<T> returnType, Object... request) {
        this.script = script;
        this.returnType = returnType;
        this.request = new ArrayList<>();
        this.binds = new ArrayList<>();
        if (request != null) {
            this.request.addAll(Arrays.asList(request));
        }
    }

    public Object[] getParams() {
        return this.request.toArray();
    }

    public void addBindObject(String key, Object bind) {
        this.binds.add(new BindObject(key, bind));
    }



}
