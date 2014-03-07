<?php
/* #example_send
 *
 **  This sample code demonstrate how you can send sms through CoolSMS Rest API PHP v1.0
 **  for more info, visit
 **  www.coolsms.co.kr
 *
 */

include_once "coolsms.php";


/*
 **  api_key and api_secret can be obtained from www.coolsms.co.kr/credentials
 */
$apikey = '#ENTER_YOUR_OWN#';
$apisecret = '#ENTER_YOUR_OWN#';


//initiate rest api sdk object
$rest = new coolsms($apikey, $apisecret);


/*
 **  5 options(timestamp, to, from, type, text) are mandatory. must be filled
 */
$options->timestamp = (string)time();
$options->to = '#받는사람 번호';
$options->from = '#보내는 사람 번호';
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
print_r($result);
