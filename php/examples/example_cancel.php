<?php
/* #example_cancel
 *
 **  This sample code demonstrate how you can cancel reserved sms through CoolSMS Rest API PHP v1.0
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
$rest = new rest($apikey, $apisecret);

// Either mid or gid must be entered. 
$options->mid = 'M52CB443257C61';			//message id. 
//$options->gid = 'G52CB4432576C8';			//group id. 

$rest->cancel($options);					//cancel does not return any.
