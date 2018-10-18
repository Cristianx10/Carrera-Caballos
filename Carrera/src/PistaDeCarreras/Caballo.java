package PistaDeCarreras;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Caballo extends Thread {

	private PApplet app;
	private Logica log;
	private PVector pos;
	private int radio;
	private int color;
	private boolean vivo;
	private boolean movimiento, gano;
	private float velocidad;
	private int ID;
	private Observer observer;

	public Caballo(Logica log, int color, float velocidad, int ID) {
		this.log = log;
		this.ID = ID;
		this.app = log.getPApplet();
		this.radio = 40;
		this.pos = new PVector(radio, (radio * 2) * (log.numCaballo() + 1) - radio);
		this.color = color;
		this.vivo = true;
		this.movimiento = false;
		this.gano = false;
		this.velocidad = velocidad;
		start();
	}

	public void draw() {
		app.fill(color);
		app.ellipseMode(PConstants.CENTER);
		app.ellipse(pos.x, pos.y, radio * 2, radio * 2);
		app.fill(0);
		app.textAlign(PConstants.CENTER, PConstants.CENTER);
		app.text(ID, pos.x, pos.y);
	}

	public void asignarId(int id) {
		this.ID = id;
	}

	@Override
	public void run() {
		while (vivo) {
			try {
				movimiento();
				sleep(60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setMovimiento(boolean accion) {
		this.movimiento = accion;
	}

	private void movimiento() {
		if (movimiento) {
			pos.x += velocidad;
		} else {

		}
		
		if (pos.x > app.width - radio) {
			movimiento = false;
		}

		if (observer != null) {
			if (pos.x > app.width - radio) {
				movimiento = false;
				observer.getDatos("Ganador:");
			}
		}

	}

	public void setObserver(Observer o) {
		this.observer = o;
	}

}
