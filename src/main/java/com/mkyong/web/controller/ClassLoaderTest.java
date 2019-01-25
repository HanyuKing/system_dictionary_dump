package com.mkyong.web.controller;

public class ClassLoaderTest {
    public static void main(String[] args) {
        try {
            System.out.println("sun.boot.class.path -> " + System.getProperty("sun.boot.class.path"));
            System.out.println("java.ext.dirs -> " + System.getProperty("java.ext.dirs"));
            System.out.println("java.class.path -> " + System.getProperty("java.class.path"));
            Class.forName("com.mkyong.web.controller.MyClass", false, ClassLoaderTest.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class MyClass {}