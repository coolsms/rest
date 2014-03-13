<?php

class coolsms
{
	private $api_key;
	private	$api_secret;
	private $host = "https://api.coolsms.co.kr";
	private $version = 1;
	private $path;
	private $method;
	private $timestamp;
	private $salt;
	private $result;

	public function __construct($api_key, $api_secret)
	{
		$this->api_key = $api_key;
		$this->api_secret = $api_secret;
	}

	public function curlProcess()
	{
		$ch = curl_init();

		// 1 = POST , 0 = GET
		if($this->method==1)
			$host = sprintf("%s/%s/%s", $this->host, $this->version, $this->path);
		elseif($this->method==0)
			$host = sprintf("%s/%s/%s?%s", $this->host, $this->version, $this->path, $this->content);

		curl_setopt ($ch, CURLOPT_URL, $host);
		curl_setopt ($ch, CURLOPT_SSL_VERIFYPEER, FALSE); 
		curl_setopt ($ch, CURLOPT_SSLVERSION,3); // SSL 버젼 (https 접속시에 필요)
		curl_setopt ($ch, CURLOPT_HEADER, 0); // 헤더 출력 여부
		curl_setopt ($ch, CURLOPT_POST, $this->method); // Post Get 접속 여부
		//Set POST DATA
		if($this->method)
			curl_setopt ($ch, CURLOPT_POSTFIELDS, $this->content); 
		curl_setopt ($ch, CURLOPT_TIMEOUT, 10); // TimeOut 값
		curl_setopt ($ch, CURLOPT_RETURNTRANSFER, 1); // 결과값을 받을것인지
		
		$this->result = json_decode(curl_exec($ch));
		//Handle errors
		if(curl_errno($ch))
		{
			$this->setError('-1', curl_error($ch));
			echo curl_error($ch);
		}
		curl_close ($ch);
	}

	private function setContent($options)
	{
		foreach($options as $key => $val)
			$this->content .= $key."=".urlencode($val)."&";
	}

	private function getSignature()
	{
		return hash_hmac('md5', (string)$this->timestamp.$this->salt, $this->api_secret);
	}

	private function addInfos($options)
	{
		$this->salt = uniqid();
		$this->timestamp = (string)time();

		$options->User_Agent = "PHP";
		$options->salt = $this->salt;
		$options->timestamp = $this->timestamp;
		$options->api_key = $this->api_key;
		$options->signature = $this->getSignature();

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



}
?>

