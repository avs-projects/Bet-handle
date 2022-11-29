<?php
// Session initialization
session_start();
 
// Disabling all previous sessions
$_SESSION = array();
 
// Session destruction
session_destroy();
 
// Redirection to main page
header("location: ../index.php");
exit;

?>
