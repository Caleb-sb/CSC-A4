# Cloud Assignment Makefile
# Caleb Bredekamp
# 7 September 2019

JAVAC=/usr/bin/javac

.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= Score.class WordDictionary.class WordRecord.class WordPanel.class WordApp.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm bin/*.class

run:
	java -cp bin WordApp "10" "4" "example_dict.txt"
