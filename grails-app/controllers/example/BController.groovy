package example

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond B.list(params), model:[bCount: B.count()]
    }

    def show(B b) {
        respond b
    }

    def create() {
        respond new B(params)
    }

    @Transactional
    def save(B b) {
        if (b == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (b.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond b.errors, view:'create'
            return
        }

        b.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'b.label', default: 'B'), b.id])
                redirect b
            }
            '*' { respond b, [status: CREATED] }
        }
    }

    def edit(B b) {
        respond b
    }

    @Transactional
    def update(B b) {
        if (b == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (b.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond b.errors, view:'edit'
            return
        }

        b.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'b.label', default: 'B'), b.id])
                redirect b
            }
            '*'{ respond b, [status: OK] }
        }
    }

    @Transactional
    def delete(B b) {

        if (b == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        b.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'b.label', default: 'B'), b.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'b.label', default: 'B'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
