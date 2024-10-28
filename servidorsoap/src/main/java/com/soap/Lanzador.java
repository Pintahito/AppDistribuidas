package com.soap;

import javax.xml.ws.Endpoint;

public class Lanzador {

    public final static String urlCalculadoraWS = "http://localhost:8888/calculadora";

    public static void main(String[] args) {
        Endpoint.publish(urlCalculadoraWS, new CalculadoraWS());
        System.out.println("Servicio Web en espera en "+urlCalculadoraWS);        
    }
}
