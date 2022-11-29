<?php

// SQL query to retrieve the information contained in the classification table
$query = 'select * from parieur'; // Change bettor for ranking
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

// Realization of a loop to design the contents of the table
$i = 0;
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