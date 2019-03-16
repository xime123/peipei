TOP    = ../../../../../../..
DOC    = $(TOP)/docs
PKG    = com.tshang.peipei.protocol.asn.gogirl

Files.java = $(wildcard *.java)

all: .$(PKG)_timestamp

.$(PKG)_timestamp: $(Files.java)
ifneq ($(strip $(Files.java)),)
	javac -classpath ".;$(TOP)/classes;$(TOP)/src;.;C:/Program Files/Java/jdk1.8.0_45/lib;C:/Program Files/Java/jdk1.8.0_45/lib/tools.jar" -d $(TOP)/classes $?
	touch .$(PKG)_timestamp
endif

docs: $(DOC)/$(PKG)/AllNames.html

$(DOC)/$(PKG)/AllNames.html: $(Files.java)
ifneq ($(strip $(Files.java)),)
	mkdir -p $(DOC)/$(PKG)
	javadoc -author -version -classpath ".;$(TOP)/classes;$(TOP)/src;.;C:/Program Files/Java/jdk1.8.0_45/lib;C:/Program Files/Java/jdk1.8.0_45/lib/tools.jar" -d $(DOC)/$(PKG) $(PKG)
endif

clean:
	rm -rf $(TOP)/classes/$(PKG) $(DOC)/$(PKG) $(Files.java)
