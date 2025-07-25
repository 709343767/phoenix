@echo off

cd ..

mvnw -Dmaven.test.skip=true clean package

@pause