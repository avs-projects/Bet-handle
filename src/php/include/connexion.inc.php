<?php

// Defining variables with null values
$email = $password = "";
$email_err = $password_err = "";
 
// In case the form is submitted, we do the following
if($_SERVER["REQUEST_METHOD"] == "POST"){
 
    // Validation of the email entered
    if(empty(trim($_POST["email"]))){
        $email_err = "Veuillez entrer un email.";
    } else{
        $email = strtolower(trim($_POST["email"]));
    }
    
    // Validation of the entered password
    if(empty(trim($_POST["password"]))){
        $password_err = "Veuillez entrer un mot de passe.";
    } else{
        $password = trim($_POST["password"]);
    }
    
    // Checking for errors before communicating with the database
    if(empty($email_err) && empty($password_err)){
        // Preparing sql commands
        $sql = "SELECT email FROM parieur WHERE email ='$email'";
        $sql2 = "SELECT mdp FROM parieur WHERE email ='$email'";
        $sql3 = "SELECT id_parieur FROM parieur WHERE email ='$email'";
        $query = pg_query($sql);
        // Solution calculate the number of rows and see if ==1 ok else = false
        $query2 = pg_query($sql2);
        $query3 = pg_query($sql3);
        $mdphash = pg_fetch_result($query2,0);
        $id_parieur = pg_fetch_result($query3,0);
        $rows = pg_num_rows($query);
        
        // Checking if the email exists
        if($rows == 1){
            // Checking if the password is valid according to the account
            if(md5($password) == $mdphash){
                // Valid email and password = login
                session_start();
                            
                // Saving information in session
                $_SESSION["connecte"] = true;
                $_SESSION["id"] = $id_parieur;
                $_SESSION["email"] = $email;                            
                            
                // Redirection to the welcome.php page (logged in page)
                header("location: ../bienvenue.php");
            } else{
                // Send an error message if the password is wrong
                $password_err = "Le mot de passe n'est pas valide.";
            }
        } else{
            // Send an error message if the email does not exist
            $email_err = "Ce compte n'existe pas.";
        }
    }
    // closing the connection with the database
    pg_close($connexion);
}

?>
