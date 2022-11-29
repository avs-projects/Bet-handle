<?php

// SQL query to retrieve information about the bets made by the connected player
$param_id = $_SESSION["id"];
$query = "select * from pari WHERE id_parieur ='$param_id'"; // Instead of bettor put paris to have the list of the player's bets
$result = pg_query($query);

// Making a loop to design the table caption
$i = 0;
echo '<table id="t01"><tr>';
while ($i < pg_num_fields($result))
{
	$legende = pg_field_name($result, $i);
	echo '<td>' . $legende . '</td>';
	$i = $i + 1;
}
echo '</tr>';
$i = 0;

// Realization of a loop to design the contents of the table
while ($row = pg_fetch_row($result)) 
{
	echo '<tr>';
	$count = count($row);
	$y = 0;
	while ($y < $count)
	{
		$c_row = current($row);
		echo '<td>' . $c_row . '</td>';
		next($row);
		$y = $y + 1;
	}
	echo '</tr>';
	$i = $i + 1;
}

// Release of results
pg_free_result($result);

// Affichage du tableau 
echo '</table>';

// SQL query to retrieve information about the bets made by the connected player
$param_id = $_SESSION["id"];
$query = "select equipe_nom, classement_victoire, classement_defaite, classement_point from classement INNER JOIN equipe ON classement.classement_id = equipe.classement_id ORDER BY classement_point DESC";
$result = pg_query($query);

// Making a loop to design the table caption
$i = 0;
echo '<table id="t01"><tr>';
while ($i < pg_num_fields($result))
{
	$legende = pg_field_name($result, $i);
	echo '<td>' . $legende . '</td>';
	$i = $i + 1;
}
echo '</tr>';
$i = 0;

// Realization of a loop to design the contents of the table
while ($row = pg_fetch_row($result)) 
{
	echo '<tr>';
	$count = count($row);
	$y = 0;
	while ($y < $count)
	{
		$c_row = current($row);
		echo '<td>' . $c_row . '</td>';
		next($row);
		$y = $y + 1;
	}
	echo '</tr>';
	$i = $i + 1;
}

// Release of results
pg_free_result($result);

// Table display
echo '</table>';
?>
