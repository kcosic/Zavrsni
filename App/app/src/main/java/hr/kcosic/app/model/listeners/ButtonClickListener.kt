package hr.kcosic.app.model.listeners

import hr.kcosic.app.model.entities.Request

interface ButtonClickListener {
    fun onClick(s: String){
        throw Exception("Method not implemented")
    }
    fun onClick(o: Request){
        throw Exception("Method not implemented")
    }
}