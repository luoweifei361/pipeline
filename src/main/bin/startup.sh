#!/bin/bash 

current_path=`pwd`
case "`uname`" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname $0))
		;;
	*)
		bin_abs_path=`cd $(dirname $0); pwd`
		;;
esac
base=${bin_abs_path}/..
client_mode="Simple"
logback_configurationFile=$base/conf/logback.xml
export LANG=en_US.UTF-8
export BASE=$base

if [ -f $base/bin/run.pid ] ; then
	echo "found run.pid , Please run stop.sh first ,then startup.sh" 2>&2
    exit 1
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

if [ -z "$JAVA" ]; then
  	echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=1.5) in your PATH." 2>&2
    exit 1
fi

str=`file $JAVA_HOME/bin/java | grep 64-bit`
if [ -n "$str" ]; then
	JAVA_OPTS="-server -Xms2048m -Xmx3072m -Xmn1024m -XX:SurvivorRatio=2 -XX:PermSize=96m -XX:MaxPermSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError"
else
	JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxPermSize=128m "
fi

JAVA_OPTS=" $JAVA_OPTS -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
for i in $base/lib/*;
    do CLASSPATH=$i:"$CLASSPATH";
done
CLASSPATH="$base/conf:$CLASSPATH";

echo "cd to $bin_abs_path for workaround relative path"
cd $bin_abs_path

echo LOG CONFIGURATION : $logback_configurationFile
echo client mode : $client_mode
echo CLASSPATH :$CLASSPATH
$JAVA $JAVA_OPTS $JAVA_DEBUG_OPT -classpath .:$CLASSPATH com.klwk.PipleLineMain 1>>$base/bin/nohup.out 2>&1 &

echo $! > $base/bin/run.pid
echo "cd to $current_path for continue"