@echo off
echo %date%-%time% >> run.log
echo finish >> run.log
rem rem 115200  9600
rem SerialSend.exe /baudrate 115200 /devnum 6 "F">> run.log

