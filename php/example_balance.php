<?php

include_once "rest.php";

/*
 ** api_key and api_secret can be obtained from www.coolsms.co.kr/credentials
 */
$apikey = 'ENTER_YOUR_OWN';
$apisecret = 'ENTER_YOUR_OWN';

//initiate rest api sdk object
$rest = new rest($apikey, $apisecret);

$result = $rest->balance();
foreach($result as $key=>$val)
	echo $key . " : " . $val . "<br>";
