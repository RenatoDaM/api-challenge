package com.api.challenge.apichallenge.util.csv;

import com.opencsv.bean.CsvToBeanFilter;

public class ClienteCSVIsFirstLine implements CsvToBeanFilter {
    private boolean isFirstLine = true;

    @Override
    public boolean allowLine(String[] line) {
        if (isFirstLine || line[0].equals("Id")) {
            isFirstLine = false;
            return false;
        }
        return true;
    }
}
