
当使用 sudo apt-get update 时显示could not get lock

说明当前apt还在运行

有2种解决方案

#1.查看当前运行的apt进程

	ps -A | grep apt

	sudo kill -9 进程id
	或者
	sudo kill -SIGKILL 进程id

#2.删除锁定文件

当你执行了 apt-get 或者 apt命令的时候
锁定文件会创建于 
		/var/lib/apt/list
		/var/lib/dpkg/
		/var/cache/apt/archives中

首先 

	sudo rm /var/lib/dpkg/lock

重新配置软件包

	sudo dpkg --configure -a

也可以删除/var/lib/apt/lists/

	sudo rm /var/lib/apt/lists/lock
	sudo rm /var/cache/apt/archives/lock
	   
再次更新
	sudo apt-get update
