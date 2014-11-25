
abstract public class DCT1D{

	// ---------------------------------------------------------------------------------
	/**
	 * Transformation DCT 1D directe (m�thode de classe)
	 * @param imageOrigine(m) Signal 1D d'entr�e (double[])
	 * @return signaltransform(u) Signal transform� (double[])
	 */ 
	public static double[] forwardDCT(double[] imageOrigine) {
		int tailleSignal = imageOrigine.length;	// Taille du signal
		double k = Math.sqrt(2.0 / tailleSignal); // Facteur de normalisation
		double[] signaltransform = new double[tailleSignal];	// r�sulat
		for (int u = 0; u < tailleSignal; u++) {
			double cu = 1.0;
			if (u == 0)
				cu = 1.0 / Math.sqrt(2);	// Facteur de normalisation
			double somme = 0.0;
			for (int m = 0; m < tailleSignal; m++) {
				somme = somme + imageOrigine[m] * Math.cos(Math.PI * (m + 0.5) * u / tailleSignal);
			}
			signaltransform[u] = k * cu * somme;
		}
		return signaltransform;
	}

	// ---------------------------------------------------------------------------------
	/**
	 * Transformation DCT 1D inverse (m�thode de classe)
	 * @param signalTransform(u) Signal 1D transform� (double[])
	 * @return inverse(m) signal inverse (double[])
	 */
	public static double[] inverseDCT(double[] signalTransform) {
		int tailleSignal = signalTransform.length;	// Taille du signal
		double k = Math.sqrt(2.0 / tailleSignal);	// Facteur de normalisation
		double[] inverse = new double[tailleSignal];	// r�sulat
		for (int m = 0; m < tailleSignal; m++) {
			double somme = 0;
			for (int u = 0; u < tailleSignal; u++) {
				double cu = 1.0;
				if (u == 0)
					cu = 1.0/Math.sqrt(2);	// Facteur de normalisation d�pendant de u
				somme = somme + cu * signalTransform[u] * Math.cos(Math.PI * (m + 0.5) * u / tailleSignal);
			}
			inverse[m] = k * somme;
		}
		return inverse;
	}
}
