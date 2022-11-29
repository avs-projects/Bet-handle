<?php

// Call, incorporation of the file manage the connection to the database
include "../include/lien.inc.php";

// Call, embed file handle user registration
include "../include/inscription.inc.php";

// Call, incorporation of header for registration
include "../include/headerform.inc.php";

?>
<div class="grid">
    <div class="register">
        <h2>Inscription</h2>
        <p class='infos'>Veuillez entrer vos informations.</p>
        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
            <div class="form__field <?php echo (!empty($email_err)) ? 'has-error' : ''; ?>">
                <label>Email</label>
                <input type="email" name="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" class="form-control" value="<?php echo $email; ?>">
                <span class="help-block"><?php echo $email_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($prenom_err)) ? 'has-error' : ''; ?>">
                <label>Prenom</label>
                <input type="text" name="prenom" class="form-control" value="<?php echo $prenom; ?>">
                <span class="help-block"><?php echo $prenom_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($nom_err)) ? 'has-error' : ''; ?>">
                <label>Nom</label>
                <input type="text" name="nom" class="form-control" value="<?php echo $nom; ?>">
                <span class="help-block"><?php echo $nom_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($pseudo_err)) ? 'has-error' : ''; ?>">
                <label>Pseudo</label>
                <input type="text" name="pseudo" class="form-control" value="<?php echo $pseudo; ?>">
                <span class="help-block"><?php echo $pseudo_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($capital_err)) ? 'has-error' : ''; ?>">
                <label>Capital financier</label>
                <input type="number" name="capital" class="form-control" value="<?php echo $capital; ?>">
                <span class="help-block"><?php echo $capital_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($date_naissance_err)) ? 'has-error' : ''; ?><?php echo (!empty($date_naissance_age_err)) ? 'has-error' : ''; ?>">
                <label>Date de naissance</label>
                <input type="date" name="date_naissance" class="form-control" value="<?php echo $date_naissance; ?>">
                <span class="help-block"><?php echo $date_naissance_err; ?><?php echo $date_naissance_age_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($password_err)) ? 'has-error' : ''; ?>">
                <label>Mot de passe</label>
                <input type="password" name="password" class="form-control" value="<?php echo $password; ?>">
                <span class="help-block"><?php echo $password_err; ?></span>
            </div>
            <div class="form__field <?php echo (!empty($passconf_err)) ? 'has-error' : ''; ?>">
                <label>Confirmer le mot de passe</label>
                <input type="password" name="passconf" class="form-control" value="<?php echo $passconf; ?>">
                <span class="help-block"><?php echo $passconf_err; ?></span>
            </div>
            <div class="form__field">
                <input type="submit" id="validation" value="Valider">
            </div>
            <p>Vous avez déja un compte ? <a id="lien" href="./connexion.php">Connexion</a>.</p>
        </form>
    </div>
</div>

<?php

// Call, embedding footer
include "../include/footer2.inc.php";

?>
