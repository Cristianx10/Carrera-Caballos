package PistaDeCarreras;

import java.util.ArrayList;

import processing.core.PApplet;

public class Logica {
	
	public PApplet app;
	public ArrayList<Caballo> caballos;
	

	public Logica(PApplet app) {
		this.app = app;
		this.caballos = new ArrayList<>();
		
		conexionCaballo caballo = new conexionCaballo(this);
		caballo.start();
		
	}
	
	public void draw() {
		app.background(255);
		for (int i = 0; i < caballos.size(); i++) {
			Caballo c = caballos.get(i);
			c.draw();
		}
	}
	
	public int numCaballo() {
		return caballos.size();
	}
	
	public void addCaballoInicial(Caballo b) {
		
		this.caballos.add(b);
	}
	
	public void addCaballo(float velocidad, int color, int ID) {
		this.caballos.add(new Caballo(this, color, velocidad, ID));
	}
	
	public void comenzarCaballos() {
		for (int i = 0; i < caballos.size(); i++) {
			Caballo c = caballos.get(i);
			c.setMovimiento(true);
		}
	}
	
	public void detenerCaballos() {
		for (int i = 0; i < caballos.size(); i++) {
			Caballo c = caballos.get(i);
			c.setMovimiento(false);
		}
	}
	
	
	public PApplet getPApplet() {
		return app;
	}
	
}
