package ar.com.kairoslp.tripter.model

class Greeting (var name: String) {

    override fun toString(): String {
        return "Hello " + this.name
    }
}