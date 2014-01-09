# -*- coding: utf8 -*-
"""
 Copyright (C) 2008-2014 NURIGO
 http://www.coolsms.co.kr
"""
import sys
sys.path.append("..")
import coolsms

def main():
	api_key = 'NCS52A57F48C3D32'
	api_secret = '5AC44E03CE8E7212D9D1AD9091FA9966'
	to = '01000000000,01011111111' # <--- input comma-separated numbers
	sender = '01012345678'
	message = '테스트 메시지'
	cool = coolsms.rest(api_key, api_secret)
	status = cool.send(to,sender,message)
	print status

if __name__ == "__main__":
	main()
	sys.exit(0)
