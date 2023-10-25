package ar.edu.itba.pod.tp2.client;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import io.grpc.ManagedChannel;
import model.BikeTrip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static ar.edu.itba.pod.tp2.client.utils.ClientUtils.*;

public class AverageDistance {
    private static Logger logger = LoggerFactory.getLogger(AverageDistance.class);
    public static void main(String[] args) {
        // -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [params]
        // Example = ./query2 -Daddresses='10.6.0.1:5701' -DinPath=. -DoutPath=. -Dn=4
        logger.info("AverageDistance Client Starting...");

        Map<String, String> argMap = parseArguments(args);

        final String serverAddress = getArgumentValue(argMap, SERVER_ADDRESS);
        final String inPath = getArgumentValue(argMap, INPUT_PATH);
        final String outPath = getArgumentValue(argMap, OUT_PATH);
        final String N = getArgumentValue(argMap, N_VAL);

        validateNullArgument(serverAddress, "Address not specified");
        final List<String> addresses = Arrays.asList(serverAddress.split(";"));

        validateNullArgument(inPath, "Input Path not specified");
        validateNullArgument(outPath, "Output Path not specified");
        validateNullArgument(N, "Result limit not specified");

        try {
            logger.info("Hazelcast client Starting...");
            HazelcastInstance hazelcastInstance = getHazelClientInstance(addresses);
            logger.info("Hazelcast client started");


        }catch (Exception e) {
            logger.error(e.toString());
        }

    }

}
