#Solidity

solidity是一门类js的语言

通过在线编译器 remix 生成相关代码  [Remix在线编辑器](https://ethereum.github.io/browser-solidity/ "Remix")


##1.Solidity智能合约的文件结构

	pragma solidity ^0.4.0  //表示版本高于0.4.0才能编译

	import "fileName"  //全局引入

	import * as symbolName from "filename" //自定义命名空间引入

	import {symbol1 as alias, symbol2} from "fileName" //分别定义引入



##2.Solidity智能合约的基本构成

	每个合约中可包含 

	状态变量(State Variables)
	
	函数(Functions)

	函数修饰符(Function Modifiers)

	事件(Events)

	结构类型(Structs Types)

	枚举类型(Enum Types)

##3.值类型和引用类型

###1.值类型

	布尔(Booleans)
	整型(Integer)
	地址(Address)
	定长字节数组(fixed byte arrays)
	有理数和整型(Rational and Integer Literals，String literals)
	枚举类型(Enums)
	函数(Function Types)

###2.引用类型
	
	不定长字节数组（bytes）
	字符串（string）
	数组（Array）
	结构体（Struts）


##布尔(Booleans)

	bool 可能取值为 true 和 fasle

	逻辑运算符  !  && || == !=

##Integer

	int 默认指的是 int256 范围 -2^255 到 2^255 
	指的是有符号整型，可以是负数

	uint 默认指 uint256 范围 0 到 2^255
	指的是无符号整型,只能为正数

	
	变量支持的步长以8递增，支持从uint8到uint256;

	支持的运算符：

	比较：<=，<，==，!=，>=，>，返回值为bool类型。
	
	位运算符：&，|，（^异或），（~非）。
	
	数学运算：+，-，一元运算+，*，/，（%求余），（**平方）。


###整数字面量

	整数字面量，由包含0-9的数字序列组成，默认被解释成十进制。在Solidity中不支持八进制，前导0会被默认忽略，如0100，会被认为是100。
	
	小数由.组成，在他的左边或右边至少要包含一个数字。如1.，.1，1.3均是有效的小数。

	字面量本身支持任意精度，也就是可以不会运算溢出，或除法截断。但当它被转换成对应的非字面量类型，如整数或小数。或者将他们与非字面量进行运算，则不能保证精度了。
		
##地址(Address)

	地址： 以太坊地址的长度，大小20个字节，160位，所以可以用一个uint160编码。地址是所有合约的基础，所有的合约都会继承地址对象，也可以随时将一个地址串，得到对应的代码进行调用。当然地址代表一个普通帐户时，就没有这么多丰富的功能啦。
	
	支持的运算符
	
	<=，<，==，!=，>=和>


	地址类型的成员

	属性：balance
	函数：send()，call()，delegatecall()，callcode()。


####balance
	
	获取一个地址的余额
	
	pragma solidity ^0.4.0;
	
	contract addressTest{
	    
	    function getBalance(address addr) returns (uint){
	        return addr.balance;
	    }
	
	}


####this

	获取当前合约的余额

	pragma solidity ^0.4.0;

	contract addressTest{
	    
	    function getBalance() returns (uint){
	        return this.balance;
	    }
	
	}

	原因是对于合约来说，地址代表的就是合约本身，合约对象默认继承自地址对象，所以内部有地址的属性。


####send()
	
	向某个地址发送货币(单位是wei)

	pragma solidity ^0.4.0;

	//请注意这个仅是Demo，请不要用到正式环境
	contract PayTest {
	    //得到当前合约的余额
	    function getBalance() returns (uint) {
	        return this.balance;//0
	    }  
    
	    //向当前合约存款
	    function deposit() payable returns(address addr, uint amount, bool success){
	        //msg.sender 全局变量，调用合约的发起方
	        //msg.value 全局变量，调用合约的发起方转发的货币量，以wei为单位。
	        //send() 执行的结果
	        return (msg.sender, msg.value, this.send(msg.value));
	    }
	}
 
	这个合约实现的是充值。this.send(msg.value)意指向合约自身发送msg.value量的以太币。msg.value是合约调用方附带的以太币。

	send()方法执行时有一些风险

	调用递归深度不能超1024。
	如果gas不够，执行会失败。
	所以使用这个方法要检查成功与否。或为保险起见，货币操作时要使用一些最佳实践。
	如果执行失败，将会回撤所有交易，所以务必留意返回结果。


call()，delegatecall()，callcode()都是底层的消息传递调用，最好仅在万不得已才进行使用，因为他们破坏了Solidity的类型安全。

##字节数组(byte arrays)

	运算符

	比较：<=，<，==，!=，>=，>，返回值为bool类型。
	
	位运算符：&，|，^(异或)，~非
	
	支持序号的访问，与大多数语言一样，取值范围[0, n)，其中n表示长度。
	
	成员变量
	
	.length表示这个字节数组的长度（只读）。

###定长字节数组（Fixed-size byte arrays）

	bytes1， ... ，bytes32，允许值以步长1递增。byte默认表示byte1。

##小数

	小数字面量
	
	如果字面量计算的结果不是一个整数，那么将会转换为一个对应的ufixed，或fixed类型。Solidity会选择合适的大小，以能尽量包含小数部分。

	例，在var x = 1 / 4中，x的实际类型是ufixed0x8。而在var x = 1/ 3中，类型会是ufixedox256，因为这个结果表示是无限的，所以他只能是无限接近。

##枚举

	pragma solidity ^0.4.0;

	contract test {
	    enum ActionChoices { GoLeft, GoRight, GoStraight, SitStill }
	    ActionChoices choice;
	    ActionChoices constant defaultChoice = ActionChoices.GoStraight;
	
	    function setGoStraight() {
	        choice = ActionChoices.GoStraight;
	    }

	    function getChoice() returns (ActionChoices) {
	        return choice;
    }

	    function getDefaultChoice() returns (uint) {
	        return uint(defaultChoice);
	    }
}