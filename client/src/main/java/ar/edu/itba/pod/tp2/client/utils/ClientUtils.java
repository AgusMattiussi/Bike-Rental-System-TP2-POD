package ar.edu.itba.pod.tp2.client.utils;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientUtils {
    private final static Logger logger = LoggerFactory.getLogger(ClientUtils.class);
    public final static String INPUT_PATH = "inPath";
    public final static String ADDRESSES = "addresses";
    public final static String OUT_PATH = "outPath";
    public final static String N_VAL = "n";
    public final static String START_DATE = "startDate";
    public final static String END_DATE = "endDate";
    private static final String STATIONS_CSV_FILENAME = "stations.csv";
    private static final String RENTALS_CSV_FILENAME = "bikes.csv";

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

    public static List<String> getAddressesList(String addresses) {
        return Arrays.asList(addresses.replaceAll("\\'","" ).split(";"));
    }

    public static HazelcastInstance getHazelClientInstance(List<String> addresses) {
        String name = "group_name";
        String pass = "group_password";

        try {
            logger.info("Hazelcast client Starting...");
            ClientConfig clientConfig = new ClientConfig();

            GroupConfig groupConfig = new GroupConfig().setName(name).setPassword(pass);
            clientConfig.setGroupConfig(groupConfig);


            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig().setAddresses(addresses);
            clientConfig.setNetworkConfig(clientNetworkConfig);

            return HazelcastClient.newHazelcastClient(clientConfig);

        } catch (Exception e) {
            logger.error(e.toString());
        }

        return null;
    }
}

