<?php
/* #example_send
 *
 **  This sample code demonstrate how to send sms through CoolSMS Rest API PHP v1.0
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
$options->to = '01000000000';
$options->from = '01000000000';
$options->type = 'SMS';
$options->text = '안녕하세요. 10000건을 20초안에 발송하는 빠르고 저렴한 CoolSMS의 테스팅 문자입니다. ';
$options->app_version = 'test app 1.2';  //application name and version	 

// Optional parameters for your own needs
//$options->image = 'desert.jpg'; 			//image for MMS. type must be set as 'MMS'
//$options->refname = '';					//Reference name 
//$options->country = 'KR';					//Korea(KR) Japan(JP) America(USA) China(CN) Default is Korea
//$options->datetime = '20140106153000';	//Format must be(YYYYMMDDHHMISS) 2014 01 06 15 30 00 (2014 Jan 06th 3pm 30 00)
//$options->mid = 'mymsgid01';				//set message id. Server creates automatically if empty
//$options->gid = 'mymsg_group_id01';		//set group id. Server creates automatically if empty
//$options->subject = 'Hello World';		//set msg title for LMS and MMS
//$options->charset = 'euckr';				//For Korean language, set euckr or utf-8

/**
 * added REST API v1.5
 **/
//$options->os_platform = 'Windows 7';		//Operating System. SDK creates automatically if empty 
//$options->dev_lang = 'PHP 5.3.3';			//Application development language. SDK creates automatically if empty 
//$options->sdk_version = 'PHP SDK 1.1';	//SDK version being used. SDK creates automatically if empty 

$result = $rest->send($options);			
print_r($result);
