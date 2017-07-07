#geth的基本命令操作

查看当前区块链中所有账号信息

	eth.accounts
	
	这里显示的值实际上就是账户的地址值也就是公钥

创建一个新的账户

	personal.newAccount("$password")
	
	$password 就是你想要设值的密码

获取账户余额

	eth.getBalance($account)

	$account 是账号地址
	例:
	eth.getBalance(eth.accounts[0])

	优化显示(以ether单位显示)
	web3.fromWei(4000000) 将wei转换为ether
	web3.fromWei(10000000000000000,"ether")
	web3.fromWei(eth.getBalance(eth.accounts[0]))

	web3.toWei(0.001) 将ether转换为wei
	web3.toWei(eth.getBalance(eth.accounts[0]))

进行转账

	1.首先定义2个变量 分别是账户acc0和账户acc1

		1. acc0 = eth.accounts[0]
		2. acc1 = eth.accounts[1]
	
	2.定义交易额

		amount = web3.toWei(10)

	3.解锁账户

		personal.unlockAccount(acc0)
	
	4.交易

		eth.sendTransaction({from: acc0, to: acc1, value: amount})
	
	5.查询
		
		web3.fromWei(eth.getBalance(acc1))
		发现余额没改变，说明还没有将交易写入区块中
		进行挖矿，将交易写入区块

	6.挖矿

		miner.start(5);admin.sleepBlocks(1);miner.stop();

		挖完之后再次查询，可以看到交易已经成功
		
	


	
	