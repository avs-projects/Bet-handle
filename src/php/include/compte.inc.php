<?php

if (session_status() == PHP_SESSION_ACTIVE) {

// sql query parameters
$param_email = $_SESSION["email"];
// Selection of bettor table data that corresponds to the logged in user's account
$query = "SELECT * FROM parieur WHERE email = '$param_email'";
$resultat = pg_query($query);
 
// Display of results in html
while($data = pg_fetch_array($resultat)) {
	$data_nom = $data['nom'];
	$data_prenom = $data['prenom'];
    $data_email = $data['email'];
	$data_pseudo = $data['pseudo'];
	$data_capital = $data['capital'].'€';
	$data_date_naissance = $data['date_naissance'];
    
}
// Release of results
pg_free_result($resultat);

// Closing the connection
pg_close($connexion);
}
?>
