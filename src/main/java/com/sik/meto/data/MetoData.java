package com.sik.meto.data;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MetoData {

    public static void main(String[] args) throws IOException {
        MetoDataHandler manager = new MetoDataHandler();
        MetoDataUtilities utilities = new MetoDataUtilities();
        MetoExcelWriter excelWriter = new MetoExcelWriter();

        Set<MonthlyWeatherData> weatherData = manager.getMonthlyData();

        //Set<MonthlyWeatherData> locationSpecificData = utilities.filterByStation(weatherData, "Paisley");

        Map<String,Set<MonthlyWeatherData>> dataByLocation = weatherData.stream()
                .sorted()
                .collect(Collectors.groupingBy(MonthlyWeatherData::getStationName,Collectors.toSet()));

        LinkedHashMap<String, Set<MonthlyWeatherData>> sortedMap = new LinkedHashMap<>();

        dataByLocation.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        excelWriter.writeHistoricWorkbook(sortedMap);

        excelWriter.writeAveragesWorkbook(sortedMap);

        excelWriter.writeExtremesWorkbook(utilities.buildExtremesMap(weatherData));

    }


}
