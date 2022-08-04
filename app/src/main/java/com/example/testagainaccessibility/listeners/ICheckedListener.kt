package com.example.testagainaccessibility.listeners

import com.example.testagainaccessibility.model.DataModel

interface ICheckedListener {
    fun onQualityChange(mList: HashSet<String>)
}