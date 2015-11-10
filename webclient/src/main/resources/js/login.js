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

function encode(value) {
	return $.md5(value);
}