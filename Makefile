run: build
	@echo Running...
	@java -cp bin UnboundedInteger < input.txt
.PHONY: run

build:
	@echo Starting build...
	@javac src/UnboundedInteger.java
	@echo Finishing building.
.PHONY: build
