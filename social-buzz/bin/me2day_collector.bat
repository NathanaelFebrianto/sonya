@echo off
rem WINDOWS LAUNCH SCRIPT

rem ENVIRONMENT VARIABLES TO MODIFY


set JDK_PATH="D:\tools\Java\jdk1.6.0_18"

rem ----------------------------------

set COMMONS_IO="lib\commons-io-2.0.1.jar"
set JFLEX="lib\jflex-1.4.3.jar"
set JTA="lib\jta-1.1.jar"
set LOG4J="lib\log4j-1.2.14.jar"
set MYBATIS="lib\mybatis-3.0.4.jar"
set MYSQL_CONNECTOR_JAVA="lib\mysql-connector-java-5.1.9.jar"
set QUARTZ="lib\quartz-1.8.3.jar"
set SLF4J_API="lib\slf4j-api-1.5.10.jar"
set SLF4J_LOG4J="lib\slf4j-log4j12-1.5.10.jar"
set TWITTER4J_CORE="lib\twitter4j-core-2.2.4.jar"
set LUCENE_ANALYZER_KR="lib\lucene-analyzer-kr-0.0.1-SNAPSHOT.jar"
set LUCENE_CORE="lib\lucene-core-3.0.0.jar"
set SOCIAL_BUZZ="lib\social-buzz-0.0.1.jar"

set LIBS=%COMMONS_IO%;%JFLEX%;%JTA%;%LOG4J%;%MYBATIS%;%MYSQL_CONNECTOR_JAVA%;%QUARTZ%;%SLF4J_API%;%SLF4J_LOG4J%;%TWITTER4J_CORE%;%LUCENE_ANALYZER_KR%;%LUCENE_CORE%;%SOCIAL_BUZZ%;bin\

%JDK_PATH%\bin\java -Xmx512m -classpath %LIBS% com.nhn.socialbuzz.me2day.collector.Me2dayCollectorJobTrigger %1 %2 %3 %4 %5 %6 %7 %8 %9