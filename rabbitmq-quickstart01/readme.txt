
producer:
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomProducer
java -classpath d:\dev\git\ReposBase\quickstart\common-quickstart\target\common-quickstart-0.0.1-SNAPSHOT.jar;target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.MultiThreadCustomProducer
java -classpath d:\dev\git\ReposBase\quickstart\common-quickstart\target\common-quickstart-0.0.1-SNAPSHOT.jar;target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part02.MultiThreadCustomProducer testExchange02 INFO


consumer:
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomConsumer testQueue06 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomConsumer testQueue07 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomConsumer testQueue08 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomConsumer testQueue09 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomConsumer testQueue10 false
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CustomConsumerForAck


binding:
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue03 testExchange

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue11 testExchange07 fruit.DEBUG.com
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue12 testExchange07 fruit.INFO.com
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue13 testExchange07 fruit.WARN.com
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue14 testExchange07 fruit.ERROR.com
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue14 testExchange07 fruit.FATAL.com
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue143 testExchange07 #

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue06 testExchange07 *.DEBUG.*
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue07 testExchange07 *.INFO.*
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue08 testExchange07 *.WARN.*
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue09 testExchange07 *.ERROR.*
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue09 testExchange07 *.FATAL.*

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue02 testExchange07 *
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue03 testExchange07 *
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue04 testExchange07 *
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue05 testExchange07 *
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.BindQueueToExchange testQueue05 testExchange07 *

create exchange:
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomExchange <exchangeName> <direct|fanout|topic|headers> <durable>

create queue:
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue <queueName> <durable>

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue01 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue02 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue03 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue04 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue05 false

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue06 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue07 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue08 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue09 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue10 false

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue11 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue12 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue13 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue14 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue15 false

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue16 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue17 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue18 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue19 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue20 false

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue21 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue22 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue23 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue24 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue25 false

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue26 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue27 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue28 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue29 true
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part01.CreateCustomQueue testQueue20 false

java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part03.CustomRpcServer
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part03.CustomRpcClient 20
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part03.RPCServer
java -classpath target\rabbitmq-quickstart01-0.0.1-SNAPSHOT.jar com.fsmflying.rabbitmq.part03.RPCClient 20



