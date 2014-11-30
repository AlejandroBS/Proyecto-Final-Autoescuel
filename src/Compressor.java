
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compressor {

    ZipOutputStream zos = null;
    ZipEntry entrada = null;
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    String nombreFicheroZip = "";
    static final int DIRECTORIO = 0;
    static final int FICHERO = 1;
    String rutaFicheroZip = "";

    /**
     *
     * @param nombreFicheroZip Contiene el nombre del fichero ".zip" que está creando.
     * @param rutaFicheroParaComprimir Contiene la ruta de un fichero o directorio el cual va a comprimirse dentro del fichero ".zip" que vamos a crear.
     * @return Devuelve true si se pudo inicializar correctamente; En caso contrario devuelve false.
     */
    public Compressor(String rutaCreacionFicheroZip,String nombreFicheroZip) {
        try {
            this.nombreFicheroZip = nombreFicheroZip;
            this.rutaFicheroZip = rutaCreacionFicheroZip;
            
            zos = new ZipOutputStream(new FileOutputStream(rutaCreacionFicheroZip+File.separator+nombreFicheroZip));
            zos.setLevel(Deflater.DEFAULT_COMPRESSION);
            zos.setMethod(Deflater.DEFLATED);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compressor.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Compressor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
        }
    }
    
    public void compress (String rutaAComprimir, String rutaRaizInteriorZip){
        File [] files = new File(rutaAComprimir).listFiles();
        for(int i = 0; i<files.length;i++){
            if(files[i].isDirectory()){
                //addEntry(rutaRaizInteriorZip+File.separator+files[i].getName()+File.separator, Compressor.DIRECTORIO);
                //System.out.println("Añadida entrada directorio: "+rutaRaizInteriorZip+File.separator+files[i].getName()+File.separator);
                
                compress(rutaAComprimir+File.separator+files[i].getName(), rutaRaizInteriorZip+File.separator+files[i].getName());
            }
            else{
                System.out.println(files[i].getName()+"?=="+nombreFicheroZip);
                if(files[i].getName().equals(nombreFicheroZip)==false){
                    System.out.println("entra");
                    addEntry(rutaRaizInteriorZip+File.separator+files[i].getName(), Compressor.FICHERO);
                    System.out.println("Añadida entrada fichero: "+rutaRaizInteriorZip+File.separator+files[i].getName());
                    
                    addFile(rutaAComprimir+File.separator+files[i].getName());
                    System.out.println("Añadido Fichero: "+rutaAComprimir+File.separator+files[i].getName());
                }
            }
        }
    }
    
    /**
     * 
     * @param entry Contiene un String con la ruta con la que se va a agregar un nuevo "ZipEntry" dentro del fichero ".zip".
     * @param filetype Contiene un entero (cero o uno) que indica si la proxima entrada es un directorio o un fichero.
     * @return Devuelve true si se pudo agregar el "ZipEntry"; En caso contrario devuelve false.
     */
    private boolean addEntry(String entry, int filetype) {
        try {
            entrada = new ZipEntry(entry);
            zos.putNextEntry(entrada);

            if (filetype == Compressor.DIRECTORIO) {
                zos.closeEntry();
            }

        } catch (IOException ex) {
            Logger.getLogger(Compressor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            return true;
        }
    }
    
    /**
     * 
     * @param rutaFichero Contiene la ruta del fichero que se va a abrir y leer para escribirlo dentro del fichero ".zip", teniendo en cuenta que previamente se ha creado un "ZipEntry" para ese fichero.
     * @return Devuelve true si se pudo agregar completamente el fichero y cerrar el "ZipEntry" correspondiente; En caso contrario devuelve false.
     */    
    private boolean addFile(String rutaFichero) {
        try {
            fis = new FileInputStream(rutaFichero);
            bis = new BufferedInputStream(fis);

            int leido = 0;
            byte[] buffer = new byte[1024];

            while ((leido = bis.read(buffer)) >= 0) {
                zos.write(buffer, 0, leido);
            }

            bis.close();
            fis.close();
            zos.closeEntry();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compressor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Compressor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            return true;
        }
    }
    
    /**
     * 
     * @return Devuelve true si se pudo cerrar el fichero ".zip"; Devuelve false en caso contrario.
     */
    public  boolean close() {
        try {
            zos.close();
        } catch (IOException ex) {
            Logger.getLogger(Compressor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            return true;
        }
    }

}