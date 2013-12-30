<?php
include "header.php";
$result = $_SESSION['result'];
$options = new stdClass();
$sentResult = new stdClass();
?>

<table class="table">

<tr>
	<th>Group ID</th>
	<th>Received Number</th>
	<th>Msg ID</th>
	<th>Status</th>
	<th>Result Code</th>
	<th>Result Msg</th>
</tr>

<?php
if(isset($result))
{
	foreach($result as $row=>$val)
	{
		$options->mid = $val->message_id;
		$rest->sent($options);
		$sentResult = $rest->getResult();
		{
			echo "<tr>
					<td>$sentResult->group_id</td>
					<td>$sentResult->recipient_number</td>
					<td>$sentResult->message_id</td>
					<td>$sentResult->status</td>
					<td>$sentResult->result_code</td>
					<td>$sentResult->result_message</td>
				  </tr>";
		}
	}
}
?>

</table>
