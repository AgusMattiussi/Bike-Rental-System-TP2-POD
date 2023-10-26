package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.model.BikeTrip;

import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.proxy.ClientMapProxy;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static ar.edu.itba.pod.tp2.client.utils.ClientUtils.*;
import static ar.edu.itba.pod.tp2.client.AverageDistance.*;


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

//        Mapa de tipo pk -> station
        Map<Integer, Station> stationMap = getStations(inPath);

//        Mapa de tipo emplacement_pk_start -> [viajes que salen de ahi]
        Map<Integer, BikeTrip> bikeTripMap = getBikeTrip(inPath, stationMap);

        IMap<Integer, BikeTrip> bikeIMap = hazelcastInstance.getMap("bike-map");
        IMap<Integer, Station> stationIMap = hazelcastInstance.getMap("station-map");
        bikeIMap.clear();
        stationIMap.clear();

        try{
            bikeIMap.putAll(bikeTripMap);
            stationIMap.putAll(stationMap);
        }catch (Exception e){
            logger.error("Error en la lectura del archivo");
//            TODO: limpiar y borrar
        }

        switch (query) {
            case "query1" -> {
                logger.info("Query 1");
            }
            case "query2" -> {
                String n = argMap.get(N_VAL);
                validateNullArgument(n, "N (result limit) not specified");
                logger.info("Query 2");

                List<Station> stations = new ArrayList<>();
                stations.addAll(stationMap.values());

                averageClientSolver(hazelcastInstance,Integer.parseInt(n), bikeIMap, stations, outPath);
            }
            case "query3" -> {
                logger.info("Query 3");
                Query3 query3Instance = new Query3("query3", hazelcastInstance, stationIMap, bikeIMap);
                query3Instance.run();
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

    private static Map<Integer, Station> getStations(String inPath){
        List<String[]> data = readData(inPath + "stations.csv");
        Map<Integer, Station> stationMap = new HashMap<>();
        for (String[] dArr: data) {
//            pk;name;latitude;longitude
            stationMap.put(Integer.parseInt(dArr[0]), new Station(
                    Integer.parseInt(dArr[0]), dArr[1], Double.parseDouble(dArr[2]), Double.parseDouble(dArr[3])));
        }
        return  stationMap;
    }

    private static Map<Integer, BikeTrip> getBikeTrip(String inPath, Map<Integer,Station> stationMap){
        List<String[]> data = readData(inPath + "bikes.csv");
        Map<Integer, BikeTrip> bikeTripMap = new HashMap<>();
        for (String[] dArr: data) {
//            start_date;emplacement_pk_start;end_date;emplacement_pk_end;is_member
            bikeTripMap.put(Integer.parseInt(dArr[1]), new BikeTrip(LocalDateTime.parse(dArr[0]), LocalDateTime.parse(dArr[2]), Integer.parseInt(dArr[1]), Integer.parseInt(dArr[3]), dArr[4].equals("1")));
        }
        return  bikeTripMap;
    }

    private static List<String[]> readData(String inPath){
            FileReader filereader = null;
            try {
                filereader = new FileReader(inPath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("File not found");
            }

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();
            try(CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build()) {
                return csvReader.readAll();
            } catch (IOException e) {
                throw new RuntimeException("Error reading file in path");
            }

    }

}
