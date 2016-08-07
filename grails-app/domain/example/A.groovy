package example

class A {

    String name

    static hasMany = [ b: B ]


    static constraints = {
        name()
    }

    String toString() {
        name
    }
}
