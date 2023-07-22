package com.example.whatsapp.data

import java.util.regex.Pattern

public class passwordChecker(){

    private val atLeastOneDigit = Pattern.compile(".*\\d")
    private val atLeastOneUpperCase = Pattern.compile(".*[A-Z]")
    private val atLeastOneLowerCase = Pattern.compile(".*[a-z]")
    private val noSpace = Pattern.compile(" ")
    private val atLeastOneSymbol = Pattern.compile(".*[@#$%^&+-=?*]")
    private val atLeastEightChar = Pattern.compile(".{8,}")

   public fun checkNumberOfDigit(password:String): Boolean {
       return password.matches(atLeastOneDigit.toRegex())
   }
    public fun checkUpperCase(password:String): Boolean {
        return password.matches(atLeastOneUpperCase.toRegex())
    }

    public fun checkLowerCase(password:String): Boolean {
        try {
            var state:Boolean = false
            if(password.toCharArray().iterator().nextChar().isLowerCase()) {
                state = true
                return state
            }
                return state
        }catch (e: Exception){
            return false
        }


       // return password.matches(atLeastOneLowerCase.toRegex())
    }

    public fun checkSpaces(password:String): Boolean {
        return !password.matches(noSpace.toRegex())
    }

    public fun checkSymbol(password:String): Boolean {
        return password.matches(atLeastOneSymbol.toRegex())
    }

    public fun checkNumberOfChar(password:String): Boolean{
        return password.matches(atLeastEightChar.toRegex())
    }
}