@echo off

mkdir D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\firebird-common.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\firebird-common*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\firebird-io.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\firebird-io*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\firebird-collector.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\firebird-collector*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\firebird-graph.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\firebird-graph*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\firebird-analyzer.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\firebird-analyzer*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\forms-1.0.5.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\forms-1.0.5.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-3d.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-3d-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-3d-demos.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-3d-demos-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-algorithms.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-algorithms-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-api.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-api-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-graph-impl.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-graph-impl-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-io.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-io-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-jai.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-jai-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-jai-samples.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-jai-samples-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-samples.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-samples-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\jung-visualization.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\jung-visualization-2.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\looks-1.2.2.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\looks-1.2.2.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\looks-win-1.2.0.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\looks-win-1.2.0.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\plastic-1.2.0.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\plastic-1.2.0.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\collections-generic-4.01.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\collections-generic-4.01.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\colt-1.2.0.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\colt-1.2.0.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\concurrent-1.3.4.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\concurrent-1.3.4.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\stax-api-1.0.1.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\stax-api-1.0.1.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\wstx-asl-3.2.6.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\wstx-asl-3.2.6.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\twitter4j-core.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\twitter4j-core-*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\substance.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\substance-*.jar firebird

D:\dev\tools\Java\jdk1.6.0_22\bin\jarsigner.exe -keystore D:\dev\workspace\firebird\firebird-web\jarsigner\firebird.keystore -storepass firebird -signedjar D:\dev\workspace\firebird\firebird-web\target\firebird-web\client-lib\trident.jar D:\dev\workspace\firebird\firebird-web\target\firebird-web\WEB-INF\lib\trident-*.jar firebird

