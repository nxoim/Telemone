package com.number869.telemone.data


// access these by context.getSharedPreferences("AppPreferences",
// Context.MODE_PRIVATE).getSomethingSomething(
// AppSettings.Whatever.id, whateverDefaultValue)
enum class AppSettings(val id: String) {
	// whenever pp and tos change - bump the version number in the id
	AgreedToPpAndTos("userAgreedToV1OnWelcomeScreen"),
	SavedThemeItemDisplayType("savedThemeItemDisplayType");
}