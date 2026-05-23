package com.codingapi.script.groovy;

import com.codingapi.script.request.BindObject;
import com.codingapi.script.request.ScriptRequest;
import com.codingapi.script.utils.Sha256Utils;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroovyRuntimeContext {

    private final GroovyShell shell;

    private final Map<String, Script> cache;

    public static final int MAX_CACHE_SIZE = 10000;

    @Getter
    private static final GroovyRuntimeContext instance = new GroovyRuntimeContext();

    private GroovyRuntimeContext() {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(getClass().getClassLoader());
        this.shell = new GroovyShell(groovyClassLoader);
        this.cache = new LinkedHashMap<>(16, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Script> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }


    public <T> T execute(String method, String script, Class<T> returnType, List<BindObject> binds, Object... args) {
        String key = Sha256Utils.sha256(script);
        Script runtime = this.cache.get(key);
        if (runtime == null) {
            runtime = this.shell.parse(script);
            this.cache.put(key, runtime);
        }

        if (binds != null && !binds.isEmpty()) {
            for (BindObject bindObject : binds) {
                runtime.setProperty(bindObject.getKey(), bindObject.getObject());
            }
        }
        return (T) runtime.invokeMethod(method, args);
    }


    public <T> T run(String script, Class<T> returnType, List<BindObject> binds, Object... args) {
        return execute("run", script, returnType, binds, args);
    }

    public <T> T run(ScriptRequest<T> request) {
        return execute("run", request.getScript(), request.getReturnType(), request.getBinds(), request.getParams());
    }


}
