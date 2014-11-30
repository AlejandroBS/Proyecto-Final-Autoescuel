import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class VentanaGestionarTest extends JFrame{	
	
	private static final long serialVersionUID = 1L;
	
	private JButton btn_crearNuevoTest = new JButton("Crear Nuevo Test");
	private JButton btn_editarTests = new JButton("Editar Tests");
	private JButton btn_GestionarPaquetes = new JButton("Gestionar Paquetes");
	
	
	//----------------------------------------------------------------------\\
	
	private JPanel contenedor = new JPanel(new BorderLayout(10,10));
	private JPanel pnl_elementos = new JPanel(new GridLayout(3,1,4,4));
	
	public VentanaGestionarTest(){
		
		this.setTitle("Gestionar Tests");
		this.setSize(250,210);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//------------------------------------------------------------------------\\

		btn_crearNuevoTest.setFont(new Font("Arial", Font.PLAIN, 20));
		btn_editarTests.setFont(new Font("Arial", Font.PLAIN, 20));
		btn_GestionarPaquetes.setFont(new Font("Arial", Font.PLAIN, 20));
		
		//------------------------------------------------------------------------\\
		pnl_elementos.add(btn_crearNuevoTest);
		pnl_elementos.add(btn_editarTests);
		pnl_elementos.add(btn_GestionarPaquetes);
		
		contenedor.add(pnl_elementos, BorderLayout.CENTER);
		this.add(contenedor);
		
		btn_crearNuevoTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                            try {
                                acciones(e);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(VentanaGestionarTest.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		});
		btn_editarTests.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                            try {
                                acciones(e);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(VentanaGestionarTest.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		});
		btn_GestionarPaquetes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                            try {
                                acciones(e);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(VentanaGestionarTest.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		});
	
	}
	
	public void acciones(ActionEvent e) throws ClassNotFoundException{
		JButton obj = (JButton) e.getSource();
			if(obj==btn_crearNuevoTest){
				VentanaCrearNuevoTest ventana = new VentanaCrearNuevoTest();
				ventana.setVisible(true);
			}
			if(obj==btn_editarTests){
				VentanaListaFicherosTest ventana = new VentanaListaFicherosTest();
				ventana.setVisible(true);
			}
			if(obj==btn_GestionarPaquetes){
				
			}
	}
	
	
	
	
	

}
