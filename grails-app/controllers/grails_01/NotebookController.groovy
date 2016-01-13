package grails_01

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NotebookController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Notebook.list(params), model: [notebookCount: Notebook.count()]
    }

    def show(Notebook notebook) {
        respond notebook
    }

    def create() {
        respond new Notebook(params)
    }

    @Transactional
    def save(Notebook notebook) {
        if (notebook == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (notebook.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond notebook.errors, view: 'create'
            return
        }

        notebook.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'notebook.label', default: 'Notebook'), notebook.id])
                redirect notebook
            }
            '*' { respond notebook, [status: CREATED] }
        }
    }

    def edit(Notebook notebook) {
        respond notebook
    }

    @Transactional
    def update(Notebook notebook) {
        if (notebook == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (notebook.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond notebook.errors, view: 'edit'
            return
        }

        notebook.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'notebook.label', default: 'Notebook'), notebook.id])
                redirect notebook
            }
            '*' { respond notebook, [status: OK] }
        }
    }

    @Transactional
    def delete(Notebook notebook) {

        if (notebook == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        notebook.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'notebook.label', default: 'Notebook'), notebook.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'notebook.label', default: 'Notebook'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
