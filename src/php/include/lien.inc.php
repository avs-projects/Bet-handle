<?php

// Database connection parameters
$host = '127.0.0.1';
$dbname = 'postgres';
$user = 'postgres';
$password_co = 'silvasql';


// Database connection
$connexion = pg_connect("host='$host' dbname='$dbname' user='$user' password='$password_co'") or die('Connexion impossible : ' . pg_last_error());

?>
