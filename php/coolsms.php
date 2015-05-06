<?php

/**
 *
 *   Copyright (C) 2008-2015 NURIGO
 *   http://www.coolsms.co.kr
 *
 **/

class coolsms
{
	private $api_key;
	private	$api_secret;
	private $host = "http://api.coolsms.co.kr/sms";
	private $version = "1.5";
	private $sdk_version = "1.1";
	private $path;
	private $method;
	private $timestamp;
	private $salt;
	private $result;
	private $basecamp;
	private $user_agent;

	public function __construct($api_key, $api_secret, $basecamp=false)
	{
		if($basecamp)
		{
			$this->coolsms_user = $api_key;
			$this->basecamp = true;
		}
		else
			$this->api_key = $api_key;

		$this->api_secret = $api_secret;
		$this->user_agent = $_SERVER['HTTP_USER_AGENT'];
	}

	public function curlProcess()
	{
		$ch = curl_init(); 
		// 1 = POST , 0 = GET
		if($this->method==1)
			$host = sprintf("%s/%s/%s", $this->host, $this->version, $this->path);
		elseif($this->method==0)
			$host = sprintf("%s/%s/%s?%s", $this->host, $this->version, $this->path, $this->content);

		curl_setopt($ch, CURLOPT_URL, $host);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE); 
		curl_setopt($ch, CURLOPT_SSLVERSION,3); // SSL 버젼 (https 접속시에 필요)
		curl_setopt($ch, CURLOPT_HEADER, 0); // 헤더 출력 여부
		curl_setopt($ch, CURLOPT_POST, $this->method); // Post Get 접속 여부
		//Set POST DATA
		if($this->method)
		{
			curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type:multipart/form-data"));
			curl_setopt($ch, CURLOPT_POSTFIELDS, $this->content); 
		}
		curl_setopt($ch, CURLOPT_TIMEOUT, 10); // TimeOut 값
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); // 결과값을 받을것인지
		
		$this->result = json_decode(curl_exec($ch));
		//Handle errors
		if(curl_errno($ch))
		{
			//$this->setError('-1', curl_error($ch));
			echo "CURL_ERROR : ".curl_error($ch);
		}
		curl_close ($ch);
	}

	private function setContent($options)
	{
		if($this->method)
		{
			$this->content = array();
			foreach($options as $key => $val)
			{
				if($key != "image")
					$this->content[$key] = sprintf("\0%s", $val);
				else
					$this->content[$key] = "@".realpath("./$val");
			}
		}
		else
		{
			foreach($options as $key => $val)
				$this->content .= $key."=".urlencode($val)."&";
		}
	}

	private function getSignature()
	{
		return hash_hmac('md5', (string)$this->timestamp.$this->salt, $this->api_secret);
	}

	private function addInfos($options)
	{
		$this->salt = uniqid();
		$this->timestamp = (string)time();
		if(!$options->User_Agent) $options->User_Agent = sprintf("PHP REST API %s", $this>version);
		if(!$options->os_platform) $options->os_platform = $this->getOS();
		if(!$options->dev_lang) $options->dev_lang = sprintf("PHP %s", phpversion());
		if(!$options->sdk_version) $options->sdk_version = sprintf("PHP SDK %s", $this->sdk_version);

		$options->salt = $this->salt;
		$options->timestamp = $this->timestamp;
		if($this->basecamp)
			$options->coolsms_user = $this->coolsms_user;
		else
			$options->api_key = $this->api_key;
		$options->signature = $this->getSignature();
		print_r($options);

		$this->setContent($options);
		$this->curlProcess();
	}

	/**
	 * $method 
	 * GET = 0, POST, 1
	 * $path
	 * 'send' 'sent' 'cancel' 'balance' 
	 */
	private function setMethod($path, $method)
	{
		$this->path = $path;
		$this->method = $method;
	}

	private function getResult()
	{
		return $this->result;
	}

	/**
	 *  @POST send method
	 *	@param $options (options must contain api_key, salt, signature, to, from, text)
	 *	@type, image, refname, country, datetime, mid, gid, subject, charset (optional)
	 *	@returns an object(recipient_number, group_id, message_id, result_code, result_message)
	 */
	public function send($options) 
	{
		$this->setMethod('send', 1);
		$this->addInfos($options);	
		return $this->getResult();
	}
	
	/**
	 * 	@GET sent method
	 * 	@param $options (options can be optional)
	 * 	@count,  page, s_rcpt, s_start, s_end, mid, gid (optional)
	 * 	@returns an object(total count, list_count, page, data['type', 'accepted_time', 'recipient_number', 'group_id', 'message_id', 'status', 'result_code', 'result_message', 'sent_time', 'text'])
	 */
	public function sent($options=null) 
	{
		if(!$options)
			$options = new stdClass();
		$this->setMethod('sent', 0);
		$this->addInfos($options);	
		return $this->getResult();
	}

	/**
	 * 	@POST cancel method
	 * 	@options must contain api_key, salt, signature
	 * 	@mid, gid (either one must be entered.)
	 */
	public function cancel($options) 
	{
		$this->setMethod('cancel', 1);
		$this->addInfos($options);	
		return $this->getResult();
	}

	
	/**
	 * 	@GET balance method
	 * 	@options must contain api_key, salt, signature
	 * 	@return an object(cash, point)
	 */
	public function balance() 
	{
		$this->setMethod('balance', 0);
		$this->addInfos($options = new stdClass());	
		return $this->getResult();
	}

	/**
	 * 	@GET status method
	 * 	@options must contain api_key, salt, signature
	 * 	@return an object(registdate, sms_average, sms_sk_average, sms_kt_average, sms_lg_average, mms_average, mms_sk_average, mms_kt_average, mms_lg_average)
	 * 	this method is made for Coolsms inc. internal use
	 */
	public function status($options) 
	{
		$this->setMethod('status', 0);
		$this->addInfos($options);	
		return $this->getResult();
	}

	private function objectToArray($d) {
		if (is_object($d)) {
			// Gets the properties of the given object
			// with get_object_vars function
			$d = get_object_vars($d);
		}
 
		if (is_array($d)) 
		{
			/*
			* Return array converted to object
			* Using __FUNCTION__ (Magic constant)
			* for recursive call
			*/
			return array_map(__FUNCTION__, $d);
		}
		else 
		{
			// Return array
			return $d;
		}
	}
	function getOS() { 
		$user_agent = $this->user_agent;
		$os_platform    =   "Unknown OS Platform";
		$os_array       =   array(
								'/windows nt 10/i'     =>  'Windows 10',
								'/windows nt 6.3/i'     =>  'Windows 8.1',
								'/windows nt 6.2/i'     =>  'Windows 8',
								'/windows nt 6.1/i'     =>  'Windows 7',
								'/windows nt 6.0/i'     =>  'Windows Vista',
								'/windows nt 5.2/i'     =>  'Windows Server 2003/XP x64',
								'/windows nt 5.1/i'     =>  'Windows XP',
								'/windows xp/i'         =>  'Windows XP',
								'/windows nt 5.0/i'     =>  'Windows 2000',
								'/windows me/i'         =>  'Windows ME',
								'/win98/i'              =>  'Windows 98',
								'/win95/i'              =>  'Windows 95',
								'/win16/i'              =>  'Windows 3.11',
								'/macintosh|mac os x/i' =>  'Mac OS X',
								'/mac_powerpc/i'        =>  'Mac OS 9',
								'/linux/i'              =>  'Linux',
								'/ubuntu/i'             =>  'Ubuntu',
								'/iphone/i'             =>  'iPhone',
								'/ipod/i'               =>  'iPod',
								'/ipad/i'               =>  'iPad',
								'/android/i'            =>  'Android',
								'/blackberry/i'         =>  'BlackBerry',
								'/webos/i'              =>  'Mobile'
							);

		foreach ($os_array as $regex => $value) { 
			if (preg_match($regex, $user_agent)) {
				$os_platform    =   $value;
			}
		}   
		return $os_platform;
	}

	function getBrowser() {
		$user_agent = $this->user_agent;
		$browser        =   "Unknown Browser";
		$browser_array  =   array(
								'/msie/i'       =>  'Internet Explorer',
								'/firefox/i'    =>  'Firefox',
								'/safari/i'     =>  'Safari',
								'/chrome/i'     =>  'Chrome',
								'/opera/i'      =>  'Opera',
								'/netscape/i'   =>  'Netscape',
								'/maxthon/i'    =>  'Maxthon',
								'/konqueror/i'  =>  'Konqueror',
								'/mobile/i'     =>  'Handheld Browser'
							);
		foreach ($browser_array as $regex => $value) { 
			if (preg_match($regex, $user_agent)) {
				$browser    =   $value;
			}
		}
		return $browser;
	}
}
?>
