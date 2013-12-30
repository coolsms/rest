<?php
	include "header.php";
	$rest->balance();
	$balance = $rest->getResult();
?>

<form class="form-horizontal" method="post">

<?php 
	if($balance->cash != null)
	{
?>
	<div class="control-group">
		<label class="control-label" for="balance_cash">Balance(cash)</label>
    	<div class="controls">
		  <input type="text" id="balance_cash" name="balance_cash" placeholder="" 
			value="<?php echo $balance->cash;?>" readonly>
	    </div>
	</div>

	<div class="control-group">
		<label class="control-label" for="balance_point">Balance(point)</label>
    	<div class="controls">
		  <input type="text" id="balance_point" name="balance_point" placeholder="" 
			value="<?php echo $balance->point;?>" readonly>
	    </div>
	</div>

<?php
	}
?>
	<div class="control-group">
		<label class="control-label" for="userid">CoolSMS ID</label>
    	<div class="controls">
		  <input type="text" id="userid" name="userid" placeholder="" 
			value="<?php echo $userid;?>">
	    </div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="api_key">API Key</label>
		<div class="controls">
			<input type=text id="api_key" name="api_key" placeholder=""
			value="<?php echo $apikey; ?>">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="api_secret">API Secret</label>
		<div class="controls">
			<input type=text id="api_secret" name="api_secret" 
			value="<?php echo $apisecret; ?>">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="test_mode">TEST Mode</label>
		<div class="controls">
			<input type=checkbox id="test_mode" name="test_mode" onchange="setTestMode(this.id)">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="api_secret"></label>
		<div class="controls">
			<input type="submit" name="submit" value="SAVE" class="btn btn-primary" />
		</div>
	</div>
</form>



<script>
function setTestMode(test)
{
	if(document.getElementById(test).checked)
	{
		document.getElementById("userid").value = "test";
		document.getElementById("api_key").value = "NCS52A57F48C3D32";
		document.getElementById("api_secret").value = "5AC44E03CE8E7212D9D1AD9091FA9966";
	}
}

</script>

