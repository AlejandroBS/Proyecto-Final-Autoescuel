import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class VentanaListaFicherosTest extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel pnl_elementos = new JPanel(new BorderLayout(10,10));
	private JPanel contenedor = new JPanel(new BorderLayout());
	private JPanel pnl_botones = new JPanel(new BorderLayout());
	
	private JButton btn_editar = new JButton("Editar");
	private JButton btn_eliminar = new JButton("Eliminar");
	
	private String[] vstr_nombreColumnas = {"Fichero","Número preguntas máximo","Número preguntas introducidas"};
	private DefaultTableModel dtm_modelo = null;
	
	public JTable tbl_ficheros= null;
	
	private JScrollPane jsp = null;
	private String testElegido = "";
	private String rutaTestElegido = "";
	private int numPregMax = 0;
	private ObjectInputStream oiss=null;
	ArrayList<File> listaFicheros = new ArrayList<File>();
	
        //------------------------------------------------------------------------------------------------------------------
        
	public VentanaListaFicherosTest() throws ClassNotFoundException{
		this.setSize(500,300);
		this.setLocationRelativeTo(null);
		dtm_modelo = new DefaultTableModel(null, vstr_nombreColumnas);
		
		tbl_ficheros =new JTable(dtm_modelo){
			/**
			 * 
			 */
			private static final long serialVersionUID = -5719113667379507627L;

			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false;
			}
		};

		jsp = new JScrollPane(tbl_ficheros);		
		
		tbl_ficheros.getSelectionModel().addListSelectionListener(new ListSelectionListener() {   
			public void valueChanged(ListSelectionEvent arg0) {
				testElegido = listaFicheros.get(tbl_ficheros.getSelectedRow()).getPath().substring(0,listaFicheros.get(tbl_ficheros.getSelectedRow()).getPath().indexOf('.'));
				testElegido = testElegido.substring(5);
                                testElegido = testElegido.substring(0,testElegido.indexOf("\\"));
				
				rutaTestElegido = listaFicheros.get(tbl_ficheros.getSelectedRow()).getAbsolutePath();
				
				try {
					oiss = new ObjectInputStream(new FileInputStream(rutaTestElegido));
					numPregMax = oiss.readInt();
					oiss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		btn_editar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(testElegido.isEmpty()==false && numPregMax>0){
					VentanaEditarTests ventana = new VentanaEditarTests(rutaTestElegido, testElegido, numPregMax);
					ventana.setVisible(true);
					dispose();
				}
			}
		});
		
		btn_eliminar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(testElegido.isEmpty()==false){
                                    try {
                                        File f = new File(rutaTestElegido);
                                        f.delete();
                                        VentanaListaFicherosTest vent = new VentanaListaFicherosTest();
                                        vent.setVisible(true);
                                        dispose();
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(VentanaListaFicherosTest.class.getName()).log(Level.SEVERE, null, ex);
                                    }
				}
			}
		});
		
		buscarFicheros();
		
		pnl_botones.add(btn_eliminar, BorderLayout.WEST);
		pnl_botones.add(btn_editar, BorderLayout.EAST);
		
		pnl_elementos.add(new JLabel("Escoge el fichero a editar"), BorderLayout.NORTH);
		pnl_elementos.add(jsp, BorderLayout.CENTER);
		pnl_elementos.add(pnl_botones, BorderLayout.SOUTH);
		
		contenedor.add(pnl_elementos,BorderLayout.CENTER);
		this.add(contenedor);		
		
	}
	
	public void listaFicherosRecursiva(File archivo){
		
		File[] files = archivo.listFiles();
		
		for(int i = 0; i<files.length;i++){
			if(files[i].isDirectory()){
				listaFicherosRecursiva(files[i]);
			}
			else{
				if(files[i].getName().endsWith(".test"))
				listaFicheros.add(files[i]);
			}
		}
	}
	
	public void buscarFicheros() throws ClassNotFoundException{
		
		File f = new File("data"+File.separator);
		ObjectInputStream oos;
		String[] fila = new String[3];
		
		listaFicherosRecursiva(f);
		
		for(int i = 0; i<listaFicheros.size(); i++){
			fila[0]=listaFicheros.get(i).getPath().substring(0,listaFicheros.get(i).getPath().indexOf('.'));
			//fila[0]=fila[0].substring(5);
                        fila[0]=fila[0].substring(fila[0].lastIndexOf("\\")+1, fila[0].length());
			try {
				oos = new ObjectInputStream(new FileInputStream(listaFicheros.get(i)));
                                
				fila[1]=oos.readInt()+"";
				fila[2]=oos.readInt()+"";
				//fila[0]=((Test) oos.readObject()).getNombreTest();
                                oos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dtm_modelo.addRow(fila);
		}
		
		
	}

	
}
