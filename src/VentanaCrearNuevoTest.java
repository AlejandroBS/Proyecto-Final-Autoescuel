import com.test.alejandro.test.Test;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class VentanaCrearNuevoTest extends JDialog{

	private static final long serialVersionUID = 1L;

	private JLabel lbl_nomTest = new JLabel("Nombre del Test");
	private JLabel lbl_nPreg = new JLabel("<html>Número de preguntas<br/>(máximo 30)</html>");
	
	private JTextField txt_nomTest = new JTextField();
	private JTextField txt_nPreg = new JTextField("",20);
	
	private JButton btn_aceptar = new JButton("Aceptar");
	private JButton btn_cancelar = new JButton("Cancelar");

	//-----------------------------------------------------------------------------\\
	
	private JPanel contenedor = new JPanel(new BorderLayout(10,10));
	private JPanel pnl_elementosIzq = new JPanel(new GridLayout(3,1,4,4));
	private JPanel pnl_elementosDer = new JPanel(new GridLayout(3,1,4,4));
	
	public VentanaCrearNuevoTest(){
		
		this.setTitle("Crear Nuevo Test");
		this.setSize(280, 150);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		
		//-----------------------------------------------------------------------------\\
		
		lbl_nomTest.setFont(new Font("Arial", Font.PLAIN, 15));
		lbl_nPreg.setFont(new Font("Arial", Font.PLAIN, 15));
		
		txt_nomTest.setFont(new Font("Arial", Font.PLAIN, 15));
		txt_nPreg.setFont(new Font("Arial", Font.PLAIN, 15));
		
		btn_aceptar.setFont(new Font("Arial", Font.PLAIN, 15));
		btn_cancelar.setFont(new Font("Arial", Font.PLAIN, 15));
		
		//------------------------------------------------------------------------------\\
		
		pnl_elementosIzq.add(lbl_nomTest);
		pnl_elementosDer.add(lbl_nPreg);
		
		pnl_elementosIzq.add(txt_nomTest);
		pnl_elementosDer.add(txt_nPreg);
		
		pnl_elementosIzq.add(btn_cancelar);
		pnl_elementosDer.add(btn_aceptar);

		contenedor.add(pnl_elementosIzq, BorderLayout.WEST);
		contenedor.add(pnl_elementosDer, BorderLayout.CENTER);
		
		this.add(contenedor);
		
		btn_cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				acciones(e);
			}
		});
		btn_aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				acciones(e);
			}
		});
		txt_nPreg.addKeyListener(new KeyListener(){
			
			public void keyReleased(KeyEvent arg0) {}
			
			public void keyTyped(KeyEvent arg0) {
				char codigo = arg0.getKeyChar();
				if(txt_nPreg.getText().length()>0){
					if(Integer.parseInt(txt_nPreg.getText().charAt(0)+"")>3){
						arg0.consume();
					}
					if(Integer.parseInt(txt_nPreg.getText().charAt(0)+"")==3 && codigo!='0'){
						arg0.consume();
					}
				}
				if(txt_nPreg.getText().length() < 2){
					if(codigo < '0' || codigo > '9'){
						arg0.consume();
					}
				}
				else{
					arg0.consume();
				}
			}

			public void keyPressed(KeyEvent arg0) {}	
			
		});
		
	}
	
	public void acciones(ActionEvent e){
		JButton obj = (JButton) e.getSource();
		if(obj==btn_cancelar){
			this.dispose();
		}
		if(obj==btn_aceptar){
			try{
				if(txt_nomTest.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Introduce un nombre de fichero válido", "AVISO", JOptionPane.ERROR_MESSAGE);
				}
				if(txt_nPreg.getText().isEmpty() && Integer.parseInt(txt_nPreg.getText())>30){
					JOptionPane.showMessageDialog(null, "Introduce un número de preguntas válido", "AVISO", JOptionPane.ERROR_MESSAGE);
				}
				if(txt_nomTest.getText().isEmpty()==false && Integer.parseInt(txt_nPreg.getText())<=30){
					File dir = new File("data"+File.separator+txt_nomTest.getText());
					//if(dir.isDirectory()){
						dir.mkdir();
					//}
					File f = new File("data"+File.separator+txt_nomTest.getText()+File.separator+txt_nomTest.getText()+".test");
					if(f.exists()==true){
						int dialogButton = JOptionPane.YES_NO_OPTION;
						JOptionPane.showMessageDialog(null, "Ya existe un fichero con ese nombre ¿desea sobreescribirlo?", "AVISO", JOptionPane.YES_NO_OPTION);
						if(dialogButton == JOptionPane.YES_OPTION){
							remove(dialogButton);
							FileOutputStream fos = new FileOutputStream(f);
							ObjectOutputStream oos = new ObjectOutputStream(fos);
							oos.writeInt(Integer.parseInt(txt_nPreg.getText()));
							oos.writeInt(0);
							oos.writeObject( new Test( txt_nomTest.getText(), Integer.parseInt(txt_nPreg.getText()) ) );
							oos.close();
							
						}
						if(dialogButton == JOptionPane.NO_OPTION){
							remove(dialogButton);
						}
					}
					else{
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
						oos.writeInt(Integer.parseInt(txt_nPreg.getText()));
						oos.writeInt(0);
						oos.writeObject( new Test( txt_nomTest.getText(),Integer.parseInt(txt_nPreg.getText()) ) );
						oos.close();
						File imgs = new File("data"+File.separator+txt_nomTest.getText()+File.separator+"img");
						imgs.mkdir();
					}
					
				}
				
				this.dispose();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
}
