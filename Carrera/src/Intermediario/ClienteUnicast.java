package Intermediario;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClienteUnicast extends Thread implements Observador {

	DatagramSocket socket;
	int PUERTO, PUERTOINCIAL;
	InetAddress address;
	GetData datos;

	public ClienteUnicast(GetData datos) {
		this.datos = datos;
		comnunicar();
	}

	@Override
	public void run() {

		while (true) {
			recibir();
		}
	}

	public void comnunicar() {
		try {
			PUERTO = 58193;
			PUERTOINCIAL = 5001;
			socket = new DatagramSocket(PUERTOINCIAL);
			address = InetAddress.getByName("127.0.0.1");

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
				DatagramPacket datagramPacket = new DatagramPacket(mensaje.getBytes(), mensaje.length(), address,
						PUERTO);
				
				address = datagramPacket.getAddress();
				PUERTO = datagramPacket.getPort();
				
				System.out.println("Direccion: " + address + "   Puerto: " + PUERTO);

				
				try {
					socket.send(datagramPacket);
					System.out.println("Dato enviado");
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

			System.out.println("Esperando datos");
			socket.receive(datagramPacket);

			String mensaje = new String(datagramPacket.getData()).trim();

			address = datagramPacket.getAddress();
			PUERTO = datagramPacket.getPort();
			
			System.out.println("Direccion: " + address + "   Puerto: " + PUERTO);

			enviar("De acuerdo");
			datos.getRecibido(mensaje);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public interface GetData {
		public void getRecibido(String mensaje);
	}

	int ganadores = 0;

	@Override
	public void getMensaje(String mensaje) {

		if (ganadores >= 0 && ganadores < 3) {
			
			System.out.println("llego");
			if (mensaje.contains("Ganador:")) {
				ganadores++;
				String[] separa = mensaje.split(":");
				enviar("Puesto-" + ganadores + "-: " + separa[1]);
				System.out.println("Envio enviador");
			}
		}
	}

}
