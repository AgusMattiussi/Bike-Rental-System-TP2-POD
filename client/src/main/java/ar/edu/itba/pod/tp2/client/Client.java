package ar.edu.itba.pod.tp2.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static ar.edu.itba.pod.tp2.client.utils.ClientUtils.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Client Starting ...");

        // Parse Arguments
        // ./queryX -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [-Dn=4 |  -DstartDate=01/05/2021 -DendDate=31/05/2021 ]
        final Map<String, String> argMap = parseArguments(args);

        final String query = args[0]; // TODO: Revisar
        final List<String> addresses = getAddressesList(argMap.get(ADDRESSES));
        final String inPath = argMap.get(INPUT_PATH);
        final String outPath = argMap.get(OUT_PATH);

        validateNullArgument(argMap.get(ADDRESSES), "Addresses not specified");
        validateNullArgument(inPath, "Input path not specified");
        validateNullArgument(outPath, "Output path not specified");

        HazelcastInstance hazelcastInstance = getHazelClientInstance(addresses);
        logger.info("Hazelcast client started");

        String mapName = "testMap";
        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);

        testMapFromMember.set(1, "test1");
        IMap<Integer, String> testMap = hazelcastInstance.getMap(mapName);
        System.out.println(testMap.get(1));

        switch (query) {
            case "query1" -> {
                logger.info("Query 1");
            }
            case "query2" -> {
                String n = argMap.get(N_VAL);
                validateNullArgument(n, "N (result limit) not specified");

                logger.info("Query 2");
            }
            case "query3" -> {
                logger.info("Query 3");
            }
            case "query4" -> {
                 String startDate = argMap.get(START_DATE);
                 String endDate = argMap.get(END_DATE);
                 validateNullArgument(startDate, "Start date not specified");
                 validateNullArgument(endDate, "End date not specified");

                logger.info("Query 4");
            }
            default -> logger.error("Invalid query");
        }

        // Shutdown
        HazelcastClient.shutdownAll();
    }
}
