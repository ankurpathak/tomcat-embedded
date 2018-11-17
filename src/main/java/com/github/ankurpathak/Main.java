package com.github.ankurpathak;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {


        Path basePath = Paths.get("");
        Path webappPath = Paths.get("","src","main","webapp");
        Path webxmlPath = webappPath.resolve("WEB-INF").resolve( "web.xml");
        Tomcat tomcat = new Tomcat();


        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));

        tomcat.setPort(Integer.valueOf(properties.getProperty("server.port", "8080")));
        tomcat.setBaseDir(basePath.toAbsolutePath().toString());
        tomcat.getHost().setDeployOnStartup(true);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setAppBase(basePath.toAbsolutePath().toString());
        tomcat.getConnector();


        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappPath.toAbsolutePath().toString());
        System.out.println("configuring app with basedir: " + webappPath.toAbsolutePath().toString());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        Path additionWebInfClassesPath = Paths.get("", "target", "classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClassesPath.toAbsolutePath().toString(), "/"));
        ctx.setResources(resources);
        ctx.setDefaultWebXml(webxmlPath.toAbsolutePath().toString());
        tomcat.start();
        tomcat.getServer().await();
    }
}