@echo off

cd ..

mvnd -Dmaven.test.skip=true clean package

@pause