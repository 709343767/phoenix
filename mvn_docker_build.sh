#!/bin/bash

mvnw -Dmaven.test.skip=true clean package docker:build