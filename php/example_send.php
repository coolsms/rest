<?php

include_once "rest.php";

/*
 ** api_key and api_secret can be obtained from www.coolsms.co.kr/credentials
 */
$apikey = 'NCS52A57F48C3D32';
$apisecret = '5AC44E03CE8E7212D9D1AD9091FA9966';

//initiate rest api sdk object
$rest = new rest($apikey, $apisecret);

/*
 ** 5 options(timestamp, to, from, type, text) are mandatory. must be filled
 */
$options->timestamp = (string)time();
$options->to = '01021725487';
$options->from = '01021725487';
$options->type = 'SMS';
$options->text = '안녕하세요 <- This means hello in Korean. ';

// Optional parameters for your own needs
//$options->image = 'desert.jpg'; 			//image for MMS. type must be set as 'MMS'
//$options->refname = '';					//Reference name 
//$options->country = 'KR';					//Korea(KR) Japan(JP) America(USA) China(CN) Default is Korea
//$options->datetime = '20140106153000';	//Format must be(YYYYMMDDHHMISS) 2014 01 06 15 30 00 (2014 Jan 06th 3pm 30 00)
//$options->mid = 'mymsgid01';				//set message id. Server creates automatically if empty
//$options->gid = 'mymsg_group_id01';		//set group id. Server creates automatically if empty
//$options->subject = 'Hello World';		//set msg title for LMS and MMS
//$options->charset = 'euckr';				//For Korean language, set euckr or utf-8

$result = $rest->send($options);			
foreach($result as $key=>$val)
	echo $key . " : " . $val . "<br>";
