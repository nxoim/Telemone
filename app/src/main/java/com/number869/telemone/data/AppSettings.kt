package com.number869.telemone.data

object AppSettings {
	val agreedToConditions = Setting("userAgreedToV1OnWelcomeScreen", false)
	val savedThemeDisplayType = Setting("savedThemeItemDisplayType", 1)

	val lastAcceptedStockThemeHashDark = NullableSetting<String>("lastAcceptedStockThemeHashDark")
	val lastAcceptedStockThemeHashLight = NullableSetting<String>("lastAcceptedStockThemeHashLight")

	val lastDeclinedStockThemeHashDark = Setting("lastDeclinedStockThemeHashDark", "")
	val lastDeclinedStockThemeHashLight = Setting("lastDeclinedStockThemeHashLight", "")
}