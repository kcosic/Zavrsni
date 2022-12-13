package hr.kcosic.app.model.listeners

import hr.kcosic.app.model.entities.Car
import hr.kcosic.app.model.entities.Issue
import hr.kcosic.app.model.entities.Request

interface ButtonClickListener {
    fun onClick(s: String){
        throw Exception("Method not implemented")
    }
    fun onClick(o: Request){
        throw Exception("Method not implemented")
    }
    fun onClick(o: Car){
        throw Exception("Method not implemented")
    }
    fun onClick(issue: Issue){
        throw Exception("Method not implemented")
    }
}