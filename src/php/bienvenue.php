<?php

// Session initialization
session_start();

// Call, incorporation of the file giving managing the session
include "./include/nonsession.inc.php";

// Call, incorporation of the file managing the connection to the database
include "./include/lien.inc.php";

// Call, header incorporation of connected people
include "./include/headerco.inc.php";

// Call, incorporation of the file allowing the display of the player's bets
include "./include/paris.inc.php";

// Call, embedding footer
include './include/footer.inc.php'

?>
