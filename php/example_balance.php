<?php
/* #example_balance
 *
 **  This sample code demonstrate how to check cash & point balance through CoolSMS Rest API PHP v1.0
 **  for more info, visit
 **  www.coolsms.co.kr
 *
 */

include_once "coolsms.php";

/*
 ** api_key and api_secret can be obtained from www.coolsms.co.kr/credentials
 */
$apikey = '#ENTER_YOUR_OWN#';
$apisecret = '#ENTER_YOUR_OWN#';

//initiate rest api sdk object
$rest = new coolsms($apikey, $apisecret);

$result = $rest->balance();
print_r($result);
