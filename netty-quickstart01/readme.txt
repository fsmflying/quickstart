
d:
cd D:\dev\git\ReposBase\quickstart\netty-quickstart01


java -classpath target\netty-quickstart01.jar com.fsmflying.netty.part01.DiscardServer -Dssl=true -Dport=8007
java -classpath target\netty-quickstart01.jar com.fsmflying.netty.echo.EchoServer

java -classpath target\netty-quickstart01.jar com.fsmflying.netty.example.discard.DiscardClient
java -classpath target\netty-quickstart01.jar com.fsmflying.netty.example.discard.DiscardServer


java -classpath target\netty-quickstart01.jar com.fsmflying.netty.part01.DiscardClient
java -classpath target\netty-quickstart01.jar com.fsmflying.netty.part01.DiscardServer



mvn clean package -Dmaven.skip.test=true

采集服务器:接收数据并写入到文件中
java -classpath target\netty-quickstart01.jar com.fsmflying.netty.part05.CollectServer

数据采集终端：发送数据到采集服务器
java -classpath target\netty-quickstart01.jar com.fsmflying.netty.part01.DiscardClient



