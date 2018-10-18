package Intermediario;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Servidor extends Thread implements ClienteUnicast.GetData{

	MulticastSocket servidor;
	int PUERTO;
	InetAddress ipGrupo;
	
	Observador observador;
	
	
	public Servidor() {
		
		inicializar();
	}
	
	public void getObserver(Observador observador) {
		this.observador = observador;
	}

	@Override
	public void run() {
		
		while(true) {
			recibir();
		}

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

	public void enviar(final String mensaje) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DatagramPacket datagramPacket = new DatagramPacket(mensaje.getBytes(), mensaje.length(), ipGrupo,
						PUERTO);

				try {
					servidor.send(datagramPacket);
					System.out.println("Envio dato");
					
					
					
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

			servidor.receive(datagramPacket);

			String mensaje = new String(datagramPacket.getData()).trim();
			
			System.out.println("Servidor recivio: " + mensaje);
			observador.getMensaje(mensaje);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getRecibido(String mensaje) {
		
		if(mensaje.equals("comenzar")) {
			enviar("comenzar");
		}
		
		if(mensaje.equals("parar")) {
			enviar("detener");
		}
		
	}

}
