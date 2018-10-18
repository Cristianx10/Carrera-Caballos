package PistaDeCarreras;

import processing.core.PApplet;

public class Main extends PApplet{

	public static void main(String[] args) {
		PApplet.main("PistaDeCarreras.Main");
	}
	
	private Logica log;
	
	public void settings() {
		size(1200, 700);
	}
	
	public void setup() {
		log = new Logica(this);
	}
	
	public void draw() {
		
		log.draw();
	}

}
