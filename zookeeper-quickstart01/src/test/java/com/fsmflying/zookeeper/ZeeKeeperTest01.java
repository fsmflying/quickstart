package com.fsmflying.zookeeper;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeeKeeperTest01 {

	Logger logger = LoggerFactory.getLogger(ZeeKeeperTest01.class);
	ZooKeeper zk = null;
	private final CountDownLatch startSignal = new CountDownLatch(1);
	private final CountDownLatch doneSignal = new CountDownLatch(10);
	// private final String connStr =
	// "192.168.1.104:2181,192.168.1.107:2181,192.168.1.109:2181";
	private final String connStr = "192.168.1.104:2181";
	Watcher watcher = null;

	@Before
	public void before() throws IOException {

		// startSignal = new CountDownLatch(1);
		// doneSignal = new CountDownLatch(10);
		watcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub

			}

		};

		CountDownLatch countDownLatch = null;

		zk = new ZooKeeper(connStr, 2000, watcher);
	}

	@After
	public void after() throws InterruptedException {
		zk.close();
	}

	@Test
	public void test_00() {

	}

	@Test
	public void test_01_Startup() throws KeeperException, InterruptedException {
		logger.info("====test_01_Startup===========================================");
		List<String> list = zk.getChildren("/test", null);

		if (list != null) {
			for (String e : list) {
				logger.info("{" + e + "}");
			}
		}
	}

	@Test
	public void test_02_State() throws KeeperException, InterruptedException {
		logger.info("====test_02_Status===========================================");
		// States states = zk.getState();
		logger.info("{State:\"" + zk.getState() + "\"}");
		logger.info("{SessionId:\"" + zk.getSessionId() + "\"}");
	}

	@Test
	public void test_03_createBaseNode() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String result = null;
		if (zk.exists("/eclipse", false) == null)
			result = zk.create("/eclipse", ("[" + timeStr + "]test for eclipse ,create by persistent mode").getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
//		 result = zk.create("/eclipse" + timeStr,
//				("[" + timeStr + "]:fangming,wangxiaojuan,yanxia").getBytes(), Ids.OPEN_ACL_UNSAFE,
//				CreateMode.PERSISTENT);

		logger.info("{result:\"" + result + "\"}");
	}

	@Test
	public void test_03_createPersistent() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		if (zk.exists("/eclipse/persistent", false) == null)
			zk.create("/eclipse/persistent", "test for create by persistent mode".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String result = zk.create("/eclipse/persistent/" + timeStr,
				("[" + timeStr + "]:fangming,wangxiaojuan,yanxia").getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);

		logger.info("{result:\"" + result + "\"}");
	}

	@Test
	public void test_03_createPersistentSequential() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		if (zk.exists("/eclipse/persistentSequential", false) == null)
			zk.create("/eclipse/persistentSequential", "test for create by persistentSequential mode".getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		for (int i = 0; i < 10; i++) {
			String result = zk.create("/eclipse/persistentSequential/x",
					("[" + timeStr + "][" + i + "]:fangming,wangxiaojuan,yanxia").getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT_SEQUENTIAL);
	
			logger.info("{result:\"" + result + "\"}");
		}
	}

	@Test
	public void test_03_createEphemeral() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

		if (zk.exists("/eclipse/ephemeral", false) == null)
			zk.create("/eclipse/ephemeral", "test for create by ephemeral mode".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		String result = zk.create("/eclipse/ephemeral",
				("[" + timeStr + "]:fangming,wangxiaojuan,yanxia").getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);

		logger.info("{result:\"" + result + "\"}");
	}

	@Test
	public void test_03_createEphemeralSequential() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		if (zk.exists("/eclipse/ephemeralSequential", false) == null)
			zk.create("/eclipse/ephemeralSequential", "test for create by ephemeralSequential mode".getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		for (int i = 0; i < 10; i++) {
			String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			String result = zk.create("/eclipse/ephemeralSequential/x",
					("[" + timeStr + "][" + i + "]:fangming,wangxiaojuan,yanxia").getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			logger.info("{result:\"" + result + "\"}");
			Thread.currentThread().sleep(2500);
			
		}

	}

	@Test
	public void test_04_getData() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		byte[] bytes = zk.getData("/eclipse", watcher, null);

		logger.info("{getData(\"/eclipse\"):\"" + new String(bytes) + "\"}");
	}

	@Test
	public void test_04_getChildren() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		List<String> list = zk.getChildren("/eclipse/persistentSequential", watcher, null);
		for(String s :list)
		{
			logger.info("{getChildren(\"/eclipse/persistentSequential\"):\"" + s + "\"}");
		}

//		logger.info("{getChildren(\"/eclipse/persistentSequential\"):\"" + new String(bytes) + "\"}");
	}


	@Test
	public void test_05_exists() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		boolean exists = (zk.exists("/eclipse/persistent", watcher) != null);

		logger.info("{exists(\"/eclipse\",watcher):\"" + exists + "\"}");
	}

	class MasterWatcher implements Watcher {
		private final String masterInfo;

		public MasterWatcher(String masterInfo) {
			this.masterInfo = masterInfo;
		}

		@Override
		public void process(WatchedEvent event) {
			// TODO Auto-generated method stub
			if (event.getPath() == "/eclipse/ephemeral/master" && event.getType() == EventType.NodeDeleted) {
				String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
						.format(Calendar.getInstance().getTime());

				for (int i = 0; i < 20; i++) {
					String result;
					try {
						result = zk.create("/eclipse/ephemeral/master", (masterInfo).getBytes(),
								Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
						logger.info("{result:\"" + result + "\"}");
					} catch (KeeperException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

	}

	@Test
	public void test_06_masterWatcher01() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		
		if (zk.exists("/eclipse/ephemeral", false) == null)
			zk.create("/eclipse/ephemeral", "test for create by ephemeral mode".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		for (int i = 0; i < 10; i++) {
			String result = zk.create("/eclipse/ephemeral/master", ("[" + timeStr + "]:192.168.1.158").getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.info("{result:\"" + result + "\"}");
		}
	}

	@Test
	public void test_06_masterWatcher02() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

		if (zk.exists("/eclipse/ephemeral", false) == null)
			zk.create("/eclipse/ephemeral", "test for create by ephemeral mode".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		for (int i = 0; i < 10; i++) {
			String result = zk.create("/eclipse/ephemeral/master", ("[" + timeStr + "]:192.168.1.158").getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.info("{result:\"" + result + "\"}");
		}
	}
	
	public void rmr(String path) throws KeeperException, InterruptedException
	{
		List<String> children = zk.getChildren(path, null);
		if(children.size()>0)
		{
			for(String s:children)
				rmr(s);
		}
		zk.delete(path, 0);
		logger.info("删除["+path+"].");
	}
	
	@Test
	public void test_07_deleteNode() throws KeeperException, InterruptedException {
		logger.info("====test_03_create===========================================");
		String timeStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String path = "/eclipse";
		rmr(path);
	}
}
