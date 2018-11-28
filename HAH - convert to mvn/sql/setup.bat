@echo off
pushd %~dp0
rm ..\.git\hooks\post-checkout
mklink /H ..\.git\hooks\post-checkout .\post-checkout
pause