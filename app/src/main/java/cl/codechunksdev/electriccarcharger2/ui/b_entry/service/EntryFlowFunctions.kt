package cl.codechunksdev.electriccarcharger2.ui.b_entry.service

import android.text.TextUtils
import android.util.Patterns

fun isValidEmail(target: CharSequence?): Boolean {
   return !TextUtils.isEmpty(target) && target?.let {
      Patterns.EMAIL_ADDRESS
         .matcher(it)
         .matches()
   } == true
}

fun containNumber(string: String): Boolean {
   val chars = string.toCharArray()
   val sb = StringBuilder()
   for (c in chars) {
      if (Character.isDigit(c)) {
         sb.append(c)
      }
   }
   return sb.toString() != ""
}

fun containSymbol(string: String): Boolean {
   var count = 0
   for (i in 0 until string.length) {
      if (!Character.isDigit(string[i]) && !Character.isLetter(string[i]) && !Character.isWhitespace(string[i])) {
         count++
      }
   }
   return count > 0
}

