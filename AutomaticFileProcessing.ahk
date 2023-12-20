#SingleInstance Force
SendMode Input
SetWorkingDir %A_ScriptDir%
    Sleep 4000
    Click, 1113, 674
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput ^p
    Sleep 500
    SendInput {Enter}
    Sleep 500
    Loop 6
    {
        SendInput, {Tab}
        Sleep, 500
    }
    SendInput {Enter}
    Sleep 500
    FileReadLine, FirstLinePath, %USERPROFILE%\Documents\filepaths.txt, 1
    Sleep 500
    SplitPath, FirstLinePath, directory, drive
    Sleep 500
    Clipboard := drive
    Sleep 500
    SendInput #{v}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput {Enter}
    Sleep 1000
    SendInput, {Tab}
    Sleep, 2000
    Loop 5
    {
        SendInput, {Tab}
        Sleep, 500
    }
    FileReadLine, FirstLineName, %USERPROFILE%\Documents\filenames.txt, 1
    Sleep 500
    Clipboard := FirstLineName
    Sleep 500
    SendInput #{v}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput {Enter}
    Sleep 5000

    ;PDF tehty

    ;Aloitetaan IESNA
    SendInput !f
    Sleep 500
    Loop 4
    {
        SendInput, {Down}
        Sleep, 200
    }
    SendInput {Enter}
    Sleep 500
    SendInput {Down}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput #{v}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    Loop 23
    {
        SendInput, {Tab}
        Sleep, 100
    }
    SendInput {Right}
    Sleep 500
    Loop 3
    {
        SendInput, {Tab}
        Sleep, 100
    }
    SendInput #{v}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    Loop 9
    {
        SendInput, {Tab}
        Sleep, 100
    }
    SendInput {Enter}
    Sleep 500
    SendInput #{v}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    ;Lisätään nimen perään _IESNA
    SendInput {End}
    Sleep 500
    SendInput _IESNA
    Sleep 500
    SendInput {Enter}
    Sleep 800
    SendInput !{F4}
    Sleep 500
    ;_IESNA tehty

    ;Poistetaan 90deg
    SendInput {F10}
    Sleep 500
    Loop 4
    {
        SendInput, {Tab}
        Sleep, 100
    }
    SendInput {Space}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    ;90deg poistettu
    

    ;Tehdään IES
    SendInput !f
    Sleep 500
    Loop 4
    {
        SendInput, {Down}
        Sleep, 100
    }
    SendInput {Enter}
    Sleep 500
    SendInput {Down}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    Loop 21
    {
        SendInput, {Tab}
        Sleep, 100
    }
    SendInput {Enter}
    Sleep 500
    SendInput #{v}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput !{F4}
    Sleep 500
    

   

    ;Tehdään LDT
    SendInput !f
    Sleep 500
    Loop 4
    {
        SendInput, {Down}
        Sleep, 100
    }
    SendInput {Enter}
    Sleep 500
    Loop 2
    {
        SendInput, {Down}
        Sleep, 200
    }
    SendInput {Enter}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput {Enter}
    Sleep 500
    SendInput !{F4}
    Sleep 500
    SendInput !{F4}
    Sleep 500
    SendInput {Right}
    Sleep 500
    SendInput {Space}
    Sleep 500

    filePath := userProfile . "\Documents\filenames.txt"
    ; Read the file contents
    FileRead, fileContents, %filePath%

    ; Find the position of the first line break
    lineBreakPos := InStr(fileContents, "`n")

    ; Remove the first line if a line break is found
    if (lineBreakPos > 0)
    {
        newContents := SubStr(fileContents, lineBreakPos + 1)

        ; Delete the original file
        FileDelete, %filePath%

        ; Append the modified content to the file
        FileAppend, %newContents%, %filePath%
    }
    ExitApp
    
    
; Hätästop jos koodi onki vituillaan :D
Esc::Pause

return
#SingleInstance Force

