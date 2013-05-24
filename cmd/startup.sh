startTime=`date +%Y%m%d%H%M%S`
JAVA_OPTS="$JAVA_OPTS  -Xms512m -Xmx2048m"
#JAVA_OPTS="$JAVA_OPTS -Xmn512m -Xms2048m -Xmx2048m -XX:PermSize=64m -XX:MaxPermSize=128m -XX:NewSize=512M -XX:MaxNewSize=512M -XX:+UseAdaptiveSizePolicy"
#JAVA_OPTS="$JAVA_OPTS -XX:ParallelGCThreads=16 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:MaxTenuringThreshold=31 -noclassgc"
#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9787"
#JAVA_OPTS="$JAVA_OPTS -verbose:gc"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
#JAVA_OPTS="$JAVA_OPTS -Xloggc:gc_im_${startTime}.log"
#export ECS_UDP_CFG="(WAIT_TIME(3000)SLIDE_WINDOW_QUEUE_SIZE(10))"
#echo $ECS_UDP_CFG
#备份nohup.out
cp nohup.out nohup${startTime}.out
#清空nohup.out
cp /dev/null nohup.out
nohup java $JAVA_OPTS -cp ./conf:./lib/commons-logging.jar:./lib/junit.jar:./lib/lucene-analyzers-3.6.1.jar:./lib/lucene-core-3.6.1.jar:./lib/lucene-highlighter-3.6.1.jar:./lib/mina-core-2.0.4.jar:./lib/IKAnalyzer3.2.8.jar:./lib/slf4j-api-1.6.1.jar:./lib/jsoup-1.6.3.jar:./lib/log4j-1.2.16.jar:./lib/slf4j-log4j12-1.6.1.jar:./lib/docService.jar: com.huawei.imax.server.DocServer  &
