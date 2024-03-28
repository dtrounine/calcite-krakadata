#!/bin/bash

./gradlew \
	-PallowUncommittedChanges \
	-PasfTestNexusUsername=lobster \
	-PasfTestNexusPassword=herring \
	server:publish
