#!/bin/bash

mvn -Dmaven.test.skip=true clean package docker:build