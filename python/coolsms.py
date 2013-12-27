# -*- coding: utf8 -*-
"""
 Copyright (C) 2008-2013 NURIGO
 http://www.coolsms.co.kr
"""

__version__ = "1.0beta"

from hashlib import md5
import httplib,urllib,sys,hmac,mimetypes,base64,array,uuid,json

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
	BOUNDARY = '----------ThIs_Is_tHe_bouNdaRY_$'
	CRLF = '\r\n'
	L = []
	for key, value in fields.items():
		L.append('--' + BOUNDARY)
		L.append('Content-Disposition: form-data; name="%s"' % key)
		L.append('')
		L.append(value)
	body = CRLF.join(L)
	body = body + CRLF
	for key, value in files.items():
		body = body + '--' + BOUNDARY + CRLF
		body = body + 'Content-Type: %s' % get_content_type(value['filename']) + CRLF
		body = body + 'Content-Disposition: form-data; name="%s"; filename="%s"' % (key, value['filename']) + CRLF
		body = body + CRLF
		body = body.encode('utf-8') + value['content'] + CRLF
	body = body + '--' + BOUNDARY + '--' + CRLF + CRLF
	content_type = 'multipart/form-data; boundary=%s' % BOUNDARY
	return content_type, body

def get_content_type(filename):
	return mimetypes.guess_type(filename)[0] or 'application/octet-stream'


class rest:
	host = 'apitest.coolsms.co.kr'
	port = 2000
	api_key = None
	api_secret = None
	uesr_id = None
	mtype = None
	imgfile = None

	def __init__(self):
		self.api_key = None
		self.api_secret = None
		self.user_id = None

	def __init__(self, api_key, api_secret, user_id):
		self.api_key = api_key
		self.api_secret = api_secret
		self.user_id = user_id

	def __get_signature__(self):
		salt = str(uuid.uuid1())
		data = salt + self.user_id
		return salt, data, hmac.new(self.api_secret, data, md5)

	def md5(self, str):
		if sys.version_info[1] < 6:
			#- ptyon version 2.6.X under
			hash = md5.new()
		else:
			#- ptyon version 2.6.X upper
			hash = hashlib.md5()
		hash.update(str)
		return string.join(map(lambda v: "%02x" % ord(v), hash.digest()), "")

	def set_type(self, mtype):
		self.mtype = mtype.lower()

	def set_image(self, image):
		self.imgfile = image

	def send(self,to='',sender='',text='',subject=''):
		if to == '':
			return 'No phone number input'
		if text == '':
			return 'No message input'

		if type(to) == list:
			to = ','.join(to)

		salt, data, signature = self.__get_signature__()

		host = self.host + ':' + str(self.port)
		selector = "/1/send"
		fields = {'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'type':self.mtype, 'to':to, 'text':text}
		if sender:
			fields['from'] = sender
		if subject:
			fields['subject'] = subject
		if self.mtype == 'mms':
			with open(self.imgfile, 'rb') as content_file:
				content = content_file.read()
			files = {'image':{'filename':self.imgfile,'content':content}}
		else:
			files = {}

		status, reason, response = post_multipart(host, selector, fields, files)
		return json.loads(response)

	def status(self, mid = None, gid = None):
		if mid == None and gid == None:
			return

		salt, data, signature = self.__get_signature__()

		conn = httplib.HTTPConnection(self.host, self.port)
		if mid:
			params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'mid':mid})
		if gid:
			params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'gid':gid})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("GET", "/1/sent?" + params, None, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()
		return json.loads(data)

	def balance(self):
		salt, data, signature = self.__get_signature__()

		conn = httplib.HTTPConnection(self.host, self.port)
		params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest()})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("GET", "/1/balance?" + params, None, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()
		return json.loads(data)

	def cancel(self, mid = None, gid = None):
		if mid == None and gid == None:
				return

		salt, data, signature = self.__get_signature__()

		conn = httplib.HTTPConnection(self.host, self.port)
		if mid:
			params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'mid':mid})
		if gid:
			params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'gid':gid})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("POST", "/1/cancel", params, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()

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
