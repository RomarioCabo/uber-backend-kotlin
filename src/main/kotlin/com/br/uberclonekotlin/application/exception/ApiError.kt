package com.br.uberclonekotlin.application.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ApiError(
    val status: HttpStatus,
    val reasons: List<String?>
) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
}