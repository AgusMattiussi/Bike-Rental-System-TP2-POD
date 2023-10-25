package ar.edu.itba.pod.tp2.client.utils;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientUtils {
    private final static Logger logger = LoggerFactory.getLogger(ClientUtils.class);
    public final static String INPUT_PATH = "inPath";
    public final static String SERVER_ADDRESS = "addresses";
    public final static String OUT_PATH = "outPath";
    public final static String N_VAL = "n";
    public final static String START_DATE = "startDate";
    public final static String END_DATE = "endDate";

    public static ManagedChannel buildChannel(String serverAddress){
        return ManagedChannelBuilder.forTarget(serverAddress)
                .usePlaintext()
                .build();
    }

    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> argMap = new HashMap<>();
        for (String arg : args) {
            String[] parts = arg.substring(2).split("=");
            if (parts.length == 2) {
                argMap.put(parts[0], parts[1]);
            }
        }
        return argMap;
    }

    public static String getArgumentValue(Map<String, String> argMap, String key) {
        return argMap.get(key);
    }

    public static void createOutputFile(String outPath, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outPath));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            logger.error("Error creating output file");
        }
    }

    public static void validateNullArgument(String arg, String erroMsg) {
        if (arg == null) {
            logger.error(erroMsg);
            System.exit(1);
        }
    }


    public static HazelcastInstance getHazelClientInstance(List<String> addresses) {
        String name = "group_name";
        String pass = "group_password";

        ClientConfig clientConfig = new ClientConfig();

        GroupConfig groupConfig = new GroupConfig().setName(name).setPassword(pass);
        clientConfig.setGroupConfig(groupConfig);


        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig().setAddresses(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}

