package grails_01

class Notebook {

    String name
    String cpu
    String video
    Integer memory
    static belongsTo = [ vendor : Vendor ]

    static constraints = {
        name(blank: false)
        cpu(blank: false)
        video(blank: true)
        memory(blank: false)
    }
}
