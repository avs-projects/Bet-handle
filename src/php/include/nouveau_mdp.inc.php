<?php
// Initialize a session
session_start();

 
// Defining variables with null values
$new_password = $confirm_password = "";
$new_password_err = $confirm_password_err = "";
 
// In case the form is submitted, we do the following
if(isset($_POST['submit1'])){
 
    // Validation of the new password
    if(empty(trim($_POST["new_password"]))){
        $new_password_err = "Veuillez entrer un mot de passe.";     
    } elseif(strlen(trim($_POST["new_password"])) < 6){
        $new_password_err = "Votre mot de passe doit avoir au moins 6 caractéres.";
    } else{
        $new_password = trim($_POST["new_password"]);
    }
    
    // Validation of confirmation of the new password
    if(empty(trim($_POST["confirm_password"]))){
        $confirm_password_err = "Veuillez entrer la confirmation de votre mot de passe.";
    } else{
        $confirm_password = trim($_POST["confirm_password"]);
        if(empty($new_password_err) && ($new_password != $confirm_password)){
            $confirm_password_err = "Les mots de passe ne sont pas identiques.";
        }
    }
        
    // Checking if there are no errors before inserting data with the database
    if(empty($new_password_err) && empty($confirm_password_err)){
        
        // Parameter preparation
        $param_email = $_SESSION["email"];
        $param_password = password_hash($new_password, PASSWORD_DEFAULT);
        
        // Preparing the sql query
        $sql = "UPDATE parieur SET mdp = '$param_password' WHERE email = '$param_email'";
        
        // Perform sql query and return user to login page
        $query3 = pg_query($sql);
                session_destroy();
                header("location: ../connexion/connexion.php");
                exit();
    }
    
}
?>
