#ETH上部署执行智能合约


##方法一

1. 首先通过solidity在线编辑器获得对应的web3代码

	Web3 deploy ：
		
			var browser_untitled_sol_demotypesContract = web3.eth.contract([{"constant":false,"inputs":[{"name":"a","type":"uint256"}],"name":"add","outputs":[{"name":"b","type":"uint256"}],"payable":false,"type":"function"}]);
		var browser_untitled_sol_demotypes = browser_untitled_sol_demotypesContract.new(
		   {
		     from: web3.eth.accounts[0], 
		     data: '0x6060604052341561000f57600080fd5b5b60b08061001e6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631003e2d214603d575b600080fd5b3415604757600080fd5b605b60048080359060200190919050506071565b6040518082815260200191505060405180910390f35b6000806008830190508091505b509190505600a165627a7a723058202f6c379353296d56c88d0a941e6deba129edcf0b6658be9cea6170c03022bb950029', 
		     gas: '4700000'
		   }, function (e, contract){
		    console.log(e, contract);
		    if (typeof contract.address !== 'undefined') {
		         console.log('Contract mined! address: ' + contract.address + ' transactionHash: ' + contract.transactionHash);
		    }
		 })


	生成如上代码


2.复制到geth console中运行 (如果涉及到交易需要申请权限)
	这时候输入browser_untitled_sol_demotypes（智能合约的名称）
	可以看到只能合约相关的信息

3.将智能合约写入区块

	miner.start();

4.执行智能合约

	browser_untitled_sol_demotypes.f.call(100)

	这样就完成了智能合约的部署和执行
	
	 






##方法二

	geth --exec 'loadScript("$fileName")' attach

	$fileName 对应智能合约的js名称


	智能合约内容

	var _greeting = "Hello,World!"  //定义的string
	
	var greeterContract = web3.eth.contract($browser-solidity.interface)

	$browser-solidity 这里指solidity的在线编辑器根据对应的
	solidity文件生成的interface
	
	web3.personal.unlockAccount(web3.eth.accounts[1],"123")
	
	这一步指解除账号的绑定，每个账户进行交易操作都需要先进行解绑操作

	var geeeter = greeterContract.new(
		_greeting,
		{
			from:web3.eth.accounts[1], //来自哪个地址
			value:web3.toWei("1.1","ether"), //交易多少以太币
			data:'$data', //solidity编辑器生成的data
			gas:'320000'  //交易的花费
			
		},function(err, myContract){

		//异步调用的结果

		if(!err){
				
			if(!myContract.address){
			console.log(myContract.transactionHash) //打印交易hash
		}else{
			console.log(myContract.address)  //打印我的账户地址
			}
	
	}else{
		console.log(err); //打印错误信息
		}
	)
