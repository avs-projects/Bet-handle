<?php

// Defining variables with null values
$email = $prenom = $nom = $pseudo = $capital = $date_naissance = $password = $passconf = "";
$email_err = $prenom_err = $nom_err = $pseudo_err = $capital_err = $date_naissance_err = $date_naissance_age_err = $password_err = $passconf_err = "";
 
// In case the form is submitted, we do the following
if($_SERVER["REQUEST_METHOD"] == "POST"){
 
    // Email validation
    if(empty(trim($_POST["email"]))){
        $email_err = "Veuillez entrer un email.";
    } else{
        $email = $_POST["email"];
        $reg = pg_query("SELECT * FROM parieur WHERE email = '$email' ");
        $rows = pg_num_rows($reg);
        if($rows >= 1){
            $email_err = "Cet email est déja utilisé.";
        } else{
            $email = strtolower(trim($_POST["email"]));
        }
    }
    
    // First name validation
    if(empty(trim($_POST["prenom"]))){
        $prenom_err = "Veuillez entrer un prenom.";
    } else{
        $prenom = trim($_POST["prenom"]);
    }
    
    // Name validation
    if(empty(trim($_POST["nom"]))){
        $nom_err = "Veuillez entrer un nom.";
    } else{
        $nom = trim($_POST["nom"]);
    }
    
    // Nickname validation
    if(empty(trim($_POST["pseudo"]))){
        $pseudo_err = "Veuillez entrer un pseudo.";
    } else{
        $pseudo = trim($_POST["pseudo"]);
    }
    
    // Capital validation
    if(empty(trim($_POST["capital"]))){
        $capital_err = "Veuillez entrer un capital.";
    } else{
        $capital = trim($_POST["capital"]);
    }
    
    // Age verification based on date of birth
    if(!empty(trim($_POST["date_naissance"]))){
        $T = explode('-', $_POST["date_naissance"]) ;
        $age = date('Y') - $T[0] ;
    }
    
    // Date of birth validation
    if(empty(trim($_POST["date_naissance"]))){
        $date_naissance_err = "Veuillez entrer une date de naissance.";
    }elseif($age<18){
        $date_naissance_age_err = "Vous n'avez pas l'âge requis.";
    } else{
        $date_naissance = trim($_POST["date_naissance"]);
    }
    
    // Password validation
    if(empty(trim($_POST["password"]))){
        $password_err = "Veuillez entrer un mot de passe.";     
    } elseif(strlen(trim($_POST["password"])) < 6){
        $password_err = "Votre mot de passe doit avoir au moins 6 caractéres.";
    } else{
        $password = trim($_POST["password"]);
    }
    
    // Validation of password confirmation
    if(empty(trim($_POST["passconf"]))){
        $passconf_err = "Veuillez confirmer votre mot de passe.";     
    } else{
        $passconf = trim($_POST["passconf"]);
        if(empty($password_err) && ($password != $passconf)){
            $passconf_err = "Les mot de passes ne sont pas identiques.";
        }
    }
    
    // Checking if there are no errors before inserting data with the database
    if(empty($email_err) && empty($prenom_err) && empty($nom_err) && empty($pseudo_err) && empty($capital_err) && empty($date_naissance_err) && empty($password_err) && empty($date_naissance_age_err) && empty($passconf_err)){
        
        // Definition of the parameters to enter in the database
        $param_email = $email;
        $param_prenom = $prenom;
        $param_nom = $nom;
        $param_pseudo = $pseudo;
        $param_capital = $capital;
        $param_date_naissance = $date_naissance;
        $param_password = md5($password);
        
        // Insertion of submitted data into the database
        $query = pg_query("INSERT INTO parieur (email, mdp, prenom, nom, pseudo, capital, date_naissance, pret, journee) VALUES ('$param_email','$param_password','$param_prenom','$param_nom','$param_pseudo','$param_capital','$param_date_naissance',false,1)");
        
        // Once the insertion and therefore the registration is done, the customer is sent back to the login page
        header("location: ./connexion.php");
    }
    
    // Closing the connection
    pg_close($connexion);
}

?>
