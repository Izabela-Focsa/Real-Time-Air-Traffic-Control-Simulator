package org.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NarrowBodyAirplane extends Airplane {
    public NarrowBodyAirplane(String model, String id_zbor, String locatie_plecare, String locatie_destinatie, LocalTime timp_dorit, boolean urgenta) {
        super(model, id_zbor, locatie_plecare, locatie_destinatie, timp_dorit, urgenta);
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "Narrow Body - " +
                getModel() + " - " +
                getId_zbor() + " - " +
                getLocatie_plecare() + " - " +
                getLocatie_destinatie() + " - " +
                getStatus() + " - " +
                getTimp_dorit().format(timeFormatter) +
                (getTimp_concret() != null ? " - " + getTimp_concret().format(timeFormatter) : "");
    }
}
