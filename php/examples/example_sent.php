<?php
/* #example_sent
 *
 **  This sample code demonstrate how you can check sms result through CoolSMS Rest API PHP v1.0
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

//set necessary options
$options->mid = 'M52CB443257C61';			//message id 
//$options->gid = 'G52CB4432576C8';			//group id
//$options->count = '40';					//result return counts. default is 20
//$options->page = '1';						//page 
//$options->s_crpt = '01012345678';			//search sent result by recipient number
//$options->s_start = '201401070915';		//set search start date  ex) 201401070915
//$options->s_end = '201401071230';			//set search end date	 ex) 201401071230

$result = $rest->sent($options);
foreach($result as $key=>$val)
	echo $key . " : " . $val . "<br>";
