package PistaDeCarreras;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import processing.core.PApplet;

public class conexionCaballo extends Thread implements Observer {

	MulticastSocket servidor;
	int PUERTO;
	InetAddress ipGrupo;
	int ID = -1;
	boolean identificado;
	Logica log;
	PApplet app;
	float velocidad;
	int color;
	boolean gano;

	public conexionCaballo(Logica logica) {
		this.log = logica;
		this.app = log.getPApplet();
		inicializar();

		int r = (int) app.random(255);
		int g = (int) app.random(255);
		int b = (int) app.random(255);
		velocidad = app.random(1, 8);
		color = app.color(r, g, b);

	}

	public void inicializar() {
		try {

			PUERTO = 5000;
			ipGrupo = InetAddress.getByName("228.5.6.8");

			servidor = new MulticastSocket(PUERTO);

			servidor.joinGroup(ipGrupo);
			System.out.println("Inicializo");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		identeficacion();

		while (true) {
			try {
				recibir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void identeficacion() {

		try {
			servidor.setSoTimeout(600);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		enviar("Identify;" + velocidad + ";" + color + ";"+ ID);

		while (identificado == false) {

			try {
				recibir();
			} catch (IOException e) {

				identificado = true;

				if (ID == -1) {
					ID = 0;

				}

				System.out.println("Hola mi id es:" + ID);
				Caballo b = new Caballo(log, color, velocidad, ID);
				log.addCaballoInicial(b);
				b.setObserver(this);

				try {
					servidor.setSoTimeout(0);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}

			}

		}
	}

	public void enviar(final String mensaje) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DatagramPacket datagramPacket = new DatagramPacket(mensaje.getBytes(), mensaje.length(), ipGrupo,
						PUERTO);

				try {
					servidor.send(datagramPacket);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void recibir() throws IOException {
		byte[] informacion = new byte[100];
		DatagramPacket datagramPacket = new DatagramPacket(informacion, informacion.length);

		servidor.receive(datagramPacket);

		String mensaje = new String(datagramPacket.getData()).trim();
		ejecutar(mensaje);

	}

	public void ejecutar(String mensaje) {
		if(identificado) {
		if (mensaje.contains("Identify")) {
			String[] separar = mensaje.split(";");
			enviar("MyIdEs:" + ID + ":" + velocidad + ":" + color);
			System.out.println("Envie mi id: " + ID);
			float vel = Float.parseFloat(separar[1]);
			int co = Integer.parseInt(separar[2]);
			int id = Integer.parseInt(separar[3]);
			log.addCaballo(vel, co, id);
		}

		if (mensaje.equals("comenzar")) {
			log.comenzarCaballos();
		}

		if (mensaje.equals("detener")) {
			log.detenerCaballos();
		}
		
		}else if (identificado == false) {
			if (mensaje.contains("MyIdEs:")) {
				String[] separa = mensaje.split(":");
				int tempId = Integer.parseInt(separa[1]);
				float vel = Float.parseFloat(separa[2]);
				int co = Integer.parseInt(separa[3]);
				System.out.println("Hola yo soy: " + tempId);
				log.addCaballo(vel, co, tempId);

				if (tempId >= ID && identificado == false) {
					this.ID = tempId + 1;
				}
			}
		}

	}

	@Override
	public void getDatos(String mensaje) {
		if (gano == false) {
			System.out.println("Ganador:" + ID);
			enviar("Ganador:" + ID);
			gano = true;
		}
	}

}
