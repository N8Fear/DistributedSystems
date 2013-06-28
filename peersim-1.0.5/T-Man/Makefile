.PHONY: all clean doc

LIB_JARS=`find -L lib/ -name "*.jar" | tr [:space:] :`

all:
	mkdir -p classes
	javac -sourcepath src -classpath $(LIB_JARS) -d classes `find -L src/ -name "*.java"`

doc:
	rm -rf doc/*
	javadoc -docletpath lib/peersim-doclet.jar -doclet peersim.tools.doclets.standard.Standard -classpath src:$(LIB_JARS) -d doc \
		peersim.tman \
		peersim.tman.chord \
		peersim.tman.pastry \
		peersim.tman.ring \
		peersim.tman.sorted \
		peersim.tman.tree 
		

%.cfg : config/%.cfg
	java -cp "lib/*:classes" peersim.rangesim.RangeSimulator $< jvm.options="-Xmx1584M"

clean: 
	rm -fr classes
