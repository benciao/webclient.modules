function toggle_menu_visibility(id) {
	var maximizedDiv = document.getElementById('maximizedMenu');
	var minimizedDiv = document.getElementById('minimizedMenu');
	var contentWrapperDiv = document.getElementById('wrapper');

	var url = '/main/tooglemenue';

	if (id == 'minimizedMenu') {
		minimizedDiv.style.display = 'none';
		maximizedDiv.style.display = 'block';
		if (contentWrapperDiv != null) {
			contentWrapperDiv.style.top = '130px';
		}
		$('input.sizing').attr('value', 'false');
		url = url + '/false';
	} else {
		minimizedDiv.style.display = 'block';
		maximizedDiv.style.display = 'none';
		if (contentWrapperDiv != null) {
			contentWrapperDiv.style.top = '105px';
		}
		$('input.sizing').attr('value', 'true');
		url = url + '/true';
	}

	$.ajax(url);
}

function toggle_sub_menu_visibility() {
	var contentDiv = document.getElementById('content');

	if ($("#submenu").is(":visible")) {
		$("#navigation").removeClass('navigation');
		$("#navigation").addClass('navigation-off');
		$("#submenu").hide();
	} else {
		$("#navigation").removeClass('navigation-off');
		$("#navigation").addClass('navigation');
		$("#submenu").show();
	}
}

function scorePassword(pass) {
	// specify minimum pass length
	var min_length = 8;
	// specify minimum occurrences of specific characters
	var min_digits = 1; // digits
	var min_lower = 1; // lower case letters
	var min_upper = 1; // upper case letters
	var min_special = 1; // special characters

	var score = 0;

	// award one point for every char up to min_length
	if (pass.length > min_length) {
		score = min_length;
	} else {
		score = pass.length;
	}

	// check pass if contains minimum number of digits, special characters
	// and letters. For every true condition give one extra point.
	if (min_digits != 0 && pass.replace(/\D/g, "").length >= min_digits)
		score += 1;
	if (min_lower != 0 && pass.replace(/[^a-z]/g, "").length >= min_lower)
		score += 1;
	if (min_upper != 0 && pass.replace(/[^A-Z]/g, "").length >= min_upper)
		score += 1;
	if (min_special != 0 && pass.replace(/[^\W]/g, "").length >= min_special)
		score += 1;

	var active_conditions = 0;
	if (min_digits > 0)
		++active_conditions;
	if (min_lower > 0)
		++active_conditions;
	if (min_upper > 0)
		++active_conditions;
	if (min_special > 0)
		++active_conditions;

	// Calculate percentage and return value
	return (score / (min_length + active_conditions)) * 100;
}

function updateProgressBar(score) {
	var progress_bar = document.getElementById("password_progress_bar");
	progress_bar.style.width = score + '%';
}

function validatePassword() {
	var pw = $("#password").val();
	var confirmedPw = $("#password-confirm").val();

	return pw == confirmedPw;
}

$(document).ready(function() {

	$("#clientSelect").change(function() {
		$("#clientSelectForm").submit();
	});
	
	$("a").mousedown(function (e) { 
		var element = e.target;
		var link = $(element).parent(".feature-link");
		var featureLinkId = $(link).attr('id');
		
		var collectionIndex = featureLinkId.split("_");
		var featureName = collectionIndex[1];
		
		if( (e.which == 2) ) {
			$("#" + featureName).attr('target', '_blank');
	   }	   
	   
	   document.getElementById(featureName).submit();
	   e.preventDefault();
	});

	var classContent = $('#minimizedMenu').attr('class');
	if (classContent.indexOf("hide-menu") > -1) {
		var contentWrapperDiv = document.getElementById('wrapper');
		if (contentWrapperDiv != null) {
			contentWrapperDiv.style.top = '130px';
		}
	} else {
		var contentWrapperDiv = document.getElementById('wrapper');
		if (contentWrapperDiv != null) {
			contentWrapperDiv.style.top = '105px';
		}
	}
});

function encode(value) {
	return $.md5(value);
}
