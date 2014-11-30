import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPanelFoto extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8002576760561410705L;
	private ImageIcon  imagen;
	private String rutaImagen;
	
	public JPanelFoto(){
		super();
	}
	
	public JPanelFoto(BorderLayout bl){
		super(bl);
	}
	
	public JPanelFoto(String nombre){
		super();
		//initialize();
		this.imagen = new ImageIcon(nombre);
		//this.setSize(imagen.getIconWidth(),imagen.getIconHeight());
	}
	protected void paintComponent(Graphics g) {
		if(imagen!=null){
			Dimension d = getSize();
			g.drawImage(imagen.getImage(),0,0,d.width,d.height,null);
			//this.setOpaque(false);
			//super.paintComponent(g);
		}
	}
	public void pintar(){
		paintComponent(this.getGraphics());
	}
	
	public void pintar(Object imagen){
		//this.rutaImagen = rutaImagen;
		//imagen = new ImageIcon(rutaImagen);
                
		if(imagen instanceof ImageIcon){
			this.imagen = (ImageIcon) imagen;
			paintComponent(this.getGraphics());
		}
		if(imagen instanceof JLabel){
			this.imagen = (ImageIcon) null;
			paintComponent(this.getGraphics());
			add((JLabel)imagen);
		}
		//initialize();
		
	}
}
