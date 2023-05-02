package com.apptasya.moovy.domain.model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
sealed class UiText : Parcelable {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: String
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is UiText) {
            if (other is DynamicString && this is DynamicString) {
                return value == other.value
            }
            if (other is StringResource && this is StringResource) {
                return resId == other.resId && args.contentEquals(other.args)
            }
            if ((other is DynamicString && this is StringResource) ||
                (other is StringResource && this is DynamicString)
            ) {
                return false
            }
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}