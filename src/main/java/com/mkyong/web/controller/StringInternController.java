package com.mkyong.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.ManagementFactory;

@Controller("/stringtable")
public class StringInternController {

    @RequestMapping("loop")
    @ResponseBody
    public String loop() throws InterruptedException {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        for(int i = 0; ; i++) {
            ("Intern_" + i).intern();
            Thread.sleep(1000);
        }
    }

    @RequestMapping("/setHanyuKing")
    @ResponseBody
    public String setHanyuKing() {
        String s = "Hanyu King";
        return s;
    }

    @RequestMapping("/setString")
    @ResponseBody
    public String setString(String string) {
        String s = string;
        s = s + " China";
        return s;
    }

    @RequestMapping("/stringEquals")
    @ResponseBody
    public Object stringEquals(String ss) {
        return ss + "HanyuKing" == ss + "HanyuKing";
    }
}
