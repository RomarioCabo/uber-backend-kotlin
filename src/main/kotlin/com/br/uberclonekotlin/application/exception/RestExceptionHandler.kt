package com.br.uberclonekotlin.application.exception

import com.br.uberclonekotlin.domain.user.BusinessRuleException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import mu.KotlinLogging
import java.util.stream.Collectors
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    private val logger = KotlinLogging.logger {}

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        fillErrorLog(ex)
        val error = "Malformed JSON request"
        return ResponseEntity(ApiError(HttpStatus.BAD_REQUEST, listOf(error)), status)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        fillErrorLog(ex)
        val errors = ex.bindingResult.fieldErrors.stream().map { fieldError: FieldError ->
            String.format(
                "'%s': %s", fieldError.field, fieldError.defaultMessage
            )
        }.collect(Collectors.toList())
        return ResponseEntity(ApiError(HttpStatus.BAD_REQUEST, errors), status)
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        fillErrorLog(ex)
        val error = ex.message
        return ResponseEntity(
            ApiError(HttpStatus.INTERNAL_SERVER_ERROR, listOf(error)), status
        )
    }

    /*@ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Any> {
        fillErrorLog(ex)
        return ResponseEntity(
            ApiError(HttpStatus.INTERNAL_SERVER_ERROR, listOf("Erro interno")),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }*/

    @ExceptionHandler(JsonMappingException::class)
    fun handleJsonMappingException(ex: JsonMappingException): ResponseEntity<Any> {
        fillErrorLog(ex)
        return ResponseEntity(
            ApiError(HttpStatus.INTERNAL_SERVER_ERROR, listOf("Error mapping json")),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(JsonProcessingException::class)
    fun handleJsonProcessingException(ex: JsonProcessingException): ResponseEntity<Any> {
        fillErrorLog(ex)
        return ResponseEntity(
            ApiError(HttpStatus.INTERNAL_SERVER_ERROR, listOf("Error processing json")),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(BusinessRuleException::class)
    fun handleBusinessRuleException(ex: BusinessRuleException): ResponseEntity<Any?>? {
        fillErrorLog(ex)
        val error: String? = ex.message
        return ResponseEntity(
            ApiError(HttpStatus.BAD_REQUEST, listOf(error)),
            HttpStatus.BAD_REQUEST
        )
    }

    private fun fillErrorLog(ex: Exception) {
        logger.info {
            "Received exception with message [${ex.message}] and stackTrace : [${ExceptionUtils.getStackTrace(ex)}]"
        }
    }
}