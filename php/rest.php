<?php

class rest
{
	private $method; // POST or GET
	private $host = "apitest.coolsms.co.kr"; // ip address or domain
	private $port = 2000; // port number
	private $path; // target  webapplication ( or  CGI)
	private $version = 1;
	private $errno;
	private $errstr;
	private $timeout;

	private $fp; // file pointer of connected socket
	private $userid;  // coolsms site id
	private $api_key;  // api_key from www.coolsms.co.kr/credentials 
	private $api_secret; //api_secret from www.coolsms.co.kr/credentials
	private $content;
	private $salt;
	
	private $boundary;
	private $options;
	private $boundaryData;

	public $result; // Response string 
	public $resultHeader; // Response string only HTTP Header (without HTTP body)
	public $resultBody; // Response string only HTTP Body (without HTTP header)
	  
	
// constructor
function __construct($userid, $api_key, $api_secret, $errno=null,$errstr=null,$timeout=10)
{
	$this->boundary = md5(uniqid(time()));
	$this->userid = $userid;
	$this->api_key = $api_key;
	$this->api_secret = $api_secret;
	$this->errno =$errno;
	$this->errstr =$errstr;
	$this->timeout =$timeout;
}

function openSocket() 
{
	$this->fp = fsockopen($this->host,$this->port,$this->errno,$this->errstr,$this->timeout);
}
  
function closeSocket() 
{
	fclose($this->fp);
}
  
// put header to connected socket 
function putHeader() 
{
	// if request method is GET, content attached at the end of URL
	$getQuery = $this->method == strtoupper("GET") ? "?".$this->content : "";
	fputs($this->fp, $this->method." ".$this->getPath().$getQuery." HTTP/1.1\r\n"); 
	fputs($this->fp, "Host: ".$this->host."\r\n"); 
	
	//fputs($this->fp, "Content-type: application/x-www-form-urlencoded\r\n");
	
	fputs($this->fp, "Content-type: multipart/form-data; boundary=\"$this->boundary\"\r\n");

	if($this->method == strtoupper("POST"))
	{
		fputs($this->fp, "Content-length: ".strlen($this->boundaryData)."\n\n"); 
		fputs($this->fp, $this->boundaryData);
	}
	fputs($this->fp, "Connection: close\r\n\r\n"); 
}

function setBoundaryData()
{
	$options = $this->options;
	foreach($options as $key => $val){
		if($key == 'image')
		{
			$data .= $this->setBoundaryImgData($data);
		}
		else
		{
			$data .= "--$this->boundary\n";
			$data .= "Content-Disposition: form-data; name=\"".$key . "\"\n\n" . $val . "\n";
		}
	}
	$data .= "--$this->boundary--\n";
	$this->boundaryData = $data;
}

function setBoundaryImgData($data)
{
	if(file_exists($this->options->image))
	{
		$data .= "--$this->boundary\n";
		$data .= "Content-Disposition: form-data; name=\"image\"; filename=\"filename.jpg\"\n";
		$data .= "Content-Type: image/jpeg\n";
		$data .= "Content-Transfer-Encoding: binary\n\n";
		$data .= file_get_contents($this->options->image)."\n";
		return $data;
	}
	return $data;
}

// Gets responsed value(by line) from connected socket
function getResponse() {
   while(!feof($this->fp)) 
   { 
	$result .= fgets($this->fp, 1024); 
   }
   $this->result = $result;
}
  
function getPath()
{
	return "/".$this->version."/".$this->path;
}	

function doSocketProc()
{
	$this->openSocket();
	$this->putHeader();
	$this->getResponse();
	$this->getResult();
	//$this->setResult();
	$this->closeSocket();
}

function send($options) 
{
	$this->addInfos($options);	
	$this->setBoundaryData();
	$this->path = 'send';
	$this->method = 'POST';
	$this->doSocketProc();
}

function sent($options)
{
	$this->addInfos($options);
	$this->setContent($this->options);
	$this->path = 'sent';
	$this->method = 'GET';
	$this->doSocketProc();
}

function cancel($options)
{
	$this->addInfos($options);
	$this->setBoundaryData();
	$this->path = 'cancel';
	$this->method = 'POST';
	$this->setContent($options);
	$this->doSocketProc();
}

function balance()
{
	$options = new stdClass();
	$this->addInfos($options);
	$this->setContent($options);
	$this->path = 'balance';
	$this->method = 'GET';
	$this->doSocketProc();
}

function addInfos($options)
{
	$this->salt = uniqid();
	$options->api_key = $this->api_key;
	$options->salt = $this->salt;
	$options->signature = $this->getSignature();
	$this->options = $options;
}

function setContent($options)
{
	foreach($options as $key => $val)
		$this->content .= $key."=".$val."&";
}

function getSignature()
{
	return hash_hmac('md5', $this->salt.$this->userid, $this->api_secret);
}

// partitioning Header,Body 
function getResult() {
	$tmp = explode("\r\n\r\n",$this->result); // Split a string by (CRLF *2)  
	$this->resultHeader = $tmp[0];
	$this->resultBody = $tmp[1];
	return json_decode($this->resultBody);
}

}
 

?>
