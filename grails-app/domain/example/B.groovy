package example

class B {

    String name

    static constraints = {
        name()
    }

    String toString() {
        name
    }
}
