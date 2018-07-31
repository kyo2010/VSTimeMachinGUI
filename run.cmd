rem Script is executed when Race starts. for gate
@echo off
echo %date%-%time% >> run.log
rem echo run start >> run.log
rem 115200  9600
rem SerialSend.exe /baudrate 115200 /devnum 6 "S">> run.log
rem Waiting 0.5 second and PIK
rem ping 1.0.0.1 -n 1 -w 500
