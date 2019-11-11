package com.hyr.disruptor.demo.api;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/*
 * @author hyr
 * @date 19-11-11-下午3:07
 * */
public class KeyUtil {
    public static String generateUUID(){
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        return timeBasedGenerator.generate().toString();
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println(generateUUID());
        System.out.println(generateUUID());
        System.out.println(generateUUID());
    }
}
