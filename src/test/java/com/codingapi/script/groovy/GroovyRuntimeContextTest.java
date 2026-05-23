package com.codingapi.script.groovy;

import com.codingapi.script.request.ScriptRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroovyRuntimeContextTest {

    @Test
    void execute() {

        String script = """
                def run(request){
                    println($request.getRequest())
                    return request;
                }
                """;

        ScriptRequest<Integer> request = new ScriptRequest<>(script, Integer.class, 100);
        request.addBindObject("$request", request);

        long t1 = System.currentTimeMillis();
        int result = GroovyRuntimeContext.getInstance().run(request);
        long t2 = System.currentTimeMillis();
        System.out.println("groovy time:" + (t2 - t1));

        assertEquals(100, result);
    }

    @Test
    void batchTest() {
        int count = 100;
        execute();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            execute();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("total time:" + (t2 - t1));
    }
}