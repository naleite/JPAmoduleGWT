#!/bin/sh
mvn clean compile gwt:compile  package tomcat7:run-war-only