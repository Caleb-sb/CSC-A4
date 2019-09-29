# Assignement 4 Makefile
# Caleb Bredekamp
# 27 September 2019

JAVAC=/usr/bin/javac
SRCDIR=src
TOTALWORDS=20
NOWORDS=4
DICT="src/example_dict.txt"

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(SRCDIR)/*.java

CLASSES= \
 Score.class\
 WordDictionary.class\
 WordRecord.class\
 Controller.class\
 WordPanel.class\
 WordApp.class

classes: $(CLASSES:%.class=$(SRCDIR)/%.class)

default: classes

clean:
	rm src/*.class

run:
	java  -cp src WordApp $(TOTALWORDS) $(NOWORDS) $(DICT)

docs:
	mkdir docs && javadoc --source-path src/*.java -d docs
