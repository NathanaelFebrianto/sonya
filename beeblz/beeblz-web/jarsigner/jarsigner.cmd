@echo off

mkdir D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\beeblz-facebook.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\beeblz-facebook*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\derby.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\derby*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\jung-algorithms.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\jung-algorithms*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\jung-api.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\jung-api*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\jung-graph-impl.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\jung-graph-impl*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\jung-io.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\jung-io*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\log4j.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\log4j*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\restfb.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\restfb*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\substance.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\substance*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\trident.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\trident*.jar beeblz

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\beeblz\beeblz-web\jarsigner\beeblz.keystore -storepass beeblz1111 -signedjar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\signedjar\collections-generic.jar D:\dev\workspace\beeblz\beeblz-web\target\beeblz-web-0.1.0\WEB-INF\lib\collections-generic*.jar beeblz

