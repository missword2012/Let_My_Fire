# BitCoin #

总共2100W

比特币的构成

	比特币协议   去中心化的点对点网络
	区块链		公共的交易账本
	分布式挖矿	去中心化的数学和确定性的货币发行
	交易脚本		去中心化的交易验证系统


	双重支付:一个货币同时使用2次

tmp:110


比特币公钥到地址的转换
							  (双哈希)
 	公钥 -> SHA256 -> RIPEMD160 -> 公钥哈希 -> Base58check编码 -> 比特币地址
