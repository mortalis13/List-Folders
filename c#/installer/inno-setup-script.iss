; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "List Folders"
#define MyAppVersion "1.0.0"
#define MyAppNameVersion "List Folders 1.0.0"
#define MyAppPublisher "mortalis"
#define MyAppExeName "List Folders.exe"
#define UninstallName "unins000.exe"

#define ExePath "C:\1-Roman\Documents\3-programming\2-projects\list-folders\c#\source\List Folders\List Folders\bin\Release"
#define FoldersPath "C:\1-Roman\Documents\3-programming\2-projects\list-folders\c#\source\List Folders\List Folders\Exports"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{023d8dc4-ada4-41d5-a007-97dcf7009da1}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppNameVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={pf}\{#MyAppNameVersion}
DefaultGroupName={#MyAppNameVersion}
OutputBaseFilename={#MyAppNameVersion}
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]

Source: "{#ExePath}\List Folders.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#FoldersPath}\export\*"; DestDir: "{app}\export"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "{#FoldersPath}\icons\*"; DestDir: "{app}\icons"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "{#FoldersPath}\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "{#FoldersPath}\templates\*"; DestDir: "{app}\templates"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppNameVersion}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{#UninstallName}"; Filename: "{app}\{#UninstallName}"
Name: "{commondesktop}\{#MyAppNameVersion}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: shellexec postinstall skipifsilent
