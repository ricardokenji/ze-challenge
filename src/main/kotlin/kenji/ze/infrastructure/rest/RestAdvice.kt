package kenji.ze.infrastructure.rest

import kenji.ze.application.exceptions.DuplicatedDocumentException
import kenji.ze.application.exceptions.NotFoundException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestControllerAdvisor: ResponseEntityExceptionHandler() {
    @ExceptionHandler(NotFoundException::class)
    fun handle(ex: NotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DuplicatedDocumentException::class)
    fun handle(ex: DuplicatedDocumentException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message, HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT)
    }

    data class ErrorResponse(
        val message: String?,
        val code: Int
    )
}