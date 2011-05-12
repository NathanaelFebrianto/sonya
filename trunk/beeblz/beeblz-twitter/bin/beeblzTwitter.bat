@echo off
rem WINDOWS LAUNCH SCRIPT

rem ENVIRONMENT VARIABLES TO MODIFY


set JDK_PATH="D:\dev\tools\Java\jdk1.6.0_22"

rem ----------------------------------

set COMMONS_LANG="lib\commons-lang-2.3.jar"
set JTA="lib\jta-1.1.jar"
set JUNIT="lib\junit-3.8.1.jar"
set JYAML="lib\jyaml-1.3.jar"
set LOG4J="lib\log4j-1.2.15.jar"
set MYBATIS="lib\mybatis-3.0.4.jar"
set MYSQL_CONNECTOR_JAVA="lib\mysql-connector-java-5.1.9.jar"
set QUARTZ="lib\quartz-1.8.3.jar"
set SLF4J_API="lib\slf4j-api-1.5.10.jar"
set SLF4J_LOG4J="lib\slf4j-log4j12-1.5.10.jar"
set TWITTER4J_CORE="lib\twitter4j-core-2.2.3-SNAPSHOT.jar"
set BEEBLZ_TWITTER="lib\beeblz-twitter-0.1.3.jar"

set LIBS=%COMMONS_LANG%;%JTA%;%JUNIT%;%JYAML%;%LOG4J%;%MYBATIS%;%MYSQL_CONNECTOR_JAVA%;%QUARTZ%;%SLF4J_API%;%SLF4J_LOG4J%;%TWITTER4J_CORE%;%BEEBLZ_TWITTER%;bin\

%JDK_PATH%\bin\java -Xmx512m -classpath %LIBS% com.beeblz.twitter.collector.TwitterCollectorJobTrigger %1 %2 %3 %4 %5 %6 %7 %8 %9