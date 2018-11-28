@echo on
set Company=Isconet
CALL "%~d0%~p0Set_Ordner.vbs" %Company%
CALL "%~d0%~p0Set_Shortcut.vbs" "%~d0%~p0AutoInstaller.bat" Install.ico Install_Service %Company%
CALL "%~d0%~p0Set_Shortcut.vbs" "%~d0%~p0AutoDeinstaller.bat" Deinstall.ico Deinstall_Service %Company%
pause
echo Setup Abgeschlossen !