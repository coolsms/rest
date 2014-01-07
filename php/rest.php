<?php

class rest
{
	private $method; 		// POST or GET
	private $host = "apitest.coolsms.co.kr"; // ip address or domain
	private $port = 2000; 	// port number
	private $path; 			// target  webapplication ( or  CGI)
	private $version = 1;
	private $errno;
	private $errstr;
	private $timeout;

	private $fp; 			// file pointer of connected socket
	private $timestamp;
	private $api_key;  		// api_key from www.coolsms.co.kr/credentials 
	private $api_secret; 	// api_secret from www.coolsms.co.kr/credentials
	private $content;		// content that add to GET method header
	private $salt;			// salt for authentication purpose
	
	private $boundary;		// boundary name for multipart
	private $options;		
	private $boundaryData;	

	public $resultHeader; 	// Response string only HTTP Header (without HTTP body)
	public $resultBody; 	// Response string only HTTP Body (without HTTP header)
	  
	
// constructor
public function __construct($api_key, $api_secret, $errno=null,$errstr=null,$timeout=10)
{
	$this->boundary = md5(uniqid(time()));
	$this->api_key = $api_key;
	$this->api_secret = $api_secret;
	$this->errno =$errno;
	$this->errstr =$errstr;
	$this->timeout =$timeout;
}

private function openSocket() 
{
	$this->fp = fsockopen($this->host,$this->port,$this->errno,$this->errstr,$this->timeout);
}
  
private function closeSocket() 
{
	fclose($this->fp);
}
  
// put header to connected socket 
private function putHeader() 
{
	// if request method is GET, content will be attached at the end of URL
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

private function setBoundaryData()
{
	$options = $this->options;
	foreach($options as $key => $val){
		if($key == 'image')
			$data .= $this->setBoundaryImgData($data);
		else
		{
			$data .= "--$this->boundary\n";
			$data .= "Content-Disposition: form-data; name=\"".$key . "\"\n\n" . $val . "\n";
		}
	}
	$data .= "--$this->boundary--\n";
	$this->boundaryData = $data;
}

private function setBoundaryImgData($data)
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

/**
 * 	@get responded value(by line) from connected socket
 * 	@partitioning Header,Body 
 */
private function setResult() 
{
    while(!feof($this->fp)) 
    {  
	$result .= fgets($this->fp, 1024); 
    }
    $tmp = explode("\r\n\r\n",$result); 
	$this->resultHeader = json_decode($tmp[0]);
	$this->resultBody = json_decode($tmp[1]);
}

private function getResult() 
{
	return $this->resultBody; 
}
  
private function getPath()
{
	return "/".$this->version."/".$this->path;
}	

private function doSocketProc()
{
	$this->openSocket();
	$this->putHeader();
	$this->setResult();
	$this->closeSocket();
}

private function setMethod($path, $method)
{
	$this->path = $path;
	$this->method = $method;
}

private function addInfos($options)
{
	$this->salt = uniqid();
	$this->timestamp = (string)time();
	$options->timestamp = $this->timestamp;
	$options->api_key = $this->api_key;
	$options->salt = $this->salt;
	$options->signature = $this->getSignature();
	//print_r($options);
	$this->options = $options;
}

private function setContent($options)
{
	foreach($options as $key => $val)
		$this->content .= $key."=".$val."&";
}

private function getSignature()
{
	
	return hash_hmac('md5', (string)$this->timestamp.$this->salt, $this->api_secret);
}

/**
 *  @POST send method
 *	@options must contain api_key, salt, signature, to, from, text
 *	@type, image, refname, country, datetime, mid, gid, subject, charset (optional)
 *	@returns an object(recipient_number, group_id, message_id, result_code, result_message)
 */
public function send($options) 
{
	$this->addInfos($options);	
	$this->setBoundaryData();
	$this->setMethod('send', 'POST');
	$this->doSocketProc();
	return $this->getResult();
}

/**
 * 	@GET sent method
 * 	@options must contain api_key, salt, signature
 * 	@mid, gid (optional)
 * 	@returns an object(recipient_number, group_id, message_id, status, result_code, result_message)
 */
public function sent($options)
{
	$this->addInfos($options);
	$this->setContent($this->options);
	$this->setMethod('sent', 'GET');
	$this->doSocketProc();
	return $this->getResult();
}

/**
 * 	@POST cancel method
 * 	@options must contain api_key, salt, signature
 * 	@mid, gid (either one must be entered.)
 */
public function cancel($options)
{
	$this->addInfos($options);
	$this->setBoundaryData();
	$this->setMethod('cancel', 'POST');
	$this->setContent($options);
	$this->doSocketProc();
	return $this->getResult();
	//return nothing
}

/**
 * 	@GET balance method
 * 	@options must contain api_key, salt, signature
 * 	@return an object(cash, point)
 */
public function balance()
{
	$options = new stdClass();
	$this->addInfos($options);
	$this->setContent($options);
	$this->setMethod('balance', 'GET');
	$this->doSocketProc();
	return $this->getResult();
}

}
 

?>
