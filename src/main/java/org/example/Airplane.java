package org.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Airplane {
    private String model;
    private String id_zbor;
    private String locatie_plecare;
    private String locatie_destinatie;
    private LocalTime timp_dorit;
    private LocalTime timp_concret;
    boolean urgenta;

    public static Boolean isUrgent(Airplane airplane) {
        return airplane.urgenta;
    }

    enum Status {
        WAITING_FOR_TAKEOFF,
        DEPARTED,
        WAITING_FOR_LANDING,
        LANDED}

    Status status;

    public LocalTime getTimp_concret() {
        return timp_concret;
    }

    public void setTimp_concret(LocalTime timp_concret) {
        this.timp_concret = timp_concret;
    }

    public LocalTime getTimp_dorit() {
        return timp_dorit;
    }

    public String getLocatie_destinatie() {
        return locatie_destinatie;
    }

    public String getLocatie_plecare() {
        return locatie_plecare;
    }

    public String getId_zbor() {
        return id_zbor;
    }

    public String getModel() {
        return model;
    }

    public Status getStatus() {
        return status;
    }

    public Airplane(String model, String id_zbor, String locatie_plecare, String locatie_destinatie, LocalTime timp_dorit, boolean urgenta) {
        this.model = model;
        this.id_zbor = id_zbor;
        this.locatie_plecare = locatie_plecare;
        this.locatie_destinatie = locatie_destinatie;
        this.timp_dorit = timp_dorit;
        this.urgenta = urgenta;
        this.status = Status.WAITING_FOR_TAKEOFF;
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return model + " - " +
                id_zbor + " - " +
                locatie_plecare + " - " +
                locatie_destinatie + " - " +
                status + " - " +
                timp_dorit.format(timeFormatter) +
                (timp_concret != null ? " - " + timp_concret.format(timeFormatter) : "");
    }
}
