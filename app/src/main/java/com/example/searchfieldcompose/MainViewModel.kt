package com.example.searchfieldcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.concurrent.fixedRateTimer

class MainViewModel: ViewModel() {

    private val _searchText= MutableStateFlow("");
    val searchText=_searchText.asStateFlow();

    private val _isSearching= MutableStateFlow(false);
    val isSearching=_isSearching.asStateFlow();

    private val _persons= MutableStateFlow(allPersons);
    val persons=searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_persons){ text, persons ->
            if(text.isBlank()){
                persons
            }else{
                delay(2000L)
                persons.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _persons.value
        )
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