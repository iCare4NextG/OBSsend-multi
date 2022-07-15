#!/bin/sh
# -------------------------------------------------------------------------
# OBSsend  Launcher
# -------------------------------------------------------------------------

MAIN_CLASS=kr.irm.obssend.ServiceLauncher

DIRNAME="`dirname "$0"`"

# Setup $COHORTUPDATER_HOME
if [ "x$_HOME" = "x" ]; then
    OBSSEND_HOME=`cd "$DIRNAME"/..; pwd`
fi

# Setup the JVM
#JAVA_HOME
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

# Setup the classpath
CP="$OBSSEND_HOME/etc/OBSsender/"
for s in $OBSSEND_HOME/lib/*.jar
do
	CP="$CP:$s"
done

# Execute the JVM

exec $JAVA $JAVA_OPTS -cp "$CP" $MAIN_CLASS "$@"