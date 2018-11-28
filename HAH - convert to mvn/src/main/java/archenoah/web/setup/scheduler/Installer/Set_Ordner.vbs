Set wshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")
Set args = WScript.Arguments
If args.Count < 1 Then
WScript.Echo "Dateiname fehlt."
WScript.Quit
End If
Company = args(0)
desktop = wshShell.SpecialFolders("AllUsersPrograms") &"\"& Company &"\"
If fso.FolderExists(desktop) Then 
fso.DeleteFolder wshShell.SpecialFolders("AllUsersPrograms") &"\"& Company,true
End If 
Set f = fso.CreateFolder(desktop)