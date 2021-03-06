package com.sysiq.tools;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.sysiq.tools.zkcopy.*;
import com.sysiq.tools.zkcopy.reader.Reader;
import com.sysiq.tools.zkcopy.writer.Writer;

public class ZkCopy
{
    
    private static Logger logger = Logger.getLogger(ZkCopy.class);
    private static final int DEFAULT_THREADS_NUMBER = 1;
    private static final boolean DEFAULT_REMOVE_DEPRECATED_NODES = false;
    
    /**
     *
     */
    public static void main(String[] args)
    {
        String loggerConfig=System.getProperty("logger.config");
        
        if (loggerConfig == null) {        
            BasicConfigurator.configure();
        } else {
            PropertyConfigurator.configure(loggerConfig);
        }
        
        String source = System.getProperty("source");
        String destination = System.getProperty("destination");
        
        if (source == null) {
            help();
            return;
        }
        int threads = getThreadsNumber();
        boolean removeDeprecatedNodes = getRemoveDeprecatedNodes();
        logger.info("Threads Number = " + threads);
        Reader reader = new Reader(source, threads);
        Node root = reader.read();
        
        Writer writer = new Writer(destination, root, removeDeprecatedNodes);
        writer.write();
    }
    
    private static int getThreadsNumber() {
        String threads = System.getProperty("threads");        
        int n = DEFAULT_THREADS_NUMBER;
        if (threads == null) {
            return DEFAULT_THREADS_NUMBER;
        }
        
        try {
            n = Integer.valueOf(threads).intValue();
        }
        catch(NumberFormatException e) {
            logger.error("Can't parse threads number - \"" + threads + "\"", e);
        }
        return n;
    }
    
    private static boolean getRemoveDeprecatedNodes() {
        String s = System.getProperty("removeDeprecatedNodes");        
        boolean ans = DEFAULT_REMOVE_DEPRECATED_NODES;
        if (s == null) {
            return DEFAULT_REMOVE_DEPRECATED_NODES;
        }        
        try {
            ans = Boolean.valueOf(s).booleanValue();
        }
        catch(NumberFormatException e) {
            logger.error("Can't parse 'removeDeprecatedNodes' - \"" + s + "\"", e);
        }
        return ans;
    }
    
    private static void help() {
        System.out.print(
                        "ZkCopy version 0.1\n" +
                        "Usage:\n" +
                        "\tjava " +
                        "-Dlogger.config=\"log4j.properties\" " +
                        "-Dsource=\"server:port/path\" " +
                        "-Ddestination=\"server:port/path\" " +
                        "-Dthreads=10 " +
                        "-DremoveDeprecatedNodes=true " +
                        "-jar zkcopy.jar\n"
                        );
    }
 
}


