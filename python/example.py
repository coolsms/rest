# -*- coding: utf8 -*-
"""
 Copyright (C) 2008-2013 NURIGO
 http://www.coolsms.co.kr
"""

__version__ = "1.0beta"

import gui
import coolsms

class smsui:
	mtype = 'sms'
	imgfile = None

	def load(self, evt):
		return

	def get_credential(self):
		api_key = str(mywin['notebook']['tab_setup']['api_key'].value)
		api_secret = str(mywin['notebook']['tab_setup']['api_secret'].value)
		user_id = str(mywin['notebook']['tab_setup']['user_id'].value)
		return api_key, api_secret, user_id

	def send_message(self, evt):
		api_key, api_secret, user_id = self.get_credential()

		to = mywin['notebook']['tab_message']['to'].value
		sender = mywin['notebook']['tab_message']['from'].value
		message = mywin['notebook']['tab_message']['message'].value.encode('utf-8')
		#print message
		#message = "TEST"
		r = coolsms.rest(api_key, api_secret, user_id)
		print 'api key : ' + api_key
		print 'api secret : ' + api_secret
		print 'user id : ' + user_id
		print 'to : ' + to
		print 'sender : ' + sender
		print 'message : ' + message
		r.set_type(self.mtype)
		if self.mtype == 'mms':
			r.set_image(self.imgfile)
		status = r.send(to,sender,message)
		print status['result_message']
		mywin['notebook']['tab_list']['listview'].items[status['message_id']] = {
			'col_mid':status['message_id']
			,'col_callno':status['called_number']
			,'col_status':status['result_code'] + status['result_message']
		}
		mywin['statusbar'].text = status['result_message'].encode('utf-8')
		#r.status(gid=gid)
		#r.balance()
		#r.set_report_url()
		#r.cancel(gid=gid)
		#r.get_report_url()
		gui.alert("Sent!")


	def close_window(self, evt):
		exit()

	def choose_type(self, evt):
		if evt.target.name == 'sms':
			self.mtype = 'sms'
		if evt.target.name == 'lms':
			self.mtype = 'lms'
		if evt.target.name == 'mms':
			self.mtype = 'mms'

	def choose_image(self, evt):
		self.imgfile = gui.open_file("select the file")
		print self.imgfile
		mywin['filename'].value = self.imgfile

	def refresh_status(self, evt):
		api_key, api_secret, user_id = self.get_credential()
		r = coolsms.rest(api_key, api_secret, user_id)
		for item in mywin['notebook']['tab_list']['listview'].items:
			print item
			print item['col_mid']
			status = r.status(item['col_mid'])
			print status
			item['col_status'] = status['result_code']


ui = smsui()

gui.Window(name='mywin', title='COOLSMS Messaging'
		, resizable=True, height='420px'
		, left='180', top='24'
		, width='396px', bgcolor='#E0E0E0')

# menu
gui.MenuBar(name='menubar', fgcolor='#000000', parent='mywin')
gui.Menu(label='File', name='menu', fgcolor='#000000', parent='mywin.menubar')
gui.MenuItem(label='Quit', help='quit program', name='menu_quit', parent='mywin.menubar.menu')

# tab panel
gui.Notebook(name='notebook', height='211', left='10', top='10', width='355', parent='mywin', selection=0, )
gui.TabPanel(name='tab_setup', parent='mywin.notebook', selected=True, text='Setup')
gui.TabPanel(name='tab_message', parent='mywin.notebook', selected=True, text='Message')
gui.TabPanel(name='tab_list', parent='mywin.notebook', selected=True, text='List')

#### setup #####
# user_id
gui.Label(name='label_userid', left='10', top='22', parent='mywin.notebook.tab_setup', text='User Id')
gui.TextBox(name='user_id', left='80', top='22', width='150', parent='mywin.notebook.tab_setup', value='test')

# api_key
gui.Label(name='label_api_key', left='10', top='50', parent='mywin.notebook.tab_setup', text='API Key')
gui.TextBox(name='api_key', left='80', top='50', width='150', parent='mywin.notebook.tab_setup', value='NCS52A57F48C3D32')

# api_secret
gui.Label(name='label_secret', left='10', top='84', parent='mywin.notebook.tab_setup', text='API Secret')
gui.TextBox(name='api_secret', left='80', top='84', width='280', parent='mywin.notebook.tab_setup', value='5AC44E03CE8E7212D9D1AD9091FA9966')

#### message #####
# type
gui.Label(name='label_type', left='10', top='22', parent='mywin.notebook.tab_message', text='Type')
gui.RadioButton(label='SMS', name='sms', left='80', top='22', parent='mywin.notebook.tab_message', value=True, onclick=ui.choose_type)
gui.RadioButton(label='LMS', name='lms', left='130', top='22', parent='mywin.notebook.tab_message', onclick=ui.choose_type)
gui.RadioButton(label='MMS', name='mms', left='180', top='22', parent='mywin.notebook.tab_message', onclick=ui.choose_type)

# to
gui.Label(name='label_to', left='10', top='50', parent='mywin.notebook.tab_message', text='To')
gui.TextBox(label='To ', name='to', left='80', top='50', width='150', parent='mywin.notebook.tab_message', value='01000000000')

# from
gui.Label(name='label_from', left='10', top='84', parent='mywin.notebook.tab_message', text='From')
gui.TextBox(name='from', left='80', top='84', width='150', parent='mywin.notebook.tab_message', value='01000000000')

# subject
gui.Label(name='label_subject', left='10', top='116', parent='mywin.notebook.tab_message', text='Subject')
gui.TextBox(name='subject', left='80', top='116', width='150', parent='mywin.notebook.tab_message', value='LMS MMS Subject')

# message
gui.Label(name='label_message', left='10', top='142', parent='mywin.notebook.tab_message', text='Message')
gui.TextBox(name='message', left='80', top='142', width='150', height='150', parent='mywin.notebook.tab_message', value='message\nhere', multiline=True)

gui.Label(name='label_image', left='10', top='306', parent='mywin.notebook.tab_message', text='Image')
gui.Button(label='File', name='choose_image', left='80', top='300', width='85', default=True, parent='mywin.notebook.tab_message', onclick=ui.choose_image)
gui.TextBox(name='filename', left='166', top='301', parent='mywin.notebook.tab_message', value='')

# buttons
gui.Button(label='Send', name='send', left='80', top='326', width='85', default=True, parent='mywin.notebook.tab_message')
gui.StatusBar(name='statusbar', parent='mywin')

#### list ####
gui.Button(label='Refresh', name='refresh', left='290', top='5', width='85', default=True, parent='mywin.notebook.tab_list', onclick=ui.refresh_status)
gui.ListView(name='listview', height='99', left='0', top='30', width='396', 
             item_count=27, parent='mywin.notebook.tab_list', sort_column=0, 
             onitemselected="print ('sel %s' % event.target.get_selected_items())", )
gui.ListColumn(name='col_mid', text='Message ID', parent='listview', )
gui.ListColumn(name='col_callno', text='Phone Number', parent='listview', )
gui.ListColumn(name='col_status', text='Status', parent='listview', )

mywin = gui.get("mywin")

#mywin.onload = load
mywin['notebook']['tab_message']['send'].onclick = ui.send_message
mywin['menubar']['menu']['menu_quit'].onclick = ui.close_window

if __name__ == "__main__":
	mywin.show()
	mywin.title = "COOLSMS"
	mywin['statusbar'].text = "COOLSMS Status Bar"
	gui.main_loop()
