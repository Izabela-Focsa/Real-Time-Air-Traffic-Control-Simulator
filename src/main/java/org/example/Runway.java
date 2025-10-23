package org.example;

import java.time.LocalTime;
import java.util.*;

public class Runway<T extends Airplane> {
    private final String id;
    private final boolean decoleazaSAUaterizeaza; //e true daca decoleaza si false daca aterizeaza
    private final boolean wideSAUnarrow; //e true daca e wide si false daca e narrow
    private final PriorityQueue<T> avioane;
    private LocalTime disponibil = null; //verific in ce interval de timp e disponibila pista
    private ArrayList<T> avioanePermise = new ArrayList<>();

    public Runway(String id, boolean decoleazaSAUaterizeaza, boolean wideSAUnarrow) {
        this.id = id;
        this.decoleazaSAUaterizeaza = decoleazaSAUaterizeaza;
        this.wideSAUnarrow = wideSAUnarrow;
        if (decoleazaSAUaterizeaza) {
            this.avioane = new PriorityQueue<>(
                    Comparator.comparing(Airplane::getTimp_dorit)
            );
        } else {
            this.avioane = new PriorityQueue<>(
                    Comparator.comparing(Airplane::isUrgent).reversed().thenComparing(Airplane::getTimp_dorit)
            );
        }
    }

    public Collection<T> getAvioane() {
        return avioane;
    }

    public Airplane[] getAvioanePermise() {
        return avioanePermise.toArray(new Airplane[0]);
    }

    //clasa pentru eventualele exceptii aruncate
    public static class IncorrectRunwayException extends Exception {
        public IncorrectRunwayException(String message) {
            super(message);
        }
    }

    //exceptia in caz ca pista e indisponibila
    public static class UnavailableRunwayException extends Exception {
        public UnavailableRunwayException(String message) {
            super(message);
        }
    }

    //verific daca e disponibila clasa
    public void checkAvailability(LocalTime currentTime) throws UnavailableRunwayException {
        if (disponibil != null && currentTime.isBefore(disponibil)) {
            throw new UnavailableRunwayException("The chosen runway for maneuver is currently occupied");
        }
    }

    //ma ocup de permisiunea pentru manevra
    public void permissionForManeuver(LocalTime currentTime) throws UnavailableRunwayException {
        if (isOccupied(currentTime)) {
            throw new UnavailableRunwayException("The chosen runway for maneuver is currently occupied");
        }

        //daca pista e libera atunci scot avionul cu cea mai mare prioritate
        if (!avioane.isEmpty()) {
            T airplane = avioane.poll();

            //updatez statusul avionului in functie de tipul pistei
            if (decoleazaSAUaterizeaza) {  //daca pista e pentru decolare
                airplane.status = Airplane.Status.DEPARTED;
            } else {  //daca pista e pentru aterizare
                airplane.status = Airplane.Status.LANDED;
            }

            airplane.setTimp_concret(currentTime);
            avioanePermise.add(airplane);

            //blochez pista pentru 5 minute daca decoleaza si 10 minute daca aterizeaza
            disponibil = decoleazaSAUaterizeaza ? currentTime.plusMinutes(6) : currentTime.plusMinutes(11);
        }
    }

    public boolean isOccupied(LocalTime currentTime) {
        return disponibil != null && currentTime.isBefore(disponibil);
    }

    //adaug avionul listei
    public void addAirplane(T airplane) throws IncorrectRunwayException, UnavailableRunwayException {
        if (isOccupied(LocalTime.now())) {
            throw new UnavailableRunwayException("org.example.Runway " + id + " is currently occupied.");
        }

        if (decoleazaSAUaterizeaza && airplane.getStatus() != Airplane.Status.WAITING_FOR_TAKEOFF) {
            throw new IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
        }
        if (!decoleazaSAUaterizeaza && airplane.getStatus() != Airplane.Status.WAITING_FOR_LANDING) {
            throw new IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
        }
        if (wideSAUnarrow && airplane instanceof NarrowBodyAirplane) {
            throw new IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
        }
        if (!wideSAUnarrow && airplane instanceof WideBodyAirplane) {
            throw new IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
        }

        avioane.add(airplane);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        for (T avion : avioane) {
            sb.append("\n").append(avion.toString());
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public boolean getAterizeazaSAUdecoleaza() {
        return decoleazaSAUaterizeaza;
    }

    public boolean getWideSAUnarrow() {
        return wideSAUnarrow;
    }

}
