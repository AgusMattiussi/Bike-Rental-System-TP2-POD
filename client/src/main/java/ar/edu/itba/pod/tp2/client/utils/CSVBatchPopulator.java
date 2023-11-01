package ar.edu.itba.pod.tp2.client.utils;

import com.hazelcast.core.IMap;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public abstract class CSVBatchPopulator<KeyT, ValueT> implements Runnable{

    private static final int BATCH_SIZE = 1000;

    private final CSVReader reader;
    private final Map<KeyT, ValueT> currentBatchIMap;
    private final IMap<KeyT, ValueT> hazelcastIMap;
    private final int LINE_LIMIT = 100_000;

    public CSVBatchPopulator(String path, IMap<KeyT, ValueT> hazelcastIMap) {
        this.reader = initializeCSVReader(path);
        this.currentBatchIMap = new HashMap<>(BATCH_SIZE);
        this.hazelcastIMap = hazelcastIMap;
        System.out.println("Hazelcast IMap name: " + hazelcastIMap.getName());
    }

    private CSVReader initializeCSVReader(String path){
        FileReader filereader;
        try {
            filereader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found in " + path);
        }

        CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

        return new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
    }

    // Devuelve un par clave-valor para guardar en el currentBatchIMap a partir de una linea del CSV
    protected abstract IMap.Entry<KeyT, ValueT> consumeNextLine(String[] line);

    private void flushBatchToHazelcast() {
        hazelcastIMap.putAll(currentBatchIMap);
    }

    @Override
    public void run() {
        String[] line;
        //TODO: Borrar i
        int i = 0;
        try {
            while ((line = reader.readNext()) != null && i < LINE_LIMIT) {
                if (currentBatchIMap.size() == BATCH_SIZE) {
                    flushBatchToHazelcast();
                    currentBatchIMap.clear();
                }

                IMap.Entry<KeyT, ValueT> nextEntry = consumeNextLine(line);
                currentBatchIMap.put(nextEntry.getKey(), nextEntry.getValue());

                i++;
                if(i % 1_000_000 == 0){
                    System.out.printf("Read %d lines%n", i);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        flushBatchToHazelcast();
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
