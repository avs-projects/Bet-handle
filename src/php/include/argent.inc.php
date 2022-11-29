<?php

// Defining variables with null values
$ajout_argent = $retrait_argent = "";
$ajout_argent_err = $retrait_argent_err = $retrait_exces_err = "";
 
// Execution of the form and database during form submission
if(isset($_POST['submit2'])){
 
    // Validation of the amount of money added
    if(empty(trim($_POST["ajout_argent"]))){
        $ajout_argent_err = "Vous n'avez pas entré de somme.";     
    } else{
        $ajout_argent = trim($_POST["ajout_argent"]);
    }
    
    // Validation of the amount of money withdrawn
    if(empty(trim($_POST["retrait_argent"]))){
        $retrait_argent_err = "Vous n'avez pas entré de somme.";     
    } else{
        $retrait_argent = trim($_POST["retrait_argent"]);
    }
        
    // Checking for errors before entering information into the database
    // IfOTHERWISE IF the addition of money is fulfilled and not the withdrawal of money then:
    if(empty($ajout_argent_err) && !empty($retrait_argent_err)){
        // Preparation of the parameters and the sql query to know how much the account of the connected user has
        $param_email = $_SESSION["email"];
        $sql_email = "SELECT capital FROM parieur WHERE email ='$param_email'";
        $query_email = pg_query($sql_email);
        $capital_actuel = pg_fetch_result($query_email, 0);
        $somme = $capital_actuel + $ajout_argent;
        // Preparation of parameters and sql query to add money
        $sql_nouvelle_somme = "UPDATE parieur SET capital = '$somme' WHERE email = '$param_email'";
        $query3 = pg_query($sql_nouvelle_somme);
                header("location: ../connexion/compte.php");
    // else if the addition of money is not fulfilled and the withdrawal of money yes then:
    } elseif(!empty($ajout_argent_err) && empty($retrait_argent_err)) { 
        // Preparation of the parameters and the sql query to know how much the account of the connected user has
        $param_email = $_SESSION["email"];
        $sql_email = "SELECT capital FROM parieur WHERE email ='$param_email'";
        $query_email = pg_query($sql_email);
        $capital_actuel = pg_fetch_result($query_email, 0);
        // If the capital present in the account is less than the sum that the user wishes to withdraw then error message
        if($capital_actuel < $retrait_argent){
            $retrait_exces_err = 'La somme que vous voulez retirer est supérieur à la somme disponible.';
        }else{ // SINON préparation des paramètres et de la requete sql pour retirer de l'argent
        $somme = $capital_actuel - $retrait_argent;
        $sql_nouvelle_somme = "UPDATE parieur SET capital = '$somme' WHERE email = '$param_email'";
        $query3 = pg_query($sql_nouvelle_somme);
                header("location: ../connexion/compte.php");
        }
    // else if adding money and withdrawing are met then:
    } elseif(empty($ajout_argent_err) && (empty($retrait_argent_err))) {
        // Preparation of the parameters and the sql query to know how much the account of the connected user has
        $param_email = $_SESSION["email"];
        $sql_email = "SELECT capital FROM parieur WHERE email ='$param_email'";
        $query_email = pg_query($sql_email);
        $capital_actuel = pg_fetch_result($query_email, 0);
        // Calculation of the result of the various operations that the user wishes to perform (removal + addition)
        $somme = $capital_actuel + $ajout_argent - $retrait_argent;
        // If the player obtains a capital less than 0 he does not have the amount he wishes to withdraw so error message
        if($somme < 0){
            $retrait_exces_err = 'La somme que vous voulez retirer est supérieur à la somme disponible.';
        } else{ // else the operations desired by the user are carried out
        $sql_nouvelle_somme = "UPDATE parieur SET capital = '$somme' WHERE email = '$param_email'";
        $query3 = pg_query($sql_nouvelle_somme);
                header("location: ../connexion/compte.php");
            }
    }
}
?>
