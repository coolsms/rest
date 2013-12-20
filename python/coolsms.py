# -*- coding: utf8 -*-
"""
 Copyright (C) 2008-2013 NURIGO
 http://www.coolsms.co.kr
"""

__version__ = "2.3"

from hashlib import md5
import httplib,urllib,sys,hmac

class rest:
	api_key = None
	api_secret = None

	def __init__(self):
		self.api_key = None
		self.api_secret = None
		self.user_id = None

	def __init__(self, api_key, api_secret, user_id):
		self.api_key = api_key
		self.api_secret = api_secret
		self.user_id = user_id

	def md5(self, str):
		if sys.version_info[1] < 6:
			#- ptyon version 2.6.X under
			hash = md5.new()
		else:
			#- ptyon version 2.6.X upper
			hash = hashlib.md5()
		hash.update(str)
		return string.join(map(lambda v: "%02x" % ord(v), hash.digest()), "")

	def send(self,to,sender,text):
		#to = "01048597580,01048597580"
		#to = "0100000000"
		#sender = "01048597580"
		#text = "Hello CoolSMS"
		salt = "1234"
		data = salt + self.user_id
		signature = hmac.new(self.api_secret, data, md5)

		conn = httplib.HTTPConnection("apitest.coolsms.co.kr", 2000)
		params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'to':to, 'from':sender, 'text':text, 'datetime':'20131212160000'})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("POST", "/1/send", params, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()

	def status(self, mid = None, gid = None):
		if mid == None and gid == None:
			return
		salt = "1234"
		data = salt + self.user_id
		signature = hmac.new(self.api_secret, data, md5)

		conn = httplib.HTTPConnection("apitest.coolsms.co.kr", 2000)
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

	def balance(self):
		salt = "1234"
		data = salt + self.user_id
		signature = hmac.new(self.api_secret, data, md5)

		conn = httplib.HTTPConnection("apitest.coolsms.co.kr", 2000)
		params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest()})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("GET", "/1/balance?" + params, None, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()

	def set_report_url(self):
		salt = "1234"
		data = salt + self.user_id
		signature = hmac.new(self.api_secret, data, md5)

		conn = httplib.HTTPConnection("apitest.coolsms.co.kr", 2000)
		params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest(), 'url':'http://nurigo.net'})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("POST", "/1/report_url", params, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()

	def get_report_url(self):
		salt = "1234"
		data = salt + self.user_id
		signature = hmac.new(self.api_secret, data, md5)

		conn = httplib.HTTPConnection("apitest.coolsms.co.kr", 2000)
		params = urllib.urlencode({'api_key':self.api_key, 'salt':salt, 'signature':signature.hexdigest()})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn.request("GET", "/1/report_url?" + params, None, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()

	def cancel(self, mid = None, gid = None):
		if mid == None and gid == None:
				return

		salt = "1234"
		data = salt + self.user_id
		signature = hmac.new(self.api_secret, data, md5)

		conn = httplib.HTTPConnection("apitest.coolsms.co.kr", 2000)
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
