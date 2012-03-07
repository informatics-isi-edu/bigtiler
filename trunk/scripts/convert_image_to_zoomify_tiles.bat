@echo off
rem
rem Copyright 2011 University of Southern California
rem
rem Licensed under the Apache License, Version 2.0 (the "License");
rem you may not use this file except in compliance with the License.
rem You may obtain a copy of the License at
rem
rem   http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.
rem
set DIR=%~dp0
set CP=%DIR%
for %%i in (%DIR%\lib\*.jar) do call %DIR%\cpappend.bat %%i
call %DIR%\cpappend.bat %DIR%\dll
if not "%CP%" == "" set CLASSPATH=%CLASSPATH%;%CP%

java.exe %JAVA_OPTS% -cp "%CLASSPATH%" edu.isi.misd.image.gateway.conversion.ConvertImageToZoomifyTiles %*

