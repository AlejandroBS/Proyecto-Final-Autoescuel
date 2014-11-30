
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;

public class Pregunta implements Serializable {
	
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = -3902132514932282124L;
	//private ImageIcon imagen;
	private int numeroPregunta = 0;
	private String enunciado = "";
	private String respuestaA = "";
	private String respuestaB = "";
	private String respuestaC = "";
	private char respuestaCorrecta = 0;
	private String nombreTest = "";
	
	public Pregunta(String nombreTest, int numeroPregunta, String enunciado,
					String respuestaA, String respuestaB, String respuestaC,
					char respuestaCorrecta){
		this.nombreTest = nombreTest;
		this.numeroPregunta = numeroPregunta;
		this.enunciado = enunciado;
		this.respuestaA = respuestaA;
		this.respuestaB = respuestaB;
		this.respuestaC = respuestaC;
		this.respuestaCorrecta = Character.toUpperCase(respuestaCorrecta);
		
		//this.imagen = imagen;		
		
	}

	public String getEnunciado() {
		return enunciado;
	}

	public String getRespuestaA() {
		return respuestaA;
	}

	public String getRespuestaB() {
		return respuestaB;
	}

	public String getRespuestaC() {
		return respuestaC;
	}

	public char getRespuestaCorrecta() {
		return respuestaCorrecta;
	}
	
	public int getNumPregunta(){
		return numeroPregunta;
	}
		
	public ImageIcon getImagen() {
		System.out.println("getImagen();"+"data"+File.separator+nombreTest+File.separator+"img"+File.separator+"p"+numeroPregunta+".jpg");
                
            try {
                return new ImageIcon(ImageIO.read(new File("data"+File.separator+nombreTest+File.separator+"img"+File.separator+"p"+numeroPregunta+".jpg")));
            } catch (IOException ex) {
                Logger.getLogger(Pregunta.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
	}
	
	
}
