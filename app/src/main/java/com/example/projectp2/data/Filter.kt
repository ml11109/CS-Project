package com.example.projectp2.data

data class Filter(
    var title: String,
    var status: String
) {
    companion object {
        fun getEmpty(): Filter {
            return Filter("", "")
        }
    }
}
