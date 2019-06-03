!/bin/sh
CLASSPATH=${CLASSPATH}:"./classes":`echo ./lib/*.jar | sed "s/ /:/g"`
if [ -z "${JAVA_HOME}" ] ; then
    echo "JAVA_HOME setting ERROR"
    echo "Please set JAVA_HOME"
    exit 1
fi
if [ ! -x "${JAVA_HOME}/bin/java" ] ; then
    echo "JAVA_HOME SETTING ERROR"
    echo "Can not find Java VM"
    exit 1
fi

echo "Using JAVA_HOME   ${JAVA_HOME}"
echo "Using CLASSPATH   ${CLASSPATH}"

nohup "${JAVA_HOME}/bin/java" -server -Xmx1024m -Xms1024m  -Xss512k  -XX:+AggressiveOpts -XX:+UseBiasedLocking -XX:PermSize=64M -XX:MaxPermSize=100M -XX:+DisableExplicitGC -XX:MaxTenuringThreshold=15 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m  -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly   -XX:LoopUnrollLimit=1 -cp .:../lib/* com.wanrong.Application &
if [ $? -eq 0 ]
    then
        /bin/echo "${!}" > "tempid"

fi
