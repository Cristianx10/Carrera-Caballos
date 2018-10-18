package Intermediario;

public class Main {

	public static void main(String[] args) {

		Servidor server = new Servidor();
		server.start();
		
		ClienteUnicast intermediario = new ClienteUnicast(server);
		intermediario.start();
		
		server.getObserver(intermediario);
		
	
		intermediario.enviar("comenzar");
		
		

	}

}
