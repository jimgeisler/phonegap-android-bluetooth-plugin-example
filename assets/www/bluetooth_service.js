/**
 * Facilitates the call into the Bluetooth plugin's BTDeviceManager.
 * 
 * @param win - success callback
 * @param fail - error callback
 */
var getDevices = function(win, fail) {
	if (window['BTDeviceManager'] == undefined)
		PluginManager.addService("BTDeviceManager","com.phonegap.BluetoothPlugin.BTDeviceManager");
	PhoneGap.execAsync(win, fail, "BTDeviceManager", "getDevices", []);
};

/**
 * Displays the list of found devices in the user interface.
 * @param number - device list (JSON)
 */
function updateDeviceList(number) {
	devices = number.split(",");
	for (d in devices) {
		$("#devices").append(devices[d]);
		$("#devices").append("<br/>");
	}
	$("#get-discoverable-devices").text('Find some devices!');
	$("#get-discoverable-devices").addClass('reallink');
	$("#get-discoverable-devices").removeClass('no_underline');
	$("#get-discoverable-devices").bind('click', getDevicesEvent);
	
}

$(document).ready( function () {
	$('#get-discoverable-devices').bind('click', getDevicesEvent);
});

/**
 * Event fired when user clicks the Get some devices link.
 */
function getDevicesEvent() {
	getDevices(
			function(r){
				// The call has been successfully made.
				$("#get-discoverable-devices").text('Gathering devices...');
				$("#get-discoverable-devices").unbind('click');
				$("#get-discoverable-devices").addClass('no_underline');
				$("#get-discoverable-devices").removeClass('reallink');
				//alert(r);
			},
			function(e){alert(e)}
	);
}