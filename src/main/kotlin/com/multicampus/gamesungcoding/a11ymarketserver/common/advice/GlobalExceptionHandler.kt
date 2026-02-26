package com.multicampus.gamesungcoding.a11ymarketserver.common.advice

import com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model.ErrorRespStatus
import com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model.RestErrorResponse
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginErrResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    private fun buildError(status: HttpStatus, errorStatus: ErrorRespStatus, message: String) =
        ResponseEntity.status(status).body(RestErrorResponse(status.value(), errorStatus, message))

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFoundException(ex: UserNotFoundException) =
        buildError(
            HttpStatus.NOT_FOUND,
            ErrorRespStatus.USER_NOT_FOUND,
            ex.message ?: "기본 에러 메시지"
        ).also { log.error(ex.message) }

    @ExceptionHandler(DataNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleDataNotFoundException(ex: DataNotFoundException) =
        buildError(
            HttpStatus.NOT_FOUND,
            ErrorRespStatus.DATA_NOT_FOUND,
            ex.message ?: "사용자를 찾을 수 없습니다."
        ).also { log.error(ex.message) }

    @ExceptionHandler(DataDuplicatedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicatedDataException(ex: DataDuplicatedException) =
        buildError(
            HttpStatus.CONFLICT,
            ErrorRespStatus.DUPLICATED_DATA,
            ex.message ?: "데이터가 이미 존재합니다."
        ).also { log.error(ex.message) }

    @ExceptionHandler(InvalidRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidRequestException(ex: InvalidRequestException) =
        buildError(
            HttpStatus.BAD_REQUEST,
            ErrorRespStatus.INVALID_REQUEST,
            ex.message ?: "잘못된 요청입니다."
        ).also { log.error(ex.message) }

    @ExceptionHandler(LoginFailedException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleLoginFailedException(ex: LoginFailedException) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body<LoginErrResponse?>(
            LoginErrResponse(
                ex.message ?: "로그인에 실패했습니다."
            )
        ).also { log.error(ex.message) }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException) =
        buildError(
            HttpStatus.BAD_REQUEST,
            ErrorRespStatus.INVALID_REQUEST,
            "잘못된 요청입니다. "
        ).also { log.error(ex.message) }
}