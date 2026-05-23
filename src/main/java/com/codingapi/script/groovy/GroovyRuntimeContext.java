package com.codingapi.script.groovy;

import com.codingapi.script.request.BindObject;
import com.codingapi.script.request.ScriptRequest;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.Getter;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.util.List;

public class GroovyRuntimeContext {


    private final GroovyShell shell;

    @Getter
    private static final GroovyRuntimeContext instance = new GroovyRuntimeContext();

    private GroovyRuntimeContext() {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setTargetDirectory((File) null);
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(getClass().getClassLoader(), config);
        this.shell = new GroovyShell(groovyClassLoader);
    }


    public <T> T execute(String method, String script, Class<T> returnType, List<BindObject> binds, Object... args) {
        Script runtime = this.shell.parse(script);
        if (binds != null && !binds.isEmpty()) {
            for (BindObject bindObject : binds) {
                runtime.setProperty(bindObject.getKey(), bindObject.getObject());
            }
        }
        if (args != null) {
            return (T) runtime.invokeMethod(method, args);
        } else {
            return (T) runtime.invokeMethod(method, null);
        }
    }

    public <T> T run(String script, Class<T> returnType, List<BindObject> binds, Object... args) {
        return execute("run", script, returnType, binds, args);
    }

    public <T> T run(ScriptRequest<T> request) {
        return execute("run", request.getScript(), request.getReturnType(), request.getBinds(), request.getParams());
    }


}
