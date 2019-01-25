
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.memory.Dictionary;
import sun.jvm.hotspot.memory.DictionaryEntry;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.oops.*;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;
import sun.jvm.hotspot.utilities.BasicHashtableEntry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PrintSystemDictionary {
    public static void main(String args[]) {
        try {
            System.out.println("Hanyu King " + args[0]);
            new PrintSystemDictionaryTask().doStart(args);
            System.out.println("Hanyu King end " + args[0]);
        } catch (Throwable e) {
            for(StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement.toString());
            }
            System.out.println(e.toString());
            System.out.println("cause: " + e.getCause());
        }
    }
}

class PrintSystemDictionaryTask extends Tool {
    public PrintSystemDictionaryTask() {
        System.out.println("new PrintSystemDictionaryTask ");
    }

    class SystemDictionaryPrinter extends Dictionary {

        public SystemDictionaryPrinter(Address addr) {
            super(addr);
        }

        void print() {
            long[] bucketSize = new long[tableSize()];
            System.out.println("Hanyu King tableSize " + tableSize());
            Set<String> classLoaders = new HashSet<String>();
            Map<String, Long> classTotalMap = new ConcurrentHashMap<String, Long>();

            for(int i = 0; i < tableSize(); i++) {
                BasicHashtableEntry currHashtableEntry = bucket(i);
                if(currHashtableEntry == null) {
                    continue;
                }
                DictionaryEntry dictionaryEntry = (DictionaryEntry) currHashtableEntry;
                Klass klass = dictionaryEntry.klass();

                //System.out.println(klass.getName().asString() + "->" + getClassLoaderOopFrom((InstanceKlass) klass));
                classLoaders.add(getClassLoaderOopFrom((InstanceKlass) klass));

                while (currHashtableEntry != null) {

                    bucketSize[i]++;

                    String className = ((DictionaryEntry) currHashtableEntry).klass().getName().asString();

                    Long loadedClassTotal;
                    if((loadedClassTotal = classTotalMap.get(className)) != null) {
                        classTotalMap.put(className, loadedClassTotal + 1);
                    } else {
                        classTotalMap.put(className, 1L);
                    }

                    currHashtableEntry = currHashtableEntry.next();
                }
            }

            for(Map.Entry<String, Long> entry : classTotalMap.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }

            List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(classTotalMap.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
                @Override
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                    return (int) (o1.getValue() - o2.getValue());
                }
            });

            for(Map.Entry<String,Long> mapping : list){
                System.out.println(mapping.getKey()+" : "+mapping.getValue());
            }

//            for(int i = 0; i < bucketSize.length; i++) {
//                System.out.println(bucketSize[i]);
//            }

//            System.out.println(classLoaders);
        }

    }

    private static String getClassLoaderOopFrom(InstanceKlass klass) {
        Oop loader = klass.getClassLoader();
        return loader != null
                ? getClassNameFrom((InstanceKlass) loader.getKlass()) + " @ " + loader.getHandle()
                : "<bootstrap>";
    }

    private static String getClassNameFrom(InstanceKlass klass) {
        return klass != null ? klass.getName().asString().replace('/', '.') : null;
    }



    void doStart(String[] args) {
        System.out.println("doStart ");
        PrintSystemDictionaryTask pst = new PrintSystemDictionaryTask();
        pst.start(args);
        System.out.println("start.... ");
        pst.stop();
        System.out.println("stop.... ");
    }

    @Override
    public void run() {
        SystemDictionary systemDictionary = VM.getVM().getSystemDictionary();
        Dictionary dictionary = systemDictionary.dictionary();

        new SystemDictionaryPrinter(dictionary.getAddress()).print();
    }
}