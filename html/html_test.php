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
		<form method="post" action="http://apitest.coolsms.co.kr:2000/1/send" enctype="multipart/form-data">
			API Key: <input type="text" name="api_key" value="<?php echo $api_key ?>" /><br />
			Timestame: <input type="text" name="timestamp" value="<?php echo $timestamp ?>" /><br />
			Salt: <input type="text" name="salt" value="<?php echo $salt ?>" /><br />
			Signature: <input type="text" name="signature" value="<?php echo $signature ?>" /><br />
			To: <input type="text" name="to" value="01000000000" /><br />
			From: <input type="text" name="from" value="01000000000" /><br />
			Subject: <input type="text" name="subject" value="TEST" /><br />
			Text: <textarea name="text">HELLO COOLSMS~!</textarea><br />
			Type: <select name="type"><option value="SMS">SMS</option><option value="LMS">LMS</option><option value="MMS">MMS</option></select><br />
			Image: <input type="file" name="image" /><br />
			<input type="submit" value="Submit" />
		</form>
	</body>
</html>
