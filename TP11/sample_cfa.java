import ij.*;
import ij.plugin.filter.*;
import ij.process.*;
import ij.gui.*;

public class sample_cfa implements PlugInFilter {

    ImagePlus imp;    // Fenêtre contenant l'image de référence
    int width;        // Largeur de la fenêtre
    int height;        // Hauteur de la fenêtre
    static float[] MASQUE_RB={0.25f,0.5f,0.25f,0.5f,1f,0.5f,0.25f,0.5f,0.25f};
    static float[] MASQUE_G={0.0f,0.25f,0.0f,0.25f,1f,0.25f,0.0f,0.25f,0.0f};

    public int setup(String arg, ImagePlus imp) {/* à compléter*/
        this.imp = imp;
        return PlugInFilter.DOES_8G;
    }

      
    public void run(ImageProcessor ip) {

        // Lecture des dimensions de la fenêtre
        width = imp.getWidth();
        height = imp.getHeight();
	
	//creation des plans rouge,vert et bleue
 	ImageProcessor rouge=cfa_samples(ip,1);
	ImageProcessor vert=cfa_samples(ip,2);
	ImageProcessor bleu=cfa_samples(ip,3);
	

	ImageStack samples_stack = imp.createEmptyStack();
	samples_stack.addSlice("rouge", rouge);	// Composante R
	samples_stack.addSlice("vert", vert);// Composante G
	samples_stack.addSlice("bleu", bleu);	// Composante B

	// Création de l'image résultat
	ImagePlus cfa_samples_imp = imp.createImagePlus();
	cfa_samples_imp.setStack("Échantillons couleur CFA", samples_stack);
	cfa_samples_imp.show();

	//fltrage des plans
	Convolver convolve=new Convolver();
	convolve.setNormalize(false);
	convolve.convolve(rouge,MASQUE_RB,3,3);
	convolve.convolve(vert,MASQUE_G,3,3);
	convolve.convolve(bleu,MASQUE_RB,3,3);
	ImageStack dematricage= imp.createEmptyStack();
	
	//affichage des plans filtrés
	dematricage.addSlice("rouge", rouge);	// Composante R
	dematricage.addSlice("vert", vert);// Composante G
	dematricage.addSlice("bleu", bleu);	// Composante B
	ImagePlus demat= imp.createImagePlus();
	demat.setStack("Échantillons couleur CFA", dematricage);
	demat.show();

    }

	ImageProcessor cfa_samples(ImageProcessor ip, int color){
		width = ip.getWidth();
        		height = ip.getHeight();
		ImageProcessor res=new ByteProcessor(width,height);
		//rouge
		if(color==1){
			for (int y=0; y<height; y+=2) {
            			for (int x=1; x<width; x+=2) {
					res.putPixel(x,y,ip.getPixel(x,y));
				}
			}
		}
		//vert
		if(color==2){
			for (int y=0; y<height; y++) {
            			for (int x=0; x<width; x++) {
					if((x+y)%2==0)
						res.putPixel(x,y,ip.getPixel(x,y));
				}
			}
		}
		//bleu
		if(color==3){
			for (int y=1; y<height; y+=2) {
            			for (int x=0; x<width; x+=2) {
					res.putPixel(x,y,ip.getPixel(x,y));
				}
			}
		}
		return res;
	}
}
