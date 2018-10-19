package com.example.ecosistemas.carrera;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClienteUnicast extends Thread {

    DatagramSocket socket;
    int PUERTO;
    InetAddress address;
    GetData datos;
    MainActivity activity;

    public ClienteUnicast(MainActivity activity) {
        this.activity = activity;
        this.datos = activity;

    }

    @Override
    public void run() {
        comnunicar();

        while (true) {
            recibir();
        }
    }

    public void comnunicar() {
        try {
            PUERTO = 5001;
            address = InetAddress.getByName("10.0.2.2");

            socket = new DatagramSocket(PUERTO);


           // socket.connect(address, PUERTO);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void enviar(final String mensaje) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket datagramPacket = new DatagramPacket(mensaje.getBytes(), mensaje.length(), address, PUERTO);

                try {
                    socket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void recibir() {
        byte[] informacion = new byte[100];
        DatagramPacket datagramPacket = new DatagramPacket(informacion, informacion.length);

        try {
            System.out.println("Esperando datos...........................................................");
            socket.receive(datagramPacket);
            System.out.println("Dato recivido...........................................................");
            String mensaje = new String(datagramPacket.getData()).trim();

            datos.getRecibido(mensaje);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface GetData {
        public void getRecibido(String mensaje);
    }
}
