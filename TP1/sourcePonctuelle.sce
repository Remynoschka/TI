// Définition des échantillons sur un axe
axe = [0:99] / 100 + 5e-3;
// Définition des éléments de surface
x = ones (1:100)' * axe;
y = axe' * ones (1:100);
// Position de la source
xs = 0.5;
ys = 0.5;
// Calcul de la distance
d = sqrt ((x - xs).^2 + (y - ys).^2);
surface = 2*%pi;

Io = 100/(2*%pi);
h = 0.5;
r = sqrt (d.^2 + h^2);
cos_a = h ./ r;
Ip = Io .* cos_a;
Ep=Io*h./(r.^3);
Epl = Ip .* cos_a ./ r.^2;
disp(cos_a);
// Trace de la fonction distance
plot3d (axe, axe, Epl);
// Visualisation sous forme d'image en niveaux de gris
imshow (Epl);
