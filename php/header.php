<?php
session_start();
include_once "rest.php";

$submit=filter_input(INPUT_POST, 'submit');

if(isset($submit) && $submit == 'SAVE'){
	$userid = filter_input(INPUT_POST, 'userid');
	$apikey = filter_input(INPUT_POST, 'api_key');
	$apisecret = filter_input(INPUT_POST, 'api_secret');

	if($userid != $_SESSION['userid'])
	{
		$_SESSION['userid'] = $userid;
		$_SESSION['apikey'] = $apikey;
		$_SESSION['apisecret'] = $apisecret;
		unset($_SESSION['result']);
	}
}
	$userid = $_SESSION['userid'];
	$apikey = $_SESSION['apikey'];
	$apisecret = $_SESSION['apisecret'];
	$rest = new rest($userid, $apikey, $apisecret);
	$options = new stdClass();

?>

<!DOCTYPE html>
<html>
  <head>
    <title>CoolSMS Rest API</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
  </head>
  <body>

<ul class="nav nav-tabs">
  <li><a href="index.php">SETUP</a></li>
  <li><a href="send.php">SEND</a></li>
  <li><a href="result.php">RESULT</a></li>
</ul>

