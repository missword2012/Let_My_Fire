#ETH 在ubuntu环境下搭建eth的私有链

1.安装geth
	
	sudo add-apt-repository -y ppa:ethereum/ethereum

	sudo apt-get update

	sudo apt-get install ethereum
	
	安装完成之后通过geth version检测

[geth安装参考地址](https://ethereum.github.io/go-ethereum/install/ "安装参考地址")

2.进行私有链的创建
	
	进入工作目录

		1.创世模块的创建
		创建 genesis.json文件内容如下。
	
	{
	  "config": {
	        "chainId": 15,
	        "homesteadBlock": 0,
	        "eip155Block": 0,
	        "eip158Block": 0
	    },
	    "coinbase" : "0x0000000000000000000000000000000000000000",
	    "difficulty" : "0x40000",
	    "extraData" : "",
	    "gasLimit" : "0xffffffff",
	    "nonce" : "0x0000000000000042",
	    "mixhash" : "0x0000000000000000000000000000000000000000000000000000000000000000",
	    "parentHash" : "0x0000000000000000000000000000000000000000000000000000000000000000",
	    "timestamp" : "0x00",
	    "alloc": { }
	}
	
	注:1.config必须要有，不然会报错
	   2.coinbase:默认矿池地址，会被第一个账户地址覆盖
	   3.difficulty:挖矿难度，值越大挖矿时间越久
	   4.nonce:交易次数（主要用于防范多重攻击）	


	2.创世模块初始化
	geth --networkid 310 --datadir geth310 init genesis.json
	
	进入console
	
	geth --networkid 310 --datadir geth310 console

	这样你的私有链已经搭建完成了。

	在console下，首先创建用户
	personal.newAccount("password")

	然后就可以进行挖矿了
