function includeHTML() {
  var z, i, elmnt, file, xhttp;
  /* Loop through a collection of all HTML elements: */
  z = $("*");
  console.log(z);
  for (i = 0; i < z.length; i++) {
    var elmnt = z[i];
    /*search for elements with a certain atrribute:*/
    file = elmnt.getAttribute("include-html");
    if (file) {
      /* Make an HTTP request using the attribute value as the file name: */
      
      $(elmnt).load(file);
      /* Exit the function: */
      return;
    }
  }
}
includeHTML();