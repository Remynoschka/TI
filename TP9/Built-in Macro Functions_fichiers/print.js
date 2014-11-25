/* 2012 Tiago Ferreira */
function replaceLinks() {
    "use strict";
	var lnk = document.links;
	for (var i=0; i<lnk.length; i++) {
		if (lnk[i].href.indexOf('#') != -1) {
			if (lnk[i].href.indexOf('#Top') != -1) {
				lnk[i].setAttribute('class', 'hidden');
			}
			else {
				lnk[i].setAttribute('class', 'internal');
			}
		} else {
			lnk[i].setAttribute('class', 'external');
			lnk[i].href = lnk[i].href;
		}
	}
    window.print();
}