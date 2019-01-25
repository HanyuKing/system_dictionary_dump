package com.mkyong.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;

public class SystemDictionaryDump {

    public static final String javaCode =
            "\n" +
                    "import sun.jvm.hotspot.debugger.Address;\n" +
                    "import sun.jvm.hotspot.memory.Dictionary;\n" +
                    "import sun.jvm.hotspot.memory.DictionaryEntry;\n" +
                    "import sun.jvm.hotspot.memory.SystemDictionary;\n" +
                    "import sun.jvm.hotspot.oops.*;\n" +
                    "import sun.jvm.hotspot.runtime.VM;\n" +
                    "import sun.jvm.hotspot.tools.Tool;\n" +
                    "import sun.jvm.hotspot.utilities.BasicHashtableEntry;\n" +
                    "\n" +
                    "import java.util.*;\n" +
                    "import java.util.concurrent.ConcurrentHashMap;\n" +
                    "\n" +
                    "public class PrintSystemDictionary {\n" +
                    "    public static void main(String args[]) {\n" +
                    "        try {\n" +
                    "            System.out.println(\"Hanyu King \" + args[0]);\n" +
                    "            new PrintSystemDictionaryTask().doStart(args);\n" +
                    "            System.out.println(\"Hanyu King end \" + args[0]);\n" +
                    "        } catch (Throwable e) {\n" +
                    "            for(StackTraceElement stackTraceElement : e.getStackTrace()) {\n" +
                    "                System.out.println(stackTraceElement.toString());\n" +
                    "            }\n" +
                    "            System.out.println(e.toString());\n" +
                    "            System.out.println(\"cause: \" + e.getCause());\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "class PrintSystemDictionaryTask extends Tool {\n" +
                    "    public PrintSystemDictionaryTask() {\n" +
                    "        System.out.println(\"new PrintSystemDictionaryTask \");\n" +
                    "    }\n" +
                    "\n" +
                    "    class SystemDictionaryPrinter extends Dictionary {\n" +
                    "\n" +
                    "        public SystemDictionaryPrinter(Address addr) {\n" +
                    "            super(addr);\n" +
                    "        }\n" +
                    "\n" +
                    "        void print() {\n" +
                    "            long[] bucketSize = new long[tableSize()];\n" +
                    "            System.out.println(\"Hanyu King tableSize \" + tableSize());\n" +
                    "            Set<String> classLoaders = new HashSet<String>();\n" +
                    "            Map<String, Long> classTotalMap = new ConcurrentHashMap<String, Long>();\n" +
                    "\n" +
                    "            for(int i = 0; i < tableSize(); i++) {\n" +
                    "                BasicHashtableEntry currHashtableEntry = bucket(i);\n" +
                    "                if(currHashtableEntry == null) {\n" +
                    "                    continue;\n" +
                    "                }\n" +
                    "                DictionaryEntry dictionaryEntry = (DictionaryEntry) currHashtableEntry;\n" +
                    "                Klass klass = dictionaryEntry.klass();\n" +
                    "\n" +
                    "                //System.out.println(klass.getName().asString() + \"->\" + getClassLoaderOopFrom((InstanceKlass) klass));\n" +
                    "                classLoaders.add(getClassLoaderOopFrom((InstanceKlass) klass));\n" +
                    "\n" +
                    "                while (currHashtableEntry != null) {\n" +
                    "\n" +
                    "                    bucketSize[i]++;\n" +
                    "\n" +
                    "                    String className = ((DictionaryEntry) currHashtableEntry).klass().getName().asString();\n" +
                    "\n" +
                    "                    Long loadedClassTotal;\n" +
                    "                    if((loadedClassTotal = classTotalMap.get(className)) != null) {\n" +
                    "                        classTotalMap.put(className, loadedClassTotal + 1);\n" +
                    "                    } else {\n" +
                    "                        classTotalMap.put(className, 1L);\n" +
                    "                    }\n" +
                    "\n" +
                    "                    currHashtableEntry = currHashtableEntry.next();\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            for(Map.Entry<String, Long> entry : classTotalMap.entrySet()) {\n" +
                    "                System.out.println(entry.getKey() + \" \" + entry.getValue());\n" +
                    "            }\n" +
                    "\n" +
                    "            List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(classTotalMap.entrySet());\n" +
                    "\n" +
                    "            Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {\n" +
                    "                @Override\n" +
                    "                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {\n" +
                    "                    return (int) (o1.getValue() - o2.getValue());\n" +
                    "                }\n" +
                    "            });\n" +
                    "\n" +
                    "            for(Map.Entry<String,Long> mapping : list){\n" +
                    "                System.out.println(mapping.getKey()+\" : \"+mapping.getValue());\n" +
                    "            }\n" +
                    "\n" +
                    "//            for(int i = 0; i < bucketSize.length; i++) {\n" +
                    "//                System.out.println(bucketSize[i]);\n" +
                    "//            }\n" +
                    "\n" +
                    "//            System.out.println(classLoaders);\n" +
                    "        }\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    private static String getClassLoaderOopFrom(InstanceKlass klass) {\n" +
                    "        Oop loader = klass.getClassLoader();\n" +
                    "        return loader != null\n" +
                    "                ? getClassNameFrom((InstanceKlass) loader.getKlass()) + \" @ \" + loader.getHandle()\n" +
                    "                : \"<bootstrap>\";\n" +
                    "    }\n" +
                    "\n" +
                    "    private static String getClassNameFrom(InstanceKlass klass) {\n" +
                    "        return klass != null ? klass.getName().asString().replace('/', '.') : null;\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "    void doStart(String[] args) {\n" +
                    "        System.out.println(\"doStart \");\n" +
                    "        PrintSystemDictionaryTask pst = new PrintSystemDictionaryTask();\n" +
                    "        pst.start(args);\n" +
                    "        System.out.println(\"start.... \");\n" +
                    "        pst.stop();\n" +
                    "        System.out.println(\"stop.... \");\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void run() {\n" +
                    "        SystemDictionary systemDictionary = VM.getVM().getSystemDictionary();\n" +
                    "        Dictionary dictionary = systemDictionary.dictionary();\n" +
                    "\n" +
                    "        new SystemDictionaryPrinter(dictionary.getAddress()).print();\n" +
                    "    }\n" +
                    "}";

    public static void runJavaCode(String pid, String sep) throws Exception {

        String dirName = System.getProperty("user.dir");
        String fileName = "PrintSystemDictionary.java";

        File dir = new File(dirName);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirName + File.separator + fileName);
        FileWriter fw = new FileWriter(file);
        fw.write(javaCode);
        fw.flush();
        fw.close();

        runJavacCompile(fileName, sep);

        Process process = runJavaExec(fileName, pid, sep);

        readResult(process);
    }

    public static void readResult(Process process) throws Exception {
        try (InputStream inputStream = process.getInputStream();
             FileOutputStream fos = new FileOutputStream("system_dictionary_dump.log")){

            byte[] b = new byte[1024];
            int len = -1;

            String str = "";
            while ((len = inputStream.read(b)) > 0) {
                fos.write(b, 0, len);
            }

            fos.flush();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void runJavacCompile(String fileName, String sep) throws Exception {
        String cmd = getJavaHome() + "/bin/javac -cp ." + sep + getJavaHome() + "/lib/sa-jdi.jar " + fileName;
        Runtime.getRuntime().exec(cmd);
    }

    public static Process runJavaExec(String fileName, String pid, String sep) throws Exception {
        String cmd = getJavaHome() + "/bin/java -cp ."+ sep + getJavaHome() + "/lib/sa-jdi.jar " + fileName.replace(".java", "") + " " + pid;
        System.out.println(cmd);
        return Runtime.getRuntime().exec(cmd);
    }

    public static String getJavaHome() {
        return System.getenv("JAVA_HOME");
    }

    public void dump(String[] args) {
        try {
            SystemDictionaryDump.runJavaCode(args[0], args[1]);
        } catch (Throwable e) {
            for(StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement.toString());
            }
            System.out.println(e.toString());
            System.out.println("cause: " + e.getCause());
        }
    }
}
