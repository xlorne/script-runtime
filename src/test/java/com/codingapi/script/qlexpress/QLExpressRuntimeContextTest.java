package com.codingapi.script.qlexpress;

import com.codingapi.springboot.framework.script.request.GroovyMethodScript;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QLExpressRuntimeContextTest {

    @Test
    void execute() {
        String script = """
                function run(request){
                    System.out.println($request.getRequest());
                    return request;
                }
                """;
        GroovyMethodScript<Integer> request = new GroovyMethodScript<>(script, Integer.class, 100);
        request.addBindObject("$request", request);

        long t1 = System.currentTimeMillis();
        int result = QLExpressRuntimeContext.getInstance().run(request);
        long t2 = System.currentTimeMillis();
        System.out.println("ql-express time:" + (t2 - t1));
        System.out.println(result);
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