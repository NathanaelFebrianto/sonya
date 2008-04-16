package org.sonya.webapp.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationResourcesInitializer {
	
	private static final String COMMENT = "#";
    private static final String UNDERBAR = "_";
    private static final Log log = LogFactory.getLog(ApplicationResourcesInitializer.class);
    private String defaultLocale = null;
    
    public void initResources(ServletContext ctx) throws Exception{
        log.debug("Initialize ApplicationResources...");
        defaultLocale = ctx.getInitParameter("javax.servlet.jsp.jstl.fmt.fallbackLocale");
        Hashtable localeMap = new Hashtable();
        readResources(localeMap, new File(ctx.getRealPath("/WEB-INF/lib")));
        writeResources(localeMap, new File(ctx.getRealPath("/WEB-INF/classes")));
    }

    private void writeResources(Hashtable localeMap, File classesPath) throws FileNotFoundException, IOException {
        log.debug("Writing from each resource file to ApplicationResouces...");
        ByteArrayOutputStream bos = null;
        Enumeration en = localeMap.keys();
        FileOutputStream fos = null;
        String key = null;
        ByteArrayInputStream bis = null;
        while(en.hasMoreElements()) {
            key = (String)en.nextElement();
            log.debug("Writing resources locale : " + key);
            bos = (ByteArrayOutputStream)localeMap.get(key);
            bis = new ByteArrayInputStream(bos.toByteArray());
            fos = new FileOutputStream(new File(classesPath, "ApplicationResources_" + key + ".properties"));
            int buf = 0;
            while ( (buf = bis.read()) != -1) {
                fos.write(buf);
            }
            fos.flush();
            fos.close();
            bos.reset();
            bis.reset();
        }
        log.debug("Complete writing"); 
    }

    private void readResources(Hashtable localeMap, File libPath) throws IOException {
        log.debug("Loading resource files from module...");
        FilenameFilter fnFilter = new FilenameFilter () {
            public boolean accept(File dir, String name) {
                return StringUtils.contains(name, "sonya-");
            }
        };
        File[] moduleList = libPath.listFiles(fnFilter);
        String resourcesSurffix = ".properties";
        String resourcesPrefix = "sonya-";
        String filename = null;
        String locale = null;
        ByteArrayOutputStream bos = null;
        int resourcesCount = 0;
        for (int i = 0; i < moduleList.length; i++) {
            JarFile jarFile = new JarFile(moduleList[i]);
            Enumeration en = jarFile.entries();
            while(en.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)en.nextElement();
                filename = entry.getName();
                if (filename.startsWith(resourcesPrefix) && StringUtils.contains(filename, resourcesSurffix)) {
                    log.debug("Reading " + filename + " from " + moduleList[i].getName());
                    locale = filename.substring(
                            filename.length() - resourcesSurffix.length() - 2, 
                            filename.length() - resourcesSurffix.length());
                    if (filename.indexOf(UNDERBAR + locale) == -1)
                        locale = defaultLocale;
                    InputStream is = jarFile.getInputStream(entry);
                    if (localeMap.containsKey(locale)) {
                        bos = (ByteArrayOutputStream)localeMap.get(locale);
                        wirteToMemory(is, bos, moduleList[i].getName());
                    } else {
                        bos = new ByteArrayOutputStream();
                        wirteToMemory(is, bos, moduleList[i].getName());
                        localeMap.put(locale, bos);
                    }
                    is.close();
                    resourcesCount++;
                }
            }
        }
        log.debug("Loading complete : " + resourcesCount);
    }
    
    private void wirteToMemory(InputStream src, OutputStream dest, String moduleName) throws IOException {
        InputStreamReader isr = new InputStreamReader(src);
        BufferedReader br = new BufferedReader(isr);
        
        PrintWriter pw = new PrintWriter(dest);
        String buf = null;
        pw.println(COMMENT);
        pw.println(COMMENT + " " + moduleName);
        pw.println(COMMENT);
        while((buf = br.readLine())!= null) {
            if (buf.trim().length() != 0 && !buf.trim().startsWith(COMMENT)) {
                pw.println(buf.trim());
                pw.flush();
            }
        }
    }
}
