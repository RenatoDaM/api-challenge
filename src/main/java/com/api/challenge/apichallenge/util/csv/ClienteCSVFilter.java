package com.api.challenge.apichallenge.util.csv;

import com.opencsv.bean.CsvToBeanFilter;

public class ClienteCSVFilter implements CsvToBeanFilter {
    private boolean isFirstLine = true;

    @Override
    public boolean allowLine(String[] line) {
        if (isFirstLine) {
            isFirstLine = false;
            return false;
        }
        return true;
    }
}
