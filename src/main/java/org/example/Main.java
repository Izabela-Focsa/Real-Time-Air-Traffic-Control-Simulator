package org.example;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        String testName = args[0];
        String basePath = Paths.get("src", "main", "resources", testName).toString();
        String inputFileName = basePath + "/input.in";
        String exceptionsFileName = basePath + "/board_exceptions.out";
        String flightInfoFileName = basePath + "/flight_info.out";

        Collection<Runway<Airplane>> piste = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter exceptionWriter = new BufferedWriter(new FileWriter(exceptionsFileName));
             BufferedWriter flightInfoWriter = new BufferedWriter(new FileWriter(flightInfoFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                Comanda(line, piste, basePath, flightInfoWriter, exceptionWriter);
            }
            flightInfoWriter.flush();
            exceptionWriter.flush();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void Comanda(String command, Collection<Runway<Airplane>> runways,
                                String basePath, BufferedWriter flightInfoWriter, BufferedWriter exceptionWriter)
            throws IOException {
        String[] parts = command.split(" - ");
        if (parts.length < 2) {
            return;
        }
        String timestamp = parts[0];
        String comanda = parts[1];

        LocalTime currentTime = LocalTime.parse(timestamp, TIME_FORMATTER);

        try {
            switch (comanda) {
                case "add_runway_in_use": {
                    String id = parts[2];
                    boolean aterizeazaSAUdecoleaza = parts[3].equals("takeoff");
                    boolean wideSAUnarrow = parts[4].equals("wide body");

                    Runway<Airplane> runway = new Runway<>(id, aterizeazaSAUdecoleaza, wideSAUnarrow);
                    runways.add(runway);
                    break;
                }

                case "allocate_plane": {
                    String tip = parts[2];
                    String model = parts[3];
                    String ID_zbor = parts[4];
                    String plecare = parts[5];
                    String destinatie = parts[6];
                    LocalTime timp_dorit = LocalTime.parse(parts[7], TIME_FORMATTER);
                    String ID_pista = parts[8];
                    boolean urgenta = parts.length > 9 && parts[9].equalsIgnoreCase("urgent");

                    Airplane airplane = tip.equals("wide body")
                            ? new WideBodyAirplane(model, ID_zbor, plecare, destinatie, timp_dorit, urgenta)
                            : new NarrowBodyAirplane(model, ID_zbor, plecare, destinatie, timp_dorit, urgenta);

                    Runway<Airplane> pista = runways.stream()
                            .filter(runway -> runway.getId().equals(ID_pista))
                            .findFirst()
                            .orElseThrow(() -> new Runway.IncorrectRunwayException("org.example.Runway " + ID_pista + " not found."));

                    if (plecare.equals("Bucharest")) {
                        airplane.status = Airplane.Status.WAITING_FOR_TAKEOFF;
                    } else {
                        airplane.status = Airplane.Status.WAITING_FOR_LANDING;
                    }

                    if (airplane.status.equals(Airplane.Status.WAITING_FOR_TAKEOFF) && !pista.getAterizeazaSAUdecoleaza()) {
                        throw new Runway.IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
                    } else if (airplane.status.equals(Airplane.Status.WAITING_FOR_LANDING) && pista.getAterizeazaSAUdecoleaza()) {
                        throw new Runway.IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
                    }

                    if (pista.getWideSAUnarrow() && airplane instanceof NarrowBodyAirplane) {
                        throw new Runway.IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
                    }
                    if (!pista.getWideSAUnarrow() && airplane instanceof WideBodyAirplane) {
                        throw new Runway.IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
                    }

                    pista.addAirplane(airplane);
                    break;
                }

                case "permission_for_maneuver": {
                    String ID_pista = parts[2];
                    Runway<Airplane> pista = runways.stream()
                            .filter(runway -> runway.getId().equals(ID_pista))
                            .findFirst()
                            .orElseThrow(() -> new Runway.IncorrectRunwayException("org.example.Runway " + ID_pista + " not found."));

                    try {
                        pista.permissionForManeuver(currentTime);
                    } catch (Runway.UnavailableRunwayException e) {
                        exceptionWriter.write(currentTime.format(TIME_FORMATTER) + " | " + e.getMessage());
                        exceptionWriter.newLine();
                        exceptionWriter.flush();
                    }
                    break;
                }

                case "runway_info": {
                    String ID_pista = parts[2];
                    Runway<Airplane> pista = runways.stream()
                            .filter(runway -> runway.getId().equals(ID_pista))
                            .findFirst()
                            .orElse(null);

                    if (pista != null) {
                        String formattedTimestamp = currentTime.format(TIME_FORMATTER).replace(":", "-");
                        String outputFileName = basePath + "/runway_info_" + ID_pista + "_" + formattedTimestamp + ".out";

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                            writer.write(pista.getId() + " - " + (pista.isOccupied(currentTime) ? "OCCUPIED" : "FREE"));

                            if (!pista.getAterizeazaSAUdecoleaza()) {
                                pista.getAvioane().stream()
                                        .sorted(Comparator
                                                .comparing(Airplane::isUrgent).reversed()
                                                .thenComparing(Airplane::getTimp_dorit)).forEach(airplane -> {
                                            try {
                                                writer.newLine();
                                                writer.write(airplane.toString());
                                            } catch (IOException e) {
                                                System.err.println("Error writing to file " + outputFileName + ": " + e.getMessage());
                                            }
                                        });
                            } else {
                                pista.getAvioane().stream()
                                        .sorted(Comparator
                                                .comparing(Airplane::getTimp_dorit)).forEach(airplane -> {
                                            try {
                                                writer.newLine();
                                                writer.write(airplane.toString());
                                            } catch (IOException e) {
                                                System.err.println("Error writing to file " + outputFileName + ": " + e.getMessage());
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }

                case "flight_info": {
                    String ID = parts[2];
                    int ok = 0;
                    for (Runway<Airplane> runway : runways) {
                        for (Airplane avion : runway.getAvioane()) {
                            if (avion.getId_zbor().equals(ID)) {
                                flightInfoWriter.write(currentTime.format(TIME_FORMATTER) + " | " + avion.toString());
                                flightInfoWriter.newLine();
                                ok = 1;
                            }
                        }
                        for (Airplane avion : runway.getAvioanePermise()) {
                            if (avion.getId_zbor().equals(ID)) {
                                flightInfoWriter.write(currentTime.format(TIME_FORMATTER) + " | " + avion.toString());
                                flightInfoWriter.newLine();
                                ok = 1;
                            }
                        }
                    }
                    if (ok == 1) {
                        flightInfoWriter.flush();
                    }
                    break;
                }

                case "exit": {
                    return;
                }

                default:
                    break;
            }
        } catch (Runway.IncorrectRunwayException | Runway.UnavailableRunwayException e) {
            exceptionWriter.write(currentTime.format(TIME_FORMATTER) + " | " + e.getMessage());
            exceptionWriter.newLine();
            exceptionWriter.flush();
        }
    }
}