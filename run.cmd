@echo off

rem for /F "tokens=2" %%i in ('date /t') do set mydate=%%i
rem set mytime=%time%
rem echo Current time is %mydate%:%mytime% >> run.log
rem Waiting 10 seconds
ping 1.0.0.1 -n 10 -w 1000
echo %date%-%time% >> run.log
echo run start >> run.log
SerialSend.exe /baudrate 9600 /devnum 5 "Hello world!">> run.log