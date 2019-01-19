#!/bin/bash

. /etc/init.d/functions
. /etc/profile

appdirname=application
param=$1
basepath=$(cd `dirname $0`; pwd)
appdir=$basepath/$appdirname
jar=$basepath/$appdirname/$param
newjar=$basepath/$param
newVersion=$basepath/$param
time=$(date "+%Y%m%dT%H%M%S")
backupdir=$basepath/backup
backuptimedir=$backupdir/$time


###############################################################
function msg() { 
        if [ $? -eq 0 ];then
            action "$1" /bin/true
        else
            action "$1" /bin/false
        fi
}

################################################################
function init() {
	if [ -d $appdir ];then
            if [ -f $newjar ];then
		return 0
            else 	
                echo "Missing update file: $newjar"
                return 1
            fi
	else
            mkdir $appdir
            init
	fi
}
###############################################################

function _shutdown() { 
#   pid=$(ps -ef | grep -v grep |  grep -v upgrade.sh | grep $param | cut -d' ' -f6)
    pid=`ps aux | grep -v grep | grep -v upgrade.sh | grep $param | awk '{print $2}'`
    echo "Obtain application pid: $pid!"
    if [ $pid -gt 0 ];then
        echo "kill application pid: $pid!"
	kill $pid
        sleep 1
        _shutdown
    else
        echo "application already shutdown!"
        return 0
    fi
}

###############################################################

function backup() { 
    if [ -d $backupdir ];then 
	if [ -d $backuptimedir ]
 	then
            if [ -f $jar ];then
	        mv $jar $backuptimedir
                return 0
            fi
	else
        if [ -f $jar ];then 
            echo "mkdir $backuptimedir"
            mkdir $backuptimedir    
            echo "move $jar to $backuptimedir"
            mv $jar $backuptimedir
            return $?
        fi
        fi
    else
            mkdir $backupdir
            echo "mkdir $backupdir"
            backup
    fi
}
###############################################################

function copyjar() { 
	echo "start copy new jar file to application"
	if [ -f $newjar ];then 
	    cp $newjar $jar
	    return $?
	else
	    return 1
	fi
}

###############################################################
function restart() { 
        if [ -f $jar ];then
	        nohup java -jar $jar &
	        return $?
        else
	        echo "$jar not exist!"
	        return 1
fi
}

###############################################################

function main() { 
	init
	msg "1) 检查部件！"
	_shutdown
	msg "2) 停止服务！"
	backup
	msg "3) 系统备份！"
	copyjar
	msg "4) 安装文件！"
	restart
	msg "5) 系统启动！"   
}
main



