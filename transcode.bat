@ECHO OFF

FOR /R %%G IN (*.mp3) DO (CALL :SUB_OGG "%%G")
FOR /R %%G IN (*.mp3.ogg*) DO (CALL :SUB_RENAME "%%G")
FOR /R %%G IN (*.mp3) DO (CALL :SUB_DEL "%%G")
GOTO :eof

:SUB_OGG
 SET _firstbit=%1
 SET _qt="
 CALL SET _newnm=%%_firstbit:%_qt%=%%
 SET _commanm=%_newnm:,=_COMMA_%
 REM echo %_commanm%
 CALL "C:\Program Files\VideoLAN\VLC\vlc" -I dummy -vvv %1 --sout=#transcode{acodec="vorbis",ab="128"}:standard{access="file",dst="%_commanm%.ogg"} vlc://quit
GOTO :eof

:SUB_RENAME
 SET _origfnm=%1
 SET _endbit=%_origfnm:*.mp3=%
 CALL SET _newfilenm=%%_origfnm:.mp3%_endbit%=.ogg%%
 SET _newfilenm=%_newfilenm:_COMMA_=,%
 COPY %1 %_newfilenm%
 DEL %1
GOTO :eof

:SUB_DEL
 SET _origfnm=%1
 DEL %1
GOTO :eof

:eof