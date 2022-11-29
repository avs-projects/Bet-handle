<?php

// Verification if the user is already logged in, in this case send him to the welcome.php page
if(isset($_SESSION["connecte"]) && $_SESSION["connecte"] === true){
  header("location: ../bienvenue.php");
  exit;
}

?>