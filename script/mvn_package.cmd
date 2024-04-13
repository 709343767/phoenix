@echo off

cd ..

mvn -Dmaven.test.skip=true clean package

@pause