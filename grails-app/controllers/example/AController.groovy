package example

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond A.list(params), model:[aCount: A.count()]
    }

    def show(A a) {
        respond a
    }

    def create() {
        respond new A(params)
    }

    @Transactional
    def save(A a) {
        if (a == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (a.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond a.errors, view:'create'
            return
        }

        a.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'a.label', default: 'A'), a.id])
                redirect a
            }
            '*' { respond a, [status: CREATED] }
        }
    }

    def edit(A a) {
        respond a
    }

    @Transactional
    def update(A a) {
        if (a == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (a.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond a.errors, view:'edit'
            return
        }

        a.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'a.label', default: 'A'), a.id])
                redirect a
            }
            '*'{ respond a, [status: OK] }
        }
    }

    @Transactional
    def delete(A a) {

        if (a == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        a.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'a.label', default: 'A'), a.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'a.label', default: 'A'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
