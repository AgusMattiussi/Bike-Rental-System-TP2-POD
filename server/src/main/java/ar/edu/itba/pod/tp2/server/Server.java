package ar.edu.itba.pod.tp2.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Collections;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);
    private static final String HAZELCAST_GROUP_NAME = "g6";
    private static final String HAZELCAST_GROUP_PASSWORD = "g6-pass";

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info("hz-config Server Starting ...");

        // Config
        Config config = new Config();

        // Group Config
        GroupConfig groupConfig = new GroupConfig()
                .setName(HAZELCAST_GROUP_NAME)
                .setPassword(HAZELCAST_GROUP_PASSWORD);
        config.setGroupConfig(groupConfig);

        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);


        InterfacesConfig interfacesConfig = new InterfacesConfig()
//                .setInterfaces(Collections.singletonList("127.0.0.1"))
                .setInterfaces(Collections.singletonList("10.9.66.*"))
                .setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.getNetworkConfig()
                .getJoin()
                .getTcpIpConfig()
                .setConnectionTimeoutSeconds(Integer.MAX_VALUE);

        config.setNetworkConfig(networkConfig);

        // Management Center Config
        /*
        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig()
                .setUrl("http://localhost:8080/mancenter/")
                .setEnabled(true);

        config.setManagementCenterConfig(managementCenterConfig);
        */

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
