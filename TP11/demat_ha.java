import ij.*;
import ij.plugin.filter.*;
import ij.process.*;
import ij.gui.*;

public class demat_ha implements PlugInFilter {

    ImagePlus imp;    // Fenêtre contenant l'image de référence
    int width;        // Largeur de la fenêtre
    int height;        // Hauteur de la fenêtre
    static float[] MASQUE_RB={0.25f,0.5f,0.25f,0.5f,1f,0.5f,0.25f,0.5f,0.25f};

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
	ImageProcessor vert=est_G_hamilton(ip);
	ImageProcessor bleu=cfa_samples(ip,3);

	//filtrage des plans rouge et bleue
	Convolver convolve=new Convolver();
	convolve.setNormalize(false);
	convolve.convolve(rouge,MASQUE_RB,3,3);
	convolve.convolve(bleu,MASQUE_RB,3,3);	

	// Calcul des échantillons de chaque composante de l'image CFA
	ImageStack samples_stack = imp.createEmptyStack();
	samples_stack.addSlice("rouge", rouge);	// Composante R
	samples_stack.addSlice("vert", vert);// Composante G
	samples_stack.addSlice("bleu", bleu);	// Composante B

	// Création de l'image résultat
	ImagePlus cfa_samples_imp = imp.createImagePlus();
	cfa_samples_imp.setStack("Échantillons couleur CFA", samples_stack);
	cfa_samples_imp.show();

    }
	//reupere le plan d'une couleur pour une image donnée
	
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

	ImageProcessor est_G_hamilton(ImageProcessor cfa_ip){
		ImageProcessor est_ip= cfa_ip.duplicate();
		width = cfa_ip.getWidth();
       	height = cfa_ip.getHeight();
		int valPixel;
		//calcul pour les pixels correspondant aux rouges
		for (int y=0; y<height; y+=2) {
           	for (int x=1; x<width; x+=2) {
				//recuperation de la valeur des pixels voisins
				int  g_10=cfa_ip.getPixel(x-1,y)&0xff;
				int g10=cfa_ip.getPixel(x+1,y)&0xff;
				int r=cfa_ip.getPixel(x,y)&0xff;
				int r_20=cfa_ip.getPixel(x-2,y)&0xff;
				int r20=cfa_ip.getPixel(x+2,y)&0xff;
				int g0_1=cfa_ip.getPixel(x,y-1)&0xff;
				int g01=cfa_ip.getPixel(x,y+1)&0xff;
				int r0_2=cfa_ip.getPixel(x,y-2)&0xff;
				int r02=cfa_ip.getPixel(x,y+2)&0xff;
				//calcul de deltaX et deltaY
				int deltaX=Math.abs(g_10-g10)+Math.abs(2*r-r_20-r20);
				int deltaY=Math.abs(g0_1-g01)+Math.abs(2*r-r0_2-r02);					
				//affectation de la valeur du pixel selon la direction du gradient
				if(deltaX<deltaY){
					valPixel=(g0_1+g10)/2+(2*r-r_20-r20)/4;
					est_ip.putPixel(x,y,valPixel);			
				}
				if(deltaX>deltaY){
					valPixel=(g0_1+g01)/2+(2*r-r0_2-r02)/4;
					est_ip.putPixel(x,y,valPixel);			
				}		
				if(deltaX==deltaY){
					valPixel=(g0_1+g_10+g01+g10)/4+(2*r-r_20-r20-r0_2-r02)/8;
					est_ip.putPixel(x,y,valPixel);			
				}		
			}
		}
		//calcul pour les pixels correspondant aux bleus
		for (int y=1; y<height; y+=2) {
            for (int x=0; x<width; x+=2) {
				//recuperation de la valeur des pixels voisins
				int  g_10=cfa_ip.getPixel(x-1,y)&0xff;
				int g10=cfa_ip.getPixel(x+1,y)&0xff;
				int b=cfa_ip.getPixel(x,y)&0xff;
				int b_20=cfa_ip.getPixel(x-2,y)&0xff;
				int b20=cfa_ip.getPixel(x+2,y)&0xff;
				int g0_1=cfa_ip.getPixel(x,y-1)&0xff;
				int g01=cfa_ip.getPixel(x,y+1)&0xff;
				int b0_2=cfa_ip.getPixel(x,y-2)&0xff;
				int b02=cfa_ip.getPixel(x,y+2)&0xff;
				//calcul de deltaX et deltaY
				int deltaX=Math.abs(g_10-g10)+Math.abs(2*b-b_20-b20);
				int deltaY=Math.abs(g0_1-g01)+Math.abs(2*b-b0_2-b02);
				//affectation de la valeur du pixel selon la direction du gradient
				if(deltaX<deltaY){
					valPixel=(g0_1+g10)/2+(2*b-b_20-b20)/4;
					est_ip.putPixel(x,y,valPixel);			
				}
				if(deltaX>deltaY){
					valPixel=(g0_1+g01)/2+(2*b-b0_2-b02)/4;
					est_ip.putPixel(x,y,valPixel);			
				}		
				if(deltaX==deltaY){
					valPixel=(g0_1+g_10+g01+g10)/4+(2*b-b_20-b20-b0_2-b02)/8;
					est_ip.putPixel(x,y,valPixel);			
				}		
			}
		}				
		return est_ip;
	}
}
