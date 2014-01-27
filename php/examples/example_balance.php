<?php
/* #example_balance
 *
 **  This sample code demonstrate how you can check cash & point balance through CoolSMS Rest API PHP v1.0
 **  for more info, visit
 **  www.coolsms.co.kr
 *
 */

include_once "rest.php";

/*
 ** api_key and api_secret can be obtained from www.coolsms.co.kr/credentials
 */
$apikey = 'NCS52A57F48C3D32';
$apisecret = '5AC44E03CE8E7212D9D1AD9091FA9966';

//initiate rest api sdk object
$rest = new rest($apikey, $apisecret);

$result = $rest->balance();
foreach($result as $key=>$val)
	echo $key . " : " . $val . "<br>";
