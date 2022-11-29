<?php

// Check if the user is not logged in, in this case send him to the login page
if(!isset($_SESSION["connecte"]) || $_SESSION["connecte"] !== true){
    header("location: ./connexion/connexion.php");
    exit;
}

?>
