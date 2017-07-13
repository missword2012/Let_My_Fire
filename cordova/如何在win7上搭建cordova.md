#Cordova 搭建 android环境

需要下载安装的工具

1.node.js  [下载地址](https://nodejs.org/en/download/)

2.git安装 [下载地址](https://git-scm.com/downloads)

3.gradle下载 [下载地址](http://services.gradle.org/distributions/)

4.android sdk [下载地址](http://tools.android-studio.org/index.php/sdk)

##如何安装

	1.node.js 下载安装后默认安装npm 和配置好环境变量了。没啥内容
	
	2.git 这个不多说直接安装就是了
	
	3.gradle 下载下来的压缩包，放到C:\Users\***\.gradle\wrapper\dists 里，你对应版本的目录下

	4.android sdk 下载好后 配置一下环境变量 ANDROID_HOME



##安装 cordova

	windows 首先以管理员身份运行cmd
	执行
	npm install -g cordova

	注:如果比较慢请设置国内镜像
	例如:
	npm config set registry https://registry.npm.taobao.org

	安装完毕可以通过cordova -v 检测是否安装成功

##创建项目

	创建项目

	cordova create hello com.example.hello HelloWorld

	进入项目
	
	cd hello

	添加平台
	
	cordova platform add android --save
	
	查看平台

	cordova platform ls

	编译
	
	cordova build android

	注:编译可能需要对应的android build 版本
	如果没有，可以通过sdk manager 去下载，在执行编译
	在cmd里可能因为没有listener权限无法自己下载

	运行

	cordova run android

	注:run之前先用adb devices 检测下连接状态 没配置adb就配置一下吧
	
	

	

