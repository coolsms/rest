<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<?php
$api_key = 'NCS52A57F48C3D32';
$api_secret = '5AC44E03CE8E7212D9D1AD9091FA9966';
$timestamp = time();
$salt = uniqid();
$data = strval($timestamp) . $salt;
$signature = hash_hmac('md5', $data, $api_secret);
?>
<html lang="ko" xml:lang="ko" xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	</head>
	<body>
		<form method="get" action="https://api.coolsms.co.kr/1/sent" enctype="multipart/form-data">
			API Key: <input type="text" name="api_key" value="<?php echo $api_key ?>" /><br />
			Timestame: <input type="text" name="timestamp" value="<?php echo $timestamp ?>" /><br />
			Salt: <input type="text" name="salt" value="<?php echo $salt ?>" /><br />
			Signature: <input type="text" name="signature" value="<?php echo $signature ?>" /><br />
			List Count : <input type="text" name="count" value="20" /><br />
			Page: <input type="text" name="page" value="1" /><br />
			<input type="submit" value="Submit" />
		</form>
	</body>
</html>
