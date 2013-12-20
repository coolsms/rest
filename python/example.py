import gui,Tkinter,tkFileDialog
import coolsms

class smsui:
	mtype = 'sms'
	imgfile = None

	def load(self, evt):
		return

	def send_message(self, evt):
		api_key = "NCS52A57F48C3D32"
		api_secret = "5AC44E03CE8E7212D9D1AD9091FA9966"
		user_id = "test"

		to = mywin['to'].value
		sender = mywin['from'].value
		message = mywin['message'].value.encode('utf-8')
		#print message
		#message = "TEST"
		r = coolsms.rest(api_key, api_secret, user_id)
		r.set_type(self.mtype)
		if self.mtype == 'mms':
			r.set_image(self.imgfile)
		r.send(to,sender,message)
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

ui = smsui()

gui.Window(name='mywin', title='COOLSMS Messaging'
		, resizable=True, height='360px'
		, left='180', top='24'
		, width='396px', bgcolor='#E0E0E0')

gui.MenuBar(name='menubar', fgcolor='#000000', parent='mywin')
gui.Menu(label='File', name='menu', fgcolor='#000000', parent='mywin.menubar')
gui.MenuItem(label='Quit', help='quit program', name='menu_quit', parent='mywin.menubar.menu')

# type
gui.Label(name='label_type', left='10', top='22', parent='mywin', text='Type')
gui.RadioButton(label='SMS', name='sms', left='80', top='22', parent='mywin', value=True, onclick=ui.choose_type)
gui.RadioButton(label='LMS', name='lms', left='130', top='22', parent='mywin', onclick=ui.choose_type)
gui.RadioButton(label='MMS', name='mms', left='180', top='22', parent='mywin', onclick=ui.choose_type)

# to
gui.Label(name='label_to', left='10', top='50', parent='mywin', text='To')
gui.TextBox(label='To ', name='to', left='80', top='50', width='150', parent='mywin', value='01000000000')

# from
gui.Label(name='label_from', left='10', top='84', parent='mywin', text='From')
gui.TextBox(name='from', left='80', top='84', width='150', parent='mywin', value='01000000000')

# message
gui.Label(name='label_message', left='10', top='116', parent='mywin', text='Message')
gui.TextBox(name='message', left='80', top='116', width='150', height='150', parent='mywin', value='message\nhere', multiline=True)

# buttons
gui.Button(label='Send', name='send', left='80', top='274', width='85', default=True, parent='mywin')
gui.Button(label='File', name='choose_image', left='160', top='274', width='85', default=True, parent='mywin', onclick=ui.choose_image)
gui.StatusBar(name='statusbar', parent='mywin')

mywin = gui.get("mywin")

#mywin.onload = load
mywin['send'].onclick = ui.send_message
mywin['menubar']['menu']['menu_quit'].onclick = ui.close_window

if __name__ == "__main__":
	mywin.show()
	mywin.title = "COOLSMS"
	mywin['statusbar'].text = "COOLSMS Status Bar"
	gui.main_loop()
