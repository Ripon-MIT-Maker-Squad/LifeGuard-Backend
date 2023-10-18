@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  LifeGuard-Backend startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and LIFE_GUARD_BACKEND_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\LifeGuard-Backend-0.0.10.jar;%APP_HOME%\lib\helidon-integrations-openapi-ui-3.2.2.jar;%APP_HOME%\lib\helidon-openapi-3.2.2.jar;%APP_HOME%\lib\helidon-health-checks-3.2.2.jar;%APP_HOME%\lib\helidon-health-3.2.2.jar;%APP_HOME%\lib\helidon-metrics-3.2.2.jar;%APP_HOME%\lib\helidon-metrics-service-api-3.2.2.jar;%APP_HOME%\lib\helidon-service-common-rest-3.2.2.jar;%APP_HOME%\lib\helidon-webserver-static-content-3.2.2.jar;%APP_HOME%\lib\helidon-webserver-cors-3.2.2.jar;%APP_HOME%\lib\helidon-webserver-3.2.2.jar;%APP_HOME%\lib\helidon-media-jsonp-common-2.0.0-M3.jar;%APP_HOME%\lib\helidon-microprofile-tests-junit5-3.2.2.jar;%APP_HOME%\lib\helidon-config-yaml-mp-3.2.2.jar;%APP_HOME%\lib\helidon-config-yaml-3.2.2.jar;%APP_HOME%\lib\postgresql-42.6.0.jar;%APP_HOME%\lib\smallrye-open-api-jaxrs-2.1.16.jar;%APP_HOME%\lib\smallrye-open-api-core-2.1.16.jar;%APP_HOME%\lib\helidon-media-jsonp-3.2.2.jar;%APP_HOME%\lib\helidon-common-key-util-3.2.2.jar;%APP_HOME%\lib\helidon-fault-tolerance-3.2.2.jar;%APP_HOME%\lib\helidon-common-configurable-3.2.2.jar;%APP_HOME%\lib\helidon-logging-common-3.2.2.jar;%APP_HOME%\lib\helidon-tracing-3.2.2.jar;%APP_HOME%\lib\helidon-metrics-api-3.2.2.jar;%APP_HOME%\lib\helidon-config-testing-3.2.2.jar;%APP_HOME%\lib\helidon-media-common-3.2.2.jar;%APP_HOME%\lib\helidon-tracing-config-3.2.2.jar;%APP_HOME%\lib\helidon-config-mp-3.2.2.jar;%APP_HOME%\lib\helidon-config-3.2.2.jar;%APP_HOME%\lib\helidon-common-media-type-3.2.2.jar;%APP_HOME%\lib\helidon-common-http-3.2.2.jar;%APP_HOME%\lib\helidon-common-reactive-3.2.2.jar;%APP_HOME%\lib\helidon-common-mapper-3.2.2.jar;%APP_HOME%\lib\helidon-jersey-client-3.2.2.jar;%APP_HOME%\lib\helidon-common-context-3.2.2.jar;%APP_HOME%\lib\helidon-common-service-loader-3.2.2.jar;%APP_HOME%\lib\yasson-2.0.4.jar;%APP_HOME%\lib\jakarta.json-2.0.0.jar;%APP_HOME%\lib\jakarta.json-2.0.0-module.jar;%APP_HOME%\lib\jackson-databind-2.13.0.jar;%APP_HOME%\lib\jackson-core-2.13.0.jar;%APP_HOME%\lib\jackson-annotations-2.13.0.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.13.0.jar;%APP_HOME%\lib\snakeyaml-2.0.jar;%APP_HOME%\lib\jandex-2.4.3.Final.jar;%APP_HOME%\lib\microprofile-openapi-api-3.0.jar;%APP_HOME%\lib\smallrye-open-api-ui-2.1.16.jar;%APP_HOME%\lib\helidon-common-3.2.2.jar;%APP_HOME%\lib\netty-codec-http-4.1.94.Final.jar;%APP_HOME%\lib\netty-handler-4.1.94.Final.jar;%APP_HOME%\lib\jersey-client-3.0.9.jar;%APP_HOME%\lib\jersey-hk2-3.0.9.jar;%APP_HOME%\lib\jersey-common-3.0.9.jar;%APP_HOME%\lib\jakarta.enterprise.cdi-api-3.0.0.jar;%APP_HOME%\lib\jakarta.interceptor-api-2.0.0.jar;%APP_HOME%\lib\jakarta.annotation-api-2.0.0.jar;%APP_HOME%\lib\microprofile-health-api-4.0.jar;%APP_HOME%\lib\helidon-health-common-3.2.2.jar;%APP_HOME%\lib\microprofile-metrics-api-4.0.jar;%APP_HOME%\lib\checker-qual-3.31.0.jar;%APP_HOME%\lib\parsson-1.0.2.jar;%APP_HOME%\lib\jakarta.json-api-2.0.2.jar;%APP_HOME%\lib\junit-platform-commons-1.7.0.jar;%APP_HOME%\lib\junit-jupiter-api-5.7.0.jar;%APP_HOME%\lib\hamcrest-all-1.3.jar;%APP_HOME%\lib\netty-codec-4.1.94.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.94.Final.jar;%APP_HOME%\lib\netty-transport-4.1.94.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.94.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.94.Final.jar;%APP_HOME%\lib\netty-common-4.1.94.Final.jar;%APP_HOME%\lib\microprofile-config-api-3.0.1.jar;%APP_HOME%\lib\jboss-logging-3.4.2.Final.jar;%APP_HOME%\lib\jakarta.json.bind-api-2.0.0.jar;%APP_HOME%\lib\jakarta.xml.bind-api-3.0.1.jar;%APP_HOME%\lib\jakarta.inject-api-2.0.1.jar;%APP_HOME%\lib\apiguardian-api-1.1.0.jar;%APP_HOME%\lib\opentest4j-1.2.0.jar;%APP_HOME%\lib\jakarta.ws.rs-api-3.0.0.jar;%APP_HOME%\lib\hk2-locator-3.0.3.jar;%APP_HOME%\lib\javassist-3.29.0-GA.jar;%APP_HOME%\lib\jakarta.activation-2.0.1.jar;%APP_HOME%\lib\jakarta.el-api-4.0.0.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.3.jar;%APP_HOME%\lib\hk2-api-3.0.3.jar;%APP_HOME%\lib\aopalliance-repackaged-3.0.3.jar;%APP_HOME%\lib\hk2-utils-3.0.3.jar


@rem Execute LifeGuard-Backend
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %LIFE_GUARD_BACKEND_OPTS%  -classpath "%CLASSPATH%" com.riponmakers.LifeGuardWebServer.Main %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable LIFE_GUARD_BACKEND_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%LIFE_GUARD_BACKEND_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
