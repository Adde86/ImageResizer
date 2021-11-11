
# Script to stop the application
PID_PATH="./bin/shutdown.pid"

if [ ! -f "$PID_PATH" ]; then
   echo "Process Id FilePath($PID_PATH) Not found"
else
pid=`cat $PID_PATH`
    if [ ! -e /proc/$pid -a /proc/$pid/exe ]; then
        echo "$appName was not running.";
    else
       kill $pid;
       echo "Gracefully stopping application with PID:$pid..."
    fi
fi