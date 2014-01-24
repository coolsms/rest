# -*- coding: utf8 -*-
"""
 Copyright (C) 2008-2013 NURIGO
 http://www.coolsms.co.kr
"""

__version__ = "1.0beta"

from hashlib import md5
import httplib,urllib,sys,hmac,mimetypes,base64,array,uuid,json,time

def post_multipart(host, selector, fields, files):
	content_type, body = encode_multipart_formdata(fields, files)
	h = httplib.HTTP(host)
	h.putrequest('POST', selector)
	h.putheader('content-type', content_type)
	h.putheader('content-length', str(len(body)))
	h.endheaders()
	h.send(body)
	errcode, errmsg, headers = h.getreply()
	return errcode, errmsg, h.file.read()

def encode_multipart_formdata(fields, files):
	BOUNDARY = str(uuid.uuid1())
	CRLF = '\r\n'
	L = []
	for key, value in fields.items():
		L.append('--' + BOUNDARY)
		L.append('Content-Disposition: form-data; name="%s"' % key)
		L.append('')
		L.append(value)
	L.append('')
	body = CRLF.join(L)
	for key, value in files.items():
		#L.append('--' + BOUNDARY)
		#L.append('Content-Type: %s' % get_content_type(value['filename']))
		#L.append('Content-Disposition: form-data; name="%s"; filename="%s"' % (key, value['filename']))
		#L.append('')
		#L.append(value['content'])
		body = body + '--' + BOUNDARY + CRLF
		body = body + 'Content-Type: %s' % get_content_type(value['filename']) + CRLF
		body = body + 'Content-Disposition: form-data; name="%s"; filename="%s"' % (key, value['filename']) + CRLF
		body = body + CRLF
		body = body.encode('utf-8') + value['content'] + CRLF
	#L.append('--' + BOUNDARY + '--')
	#L.append('')
	body = body + '--' + BOUNDARY + '--' + CRLF
	body = body + CRLF
	content_type = 'multipart/form-data; boundary=%s' % BOUNDARY
	return content_type, body

def get_content_type(filename):
	return mimetypes.guess_type(filename)[0] or 'application/octet-stream'


class rest:
	host = 'api.coolsms.co.kr'
	port = 80
	version = '1'
	api_key = None
	api_secret = None
	srk = None
	uesr_id = None
	mtype = 'sms'
	imgfile = None
	error_string = None
	test = False

	def __init__(self, api_key, api_secret, srk = None, test = False, version = None):
		self.api_key = api_key
		self.api_secret = api_secret
		self.srk = srk
		self.test = test
		if version:
			self.version = version

	def __get_signature__(self):
		salt = str(uuid.uuid1())
		timestamp = str(int(time.time()))
		data = timestamp + salt
		return timestamp, salt, hmac.new(self.api_secret, data, md5)

	def __set_error__(self, error_str):
		self.error_string = error_str

	def get_type(self):
		return self.mtype

	def get_error(self):
		return self.error_string

	def set_type(self, mtype):
		if mtype.lower() not in ['sms','lms','mms']:
			return False
		self.mtype = mtype.lower()
		return True

	def set_image(self, image):
		self.imgfile = image

	def send(self, to=None, text=None, sender=None, mtype=None, subject=None, image=None, datetime=None, extension=None):
		if type(to) == list:
			to = ','.join(to)

		if mtype:
			if mtype.lower() not in ['sms','lms','mms']:
				self.__set_error__('invalid message type')
				return False
		else:
			mtype = self.get_type()

		timestamp, salt, signature = self.__get_signature__()

		host = self.host + ':' + str(self.port)
		selector = "/%s/send" % self.version
		fields = {'api_key':self.api_key, 'timestamp':timestamp, 'salt':salt, 'signature':signature.hexdigest(), 'type':mtype}
		if self.test:
			fields['mode'] = 'test'
		if self.srk != None:
			fields['srk'] = self.srk
		if to:
			fields['to'] = to
		if text:
			fields['text'] = text
		if sender:
			fields['from'] = sender
		if subject:
			fields['subject'] = subject
		if datetime:
			fields['datetime'] = datetime
		if extension:
			fields['extension'] = extension

		if image == None:
			image = self.imgfile

		if mtype.lower() == 'mms':
			if image == None:
				self.__set_error__('image file path input required')
				return False
			try:
				with open(image, 'rb') as content_file:
					content = content_file.read()
			except IOError as e:
				self.__set_error__("I/O error({0}): {1}".format(e.errno, e.strerror))
				return False
			except:
				self.__set_error__("Unknown error")
				return False
			files = {'image':{'filename':image,'content':content}}
		else:
			files = {}

		status, reason, response = post_multipart(host, selector, fields, files)
		print response
		if status != 200:
			try:
				err = json.loads(response)
			except:
				self.__set_error__("%u:%s" % (status, reason))
				return False
			self.__set_error__("%s:%s" % (err['code'], reason))
			return False
		return json.loads(response)

	def status(self, page = 1, count = 20, mid = None, gid = None, s_rcpt = None, s_start = None, s_end = None):
		timestamp, salt, signature = self.__get_signature__()

		conn = httplib.HTTPConnection(self.host, self.port)
		params = {'api_key':self.api_key, 'timestamp':timestamp, 'salt':salt, 'signature':signature.hexdigest()}
		if page:
			params['page'] = page
		if count:
			params['count'] = count
		if mid:
			params['mid'] = mid
		if gid:
			params['gid'] = gid
		if s_rcpt:
			params['s_rcpt'] = s_rcpt
		if s_start:
			params['s_start'] = s_start
		if s_end:
			params['s_end'] = s_end
		params_str = urllib.urlencode(params)
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("GET", "/%s/sent?" % self.version + params_str, None, headers)
		response = conn.getresponse()
		data = response.read()
		conn.close()
		return json.loads(data)

	def balance(self):
		timestamp, salt, signature = self.__get_signature__()

		conn = httplib.HTTPConnection(self.host, self.port)
		params = urllib.urlencode({'api_key':self.api_key, 'timestamp':timestamp, 'salt':salt, 'signature':signature.hexdigest()})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("GET", "/%s/balance?" % self.version + params, None, headers)
		response = conn.getresponse()
		data = response.read()
		conn.close()
		obj = json.loads(data)
		return int(obj['cash']), int(obj['point'])

	def cancel(self, mid = None, gid = None):
		if mid == None and gid == None:
			return False

		timestamp, salt, signature = self.__get_signature__()

		conn = httplib.HTTPConnection(self.host, self.port)
		if mid:
			params = urllib.urlencode({'api_key':self.api_key, 'timestamp':timestamp, 'salt':salt, 'signature':signature.hexdigest(), 'mid':mid})
		if gid:
			params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'gid':gid})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("POST", "/%s/cancel" % self.version, params, headers)
		response = conn.getresponse()
		data = response.read()
		conn.close()
		if response.status == 200:
			return True
		return False

def main():
	user_id = "test"
	api_key = "NCS52A57F48C3D32"
	api_secret = "5AC44E03CE8E7212D9D1AD9091FA9966"
	mid = "M52A95079DE2B0";
	gid = "G52A95079DDA04";

	r = rest(api_key, api_secret, user_id)
	#r.send()
	r.status(gid=gid)
	#r.balance()
	#r.set_report_url()
	#r.cancel(gid=gid)
	#r.get_report_url()

if __name__ == "__main__":
	main()
	sys.exit(0)
