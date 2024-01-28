package com.example.searchfieldcompose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlin.concurrent.fixedRateTimer

class MainViewModel: ViewModel() {

    private val _searchText= MutableStateFlow("");
    val searchText=_searchText.asStateFlow();

    private val _isSearching= MutableStateFlow(false);
    val isSearching=_isSearching.asStateFlow();

    private val _persons= MutableStateFlow(listOf<Person>());
    val persons=searchText
        .combine(_persons){ text, persons ->
            if(text.isBlank()){
                persons
            }else{
                persons.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
    fun onSearchTextChange(text: String){
        _searchText.value=text
    }

}

data class Person(
    val firstName: String,
    val lastName: String
){
    fun doesMatchSearchQuery(query: String): Boolean{
        val matchingCombinations= listOf(
            "$firstName$lastName",
            "$firstName $lastName",
            "${firstName.first()} ${lastName.first()}"
        )
        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }
}

private val allPersons=listOf(
    Person(
        firstName = "John",
        lastName = "Wick"
    ),
    Person(
        firstName = "John",
        lastName = "Smith"
    ),
    Person(
        firstName = "Brad",
        lastName = "Pitt"
    ),
    Person(
        firstName = "Emma",
        lastName = "Stone"
    ),
    Person(
        firstName = "Jennifer",
        lastName = "Lawrence"
    ),
    Person(
        firstName = "Scarlett",
        lastName ="Johansson"
    ),
    Person(
        firstName = "Johnny",
        lastName ="Depp"
    ),Person(
        firstName ="Angelina" ,
        lastName ="Jolie"
    ),
)