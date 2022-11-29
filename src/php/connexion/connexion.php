<?php
// Session initialization
session_start();
 
// Call, incorporation of the file giving managing the session
include "../include/session.inc.php";
 
// Call, incorporation of the file manage the connection to the database
include "../include/lien.inc.php";

// Call, incorporation of the header intended for the connection
include "../include/headerform.inc.php";

// Call, embed file handle user login
include "../include/connexion.inc.php";

?>

<div class="grid">
    <div class="register">
        <h2>Connexion</h2>
        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
            <div class="form__field <?php echo (!empty($email_err)) ? 'has-error' : ''; ?>">
                <input type="email" name="email" class="form-control" value="<?php echo $email; ?>" placeholder="Email">
                <span class="help-block"><?php echo $email_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($password_err)) ? 'has-error' : ''; ?>">
                <input type="password" name="password" class="form-control" placeholder="Mot de passe">
                <span class="help-block"><?php echo $password_err; ?></span>
            </div>
            <div class="form__field">
                <input type="submit" id="validation" value="Connexion">
            </div>
            <p>Vous n'avez pas de compte ? <a id="lien" href="../connexion/inscription.php">Inscription</a>.</p>
        </form>
    </div>
</div>

<?php

// Call, embedding footer
include "../include/footer2.inc.php";

?>
