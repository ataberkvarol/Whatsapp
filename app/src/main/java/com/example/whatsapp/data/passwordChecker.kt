package com.example.whatsapp.data

import java.util.regex.Pattern

public class passwordChecker(){

    private val atLeastOneDigit = Pattern.compile(".*[0-9]+.*")
    private val atLeastOneUpperCase = Pattern.compile(".*[a-z]+.*")
    private val atLeastOneLowerCase = Pattern.compile(".*[a-z]+.*")
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
        return password.matches(atLeastOneLowerCase.toRegex())
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
    public fun checkAllConditions(password:String): Boolean{
        if (password.matches(noSpace.toRegex()) && password.matches(atLeastEightChar.toRegex()) && password.matches(atLeastOneDigit.toRegex()) && password.matches(atLeastOneUpperCase.toRegex()) && password.matches(atLeastOneLowerCase.toRegex()) && password.matches(atLeastOneSymbol.toRegex())){
            return true
        }else
            return false
    }

}