# Assignement 4 Makefile
# Caleb Bredekamp
# 27 September 2019

JAVAC=/usr/bin/javac

.SUFFIXES: .java .class

SRCDIR=src

.java.class:
	$(JAVAC) $(SRCDIR)/*.java

CLASSES= \
 Score.java\
 WordDictionary.java\
 WordRecord.java\
 Controller.java\
 WordPanel.java\
 WordApp.java

classes: $(CLASSES:%.java=$(SRCDIR)/%.class)

default: classes

clean:
	rm src/*.class

run:
	java  -cp src WordApp "10" "4" "src/example_dict.txt"
