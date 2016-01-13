package grails_01

class Vendor {

    String name
    static hasMany = [notebooks: Notebook]

    static constraints = {
    }

    @Override
    String toString(){name}
}
