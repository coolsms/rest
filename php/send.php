<?php 
include "header.php";

if(!$_SESSION['counter'])
	$_SESSION['counter'] = 1;
$submit=filter_input(INPUT_POST, 'submit');


if($submit && isset($submit))
{
	$options->to = filter_input(INPUT_POST, 'receiverNo');
	$options->from = filter_input(INPUT_POST, 'senderNo');
	$options->text = filter_input(INPUT_POST, 'msgContent');
	$options->type = filter_input(INPUT_POST, 'msgType');
	if($options->type == 'MMS')
		$options->image = filter_input(INPUT_POST, 'image');

	$rest->send($options);
	$result = new stdClass();
	$result = $rest->getResult();
	$index = $_SESSION['counter'];
	$_SESSION['result']->$index = $result;
	$_SESSION['counter']++;
	
	if($result->result_code == '00')
		echo "<script> alert(\"Message succesfully sent. \");</script>";
	else
		echo "<script> alert(\"".$result->code."\"); </script>";

}

?>

<form class="form-horizontal" method="post">
	<div class="control-group">
		<label class="control-label" for="msgType">Msg Type</label>
		<div class="controls">
			<input type=radio id="msgType" name="msgType" value="SMS" checked
				onchange="showImage(this)" />SMS &nbsp;
			<input type=radio id="msgType" name="msgType" value="LMS" 
				onchange="showImage(this)"/>LMS	&nbsp;
			<input type=radio id="msgType" name="msgType" value="MMS" 
				onchange="showImage(this)"/>MMS	&nbsp;
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="receiverNo">Receiver No.</label>
    	<div class="controls">
	      <input type="text" id="receiverNo" name="receiverNo" placeholder="ex) 01000000000" value="01000000000">
	    </div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="senderNo">Sender No.</label>
		<div class="controls">
			<input type=text id="senderNo" name="senderNo" placeholder="ex) 01000000000" value="01021725487">
		</div>
	</div>

	<div class="control-group" style="display:none;" id="titleDiv">
		<label class="control-label" for="senderNo">Title</label>
		<div class="controls">
			<input type=text id="title" name="title" placeholder="" value="">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="msgContent">Message</label>
		<div class="controls">
			<textarea name="msgContent" id="msgContent" rows=4>
hi there~
			</textarea>
		</div>
	</div>
	
	<div class="control-group" style="display:none;" id="imgDiv">
		<label class="control-label" for="msgContent">Image</label>
		<div class="controls">
			<input type="file" name="image" onchange="previewImage(this,'previewId')"/>
		</div><br>
		<label class="control-label" for="msgContent">Preview</label>
		<div id='previewId' class="img-polaroid" style='width: 150px; height: 150px; color: black; font-size: 9pt; border: 1px solid black; margin-left : 180px;'>
		</div>

	</div>




	<div class="control-group">
		<label class="control-label" for="msgContent"></label>
		<div class="controls">
			<input class="btn btn-primary" name="submit" type="submit" value="Send" />
		</div>
	</div>

<?php include "footer.php"; ?>



<script>

function showImage(selBtn)
{
	img = document.getElementById("imgDiv");
	title = document.getElementById("titleDiv");

	if(img.style.display =="none" && selBtn.value == "MMS")
		img.style.display = "block";
	else
		img.style.display = "none";

	if(selBtn.value == 'MMS' || selBtn.value == 'LMS')
		title.style.display = "block";
	else
		title.style.display = "none";
}


function previewImage(targetObj, previewId) 
{

	var preview = document.getElementById(previewId); //div id   
	var ua = window.navigator.userAgent;

	if (ua.indexOf("MSIE") > -1) 
	{//ie일때
		targetObj.select();
		try 
		{
			var src = document.selection.createRange().text; // get file full path 
			var ie_preview_error = document
					.getElementById("ie_preview_error_" + previewId);

			if (ie_preview_error) 
			{
				preview.removeChild(ie_preview_error); //error가 있으면 delete
			}

			var img = document.getElementById(previewId); //이미지가 뿌려질 곳 

			img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
				+ src + "', sizingMethod='scale')"; 
			//이미지 로딩, sizingMethod는 div에 맞춰서 사이즈를 자동조절 하는 역할
		} 
		catch (e) 
		{
			if (!document.getElementById("ie_preview_error_" + previewId)) {
				var info = document.createElement("<p>");
				info.id = "ie_preview_error_" + previewId;
				info.innerHTML = "a";
				preview.insertBefore(info, null);
			}
		}
	} else { //ie가 아닐때
		var files = targetObj.files;
		for ( var i = 0; i < files.length; i++) {

			var file = files[i];

			var imageType = /image.*/; 
			//이미지 파일일경우만.. 뿌려준다.
			if (!file.type.match(imageType))
				continue;

			var prevImg = document.getElementById("prev_" + previewId); 
			//이전에 미리보기가 있다면 삭제
			if (prevImg) {
				preview.removeChild(prevImg);
			}

			var img = document.createElement("img"); 
			//크롬은 div에 이미지가 뿌려지지 않는다. 그래서 자식Element를 만든다.
			img.id = "prev_" + previewId;
			img.classList.add("obj");
			img.file = file;
			img.style.width = '150px'; 
			//기본설정된 div의 안에 뿌려지는 효과를 주기 위해서 div크기와 같은 크기를 지정해준다.
			img.style.height = '150px';
			
			preview.appendChild(img);

			if (window.FileReader) { // FireFox, Chrome, Opera 확인.
				var reader = new FileReader();
				reader.onloadend = (function(aImg) {
					return function(e) {
						aImg.src = e.target.result;
					};
				})(img);
				reader.readAsDataURL(file);
			} else { // safari is not supported FileReader
				//alert('not supported FileReader');
				if (!document.getElementById("sfr_preview_error_"
						+ previewId)) {
					var info = document.createElement("p");
					info.id = "sfr_preview_error_" + previewId;
					info.innerHTML = "not supported FileReader";
					preview.insertBefore(info, null);
				}
			}
		}
	}
}

</script>

