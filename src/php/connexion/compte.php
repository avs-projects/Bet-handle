<?php

// Call, header incorporation of connected people
include "../include/headerco2.inc.php";

// Call, incorporation of the file manage the connection to the database
include "../include/lien.inc.php";

// Call, incorporation of the file intended to manage the modification of the password
include "../include/nouveau_mdp.inc.php";

// Call, incorporation of the file intended to manage the financial movement
include "../include/argent.inc.php";

// Call, incorporation of the file giving the information on the account
include "../include/compte.inc.php";

?>
<div class="align">
    <div class="grid align__item">
        <div class="register">
            <h2>COMPTE</h2>
            <h3>NOM : <span><?php echo $data_nom; ?></span></h3>
            <h3>PRENOM : <span><?php echo $data_prenom; ?></span></h3>
            <h3>EMAIL : <span><?php echo $data_email; ?></span></h3>
            <h3>PSEUDO : <span><?php echo $data_pseudo; ?></span></h3>
            <h3>CAPITAL : <span><?php echo $data_capital; ?></span></h3>
            <h3>DATE DE NAISSANCE : <span><?php echo $data_date_naissance; ?></span></h3>
        </div>
    </div>
</div>
<div class="align">
    <div class="grid align__item">
        <div class="register">
            <h2>MODIFIER VOTRE CAPITAL FINANCIER</h2>
            <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
                <div class="form-group <?php echo (!empty($ajout_argent_err)) ? 'has-error' : ''; ?>">
                    <label>Ajout d'argent</label>
                    <input type="number" name="ajout_argent" class="form-control" value="<?php echo $ajout_argent; ?>">
                    <span class="help-block"><?php echo $ajout_argent_err; ?></span>
                </div>
                <div class="form-group <?php echo (!empty($retrait_argent_err)) ? 'has-error' : ''; ?><?php echo (!empty($retrait_exces_err)) ? 'has-error' : ''; ?>">
                    <label>Retrait d'argent</label>
                    <input type="number" name="retrait_argent" class="form-control" value="<?php echo $retrait_argent; ?>">
                    <span class="help-block"><?php echo $retrait_argent_err; ?></span>
                    <span class="help-block"><?php echo $retrait_exces_err; ?></span>
                </div>
                <div class="form-group">
                    <input type="submit" id="validation" name="submit2" value="Valider">
                </div>
            </form>
        </div>
    </div>
</div>
<div class="align">
    <div class="grid align__item">
        <div class="register">
            <h2>MODIFIER MOT DE PASSE</h2>
            <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
                <div class="form-group <?php echo (!empty($new_password_err)) ? 'has-error' : ''; ?>">
                    <label>Nouveau mot de passe</label>
                    <input type="password" name="new_password" class="form-control" value="<?php echo $new_password; ?>">
                    <span class="help-block"><?php echo $new_password_err; ?></span>
                </div>
                <div class="form-group <?php echo (!empty($confirm_password_err)) ? 'has-error' : ''; ?>">
                    <label>Confirmez le nouveau mot de passe</label>
                    <input type="password" name="confirm_password" class="form-control">
                    <span class="help-block"><?php echo $confirm_password_err; ?></span>
                </div>
                <div class="form-group">
                    <input type="submit" id="validation" name="submit1" value="Valider">
                </div>
            </form>
        </div>
    </div>
</div>

<?php

// Call, embedding the footer
include "../include/footer.inc.php";

?>
