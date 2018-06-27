@echo off
echo %date%-%time% >> run.log
echo run start >> run.log
rem 115200  9600
SerialSend.exe /baudrate 115200 /devnum 6 "S">> run.log
rem Waiting 0.5 second and PIK
ping 1.0.0.1 -n 1 -w 500
