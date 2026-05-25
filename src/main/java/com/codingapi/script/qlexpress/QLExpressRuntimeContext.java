package com.codingapi.script.qlexpress;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.QLResult;
import com.alibaba.qlexpress4.security.QLSecurityStrategy;
import com.codingapi.springboot.framework.script.request.GroovyMethodScript;
import com.codingapi.springboot.framework.script.request.RuntimeBindObject;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QLExpressRuntimeContext {

    @Getter
    private final static QLExpressRuntimeContext instance = new QLExpressRuntimeContext();

    private final Express4Runner runner;

    private QLExpressRuntimeContext(){
        InitOptions options = InitOptions.builder()
                .securityStrategy(QLSecurityStrategy.open())
                .build();
        this.runner = new Express4Runner(options);
    }

    public <T> T execute(String method, String script, Class<T> returnType, List<RuntimeBindObject> binds, Object... request) {
        Map<String,Object> params = new HashMap<>();
        if (binds != null && !binds.isEmpty()) {
            for (RuntimeBindObject bindObject : binds) {
                params.put(bindObject.getKey(), bindObject.getObject());
            }
        }
        StringBuilder callArgs = new StringBuilder();
        if (request != null) {
            for (int i = 0; i < request.length; i++) {
                String name = "arg" + i;
                params.put(name, request[i]);
                if (i > 0) {
                    callArgs.append(",");
                }
                callArgs.append(name);
            }
        }
        String fullScript = script + "\n" + method + "(" + callArgs + ");";
        QLOptions options = QLOptions.builder().polluteUserContext(true).build();
        QLResult result = runner.execute(fullScript,params,options);
        return (T)result.getResult();
    }

    public <T> T run(String script, Class<T> returnType, List<RuntimeBindObject> binds, Object... args) {
        return execute("run", script, returnType, binds, args);
    }


    public <T> T run(GroovyMethodScript<T> request){
        return execute("run",request.getScript(),request.getReturnType(),request.getBinds(),request.getParams());
    }

}
