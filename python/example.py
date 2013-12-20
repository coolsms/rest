import gui
import coolsms

def load(evt):
	return

def send_message(evt):
	api_key = "NCS52A57F48C3D32"
	api_secret = "5AC44E03CE8E7212D9D1AD9091FA9966"
	user_id = "test"

	to = mywin['to'].value
	sender = mywin['from'].value
	message = mywin['message'].value.encode('utf-8')
	#print message
	#message = "TEST"
	r = coolsms.rest(api_key, api_secret, user_id)
	r.send(to,sender,message)
	#r.status(gid=gid)
	#r.balance()
	#r.set_report_url()
	#r.cancel(gid=gid)
	#r.get_report_url()
	gui.alert("Sent!")


def close_window(evt):
	exit()

gui.Window(name='mywin', title='COOLSMS Messaging'
		, resizable=True, height='610px'
		, left='180', top='24'
		, width='396px', bgcolor='#E0E0E0')

gui.TextBox(name='message', left='0', top='0', width='150', height='160', parent='mywin', value='message\nhere', multiline=True)

gui.TextBox(name='to', left='0', top='170', width='150', parent='mywin', value='01000000000')
gui.TextBox(name='from', left='0', top='190', width='150', parent='mywin', value='01000000000')

gui.Button(label='Send', name='send', left='196', top='50', width='85', default=True, parent='mywin')
gui.Button(label='Quit', name='close', left='196', top='80', width='85', default=True, parent='mywin')
gui.StatusBar(name='statusbar', parent='mywin')

mywin = gui.get("mywin")

#mywin.onload = load
mywin['send'].onclick = send_message
mywin['close'].onclick = close_window
#mywin['slider'].onclick = slider_click

if __name__ == "__main__":
	mywin.show()
	mywin.title = "COOLSMS"
	mywin['statusbar'].text = "COOLSMS Status Bar"
	gui.main_loop()
